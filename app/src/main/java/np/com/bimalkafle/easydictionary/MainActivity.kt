package np.com.bimalkafle.easydictionary

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import np.com.bimalkafle.easydictionary.adapter.MeaningAdapter
import np.com.bimalkafle.easydictionary.databinding.ActivityMainBinding
import np.com.bimalkafle.easydictionary.model.WordResult
import np.com.bimalkafle.easydictionary.viewmodel.DictionaryViewModel
import np.com.bimalkafle.easydictionary.viewmodel.UiState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var meaningAdapter: MeaningAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences  // Added for history

    private val viewModel: DictionaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        checkAuthenticationStatus()
        setupRecyclerView()
        setupSearchButton()
        observeViewModelState()

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE)

        // 🔹 Open History Page when "History" button is clicked
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun checkAuthenticationStatus() {
        if (auth.currentUser == null) {
            navigateToLogin()
        }

        binding.btnLogout?.setOnClickListener {
            auth.signOut()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun setupRecyclerView() {
        meaningAdapter = MeaningAdapter(emptyList())
        binding.meaningRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = meaningAdapter
        }
    }

    private fun setupSearchButton() {
        binding.searchBtn.setOnClickListener {
            val word = binding.searchInput.text.toString().trim()
            if (word.isNotEmpty()) {
                viewModel.getMeaning(word)

                // 🔹 Save search word to SharedPreferences
                val historySet = sharedPreferences.getStringSet("history", mutableSetOf()) ?: mutableSetOf()
                historySet.add(word)

                val editor = sharedPreferences.edit()
                editor.putStringSet("history", historySet)
                editor.apply()

            } else {
                Toast.makeText(this, "Please enter a word", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModelState() {
        lifecycleScope.launch {
            viewModel.wordResult.collect { state ->
                when (state) {
                    is UiState.Initial -> resetUI()
                    is UiState.Loading -> setInProgress(true)
                    is UiState.Success -> {
                        setInProgress(false)
                        updateUI(state.data)
                    }
                    is UiState.Error -> {
                        setInProgress(false)
                        handleError(state.message)
                    }
                }
            }
        }
    }

    private fun updateUI(wordResults: List<WordResult>) {
        if (wordResults.isNotEmpty()) {
            val wordResult = wordResults.first()
            binding.wordTextview.text = wordResult.word
            binding.phoneticTextview.text = wordResult.phonetic ?: "No phonetic information"
            meaningAdapter.updateNewData(wordResult.meanings)
        }
    }

    private fun resetUI() {
        binding.wordTextview.text = ""
        binding.phoneticTextview.text = ""
        meaningAdapter.updateNewData(emptyList())
    }

    private fun setInProgress(inProgress: Boolean) {
        binding.searchBtn.visibility = if (inProgress) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (inProgress) View.VISIBLE else View.INVISIBLE
    }

    private fun handleError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
