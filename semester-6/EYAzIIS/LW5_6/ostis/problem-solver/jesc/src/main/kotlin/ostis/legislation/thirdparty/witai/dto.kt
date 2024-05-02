package ostis.legislation.thirdparty.witai

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WitAIResponse(
    val text: String,
    val intents: List<WitAIIntent>,
    val entities: Map<String, List<WitAIEntity>>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WitAIIntent(
    val confidence: Double,
    val id: String,
    val name: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WitAIEntity(
    val body: String,
    val confidence: Double,
    val end: Int,
    val id: String,
    val name: String,
    val role: String,
    val start: Int,
    val suggested: Boolean,
    val type: String,
    val value: String
)
