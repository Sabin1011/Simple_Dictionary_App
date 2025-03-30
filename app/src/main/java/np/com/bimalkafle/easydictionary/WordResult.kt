package np.com.bimalkafle.easydictionary.model

data class WordResult(
    val word: String,
    val phonetic: String? = null,
    val meanings: List<Meaning>
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)

data class Definition(
    val definition: String,
    val example: String? = null,
    val synonyms: List<String>? = null
)