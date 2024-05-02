package ostis.legislation.thirdparty.witai

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WitAI(
    private val authToken: String,
    private val http: OkHttpClient = OkHttpClient(),
    private val mapper: ObjectMapper = ObjectMapper()
        .registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

) {

    fun process(query: String): WitAIResponse {
        val url = BASE_URL.toHttpUrl().newBuilder()
            .addQueryParameter("v", genDateForRequest())
            .addQueryParameter("q", query)
            .build()

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $authToken")
            .build()

        val response = http.newCall(request).execute()
        return mapper.readValue(response.body?.string()!!)
    }

    companion object {
        private const val BASE_URL = "https://api.wit.ai/message"

        private fun genDateForRequest() = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    }

}
