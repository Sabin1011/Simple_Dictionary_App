package np.com.bimalkafle.easydictionary.network

import np.com.bimalkafle.easydictionary.model.WordResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("{word}")
    suspend fun getMeaning(@Path("word") word: String): Response<List<WordResult>>
}