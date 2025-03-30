package np.com.bimalkafle.easydictionary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import np.com.bimalkafle.easydictionary.model.WordResult
import np.com.bimalkafle.easydictionary.network.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

class DictionaryViewModel : ViewModel() {
    private val _wordResult = MutableStateFlow<UiState<List<WordResult>>>(UiState.Initial)
    val wordResult: StateFlow<UiState<List<WordResult>>> = _wordResult

    fun getMeaning(word: String) {
        viewModelScope.launch {
            _wordResult.value = UiState.Loading
            try {
                val response = RetrofitInstance.dictionaryApi.getMeaning(word)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _wordResult.value = UiState.Success(body)
                    } ?: run {
                        _wordResult.value = UiState.Error("No definition found")
                    }
                } else {
                    _wordResult.value = UiState.Error("Error: ${response.message()}")
                }
            } catch (e: IOException) {
                _wordResult.value = UiState.Error("Network error. Check your connection.")
            } catch (e: HttpException) {
                _wordResult.value = UiState.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                _wordResult.value = UiState.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }
}

// UI State to manage different states of the API call
sealed class UiState<out T> {
    object Initial : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
