package np.com.bimalkafle.easydictionary

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import np.com.bimalkafle.easydictionary.adapter.HistoryAdapter

class HistoryActivity : AppCompatActivity() {

    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var btnBack: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        btnBack = findViewById(R.id.btnBack)

        // Load history data
        loadSearchHistory()

        // Back Button Click - Go Back to MainActivity
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // Load search history from SharedPreferences
    private fun loadSearchHistory() {
        sharedPreferences = getSharedPreferences("SearchHistory", MODE_PRIVATE)
        val historySet = sharedPreferences.getStringSet("history", setOf()) ?: setOf()
        val searchHistory = historySet.toList()

        if (searchHistory.isEmpty()) {
            Toast.makeText(this, "No search history found.", Toast.LENGTH_SHORT).show()
        }

        // Set up RecyclerView adapter
        historyAdapter = HistoryAdapter(searchHistory)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter
    }
}
