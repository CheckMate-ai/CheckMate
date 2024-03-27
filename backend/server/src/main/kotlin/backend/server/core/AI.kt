package backend.server.AI

import io.github.cdimascio.dotenv.Dotenv
import org.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

val AIPrefix = "AI: "
val HumanPrefix = "Human: "
val APIURL = "https://api-inference.huggingface.co/models/"
val APIKEY = Dotenv.load()["HUGGING_FACE_API_KEY"]
val DefaultModel = "mistralai/Mixtral-8x7B-Instruct-v0.1"

fun sanitize(text: String): String {
        val r = Regex("[^\\w\\d\\s.,!?;:'\"\\(\\)\\-–—\\/\\\\%$€£¥@&*+=\\[\\]{}<>`~^:]+")
        val singleSpace = Regex("\\s+")
        return text.replace(r, "").trim().replace(singleSpace, " ")
}

fun sendAndReceive(prompt: String, model: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBearerAuth(APIKEY)
        val request = HttpEntity(mapOf("inputs" to prompt), headers)
        val response =
                        RestTemplate().exchange(
                                                        APIURL + model,
                                                        HttpMethod.POST,
                                                        request,
                                                        String::class.java
                                        )
        /* Handle errors and show msg */
        if (response.statusCode.isError)
                        throw Exception("Error: ${response.statusCode} ${response.body}")
        val responseJSON = JSONObject(response.body!!.substringAfter('[').substringBeforeLast(']'))
        val output = responseJSON.get("generated_text").toString().substringAfterLast(HumanPrefix)
        return output
}

open class GenericAI(val Context: String, open val Model: String = DefaultModel) {
        val CONTEXT = sanitize(AIPrefix + Context + "\n\n")
        protected fun ask(question: String): String {
                var questionclean = sanitize(question + "\n\n")
                return sendAndReceive(CONTEXT + HumanPrefix + questionclean, Model)
        }
}

open class KeyWordAI() :
                GenericAI(
                                """Hello.
                        I have one main function:
                        Extract five key words that summarize the main points of a given text at this format {"result" : [keywords]}
                     """,
                ) {

        fun getKeyword(statement: String): List<String> {
                val question = """Here is a statement:\n $statement give me key words."""
                val rawJson = super.ask(question).substringAfterLast('{').substringBefore('}')
                var keywords = JSONObject('{' + rawJson + '}')
                var keywordlist =
                                keywords.getJSONArray("result").toList().map {
                                        sanitize(it.toString())
                                }
                return keywordlist
        }
}

enum class CheckerResults(val value: String) {
        LEGIT("Legit"),
        FAKE("Fake"),
        UNSURE("Unsure");

        fun allValues(): String {
                return listOf(LEGIT, FAKE, UNSURE).joinToString("|")
        }

        fun toInt(): Int {
                return when (this) {
                        LEGIT -> 0
                        FAKE -> 1
                        UNSURE -> 2
                }
        }
}

fun FromString(value: String): CheckerResults {
        return when (value) {
                "Legit" -> CheckerResults.LEGIT
                "Fake" -> CheckerResults.FAKE
                "Unsure" -> CheckerResults.UNSURE
                else -> throw IllegalArgumentException("Invalid value $value")
        }
}

open class FactCheckerAI() :
                GenericAI(
                                """Hello.
                        I have one main function:
                        Given a sentence and some sources, I return an evaluation of the veracity of the sentence only based on the sources provided and not on any external information.
                        Here are the 3 possible outputs:
                        1. "%s" if the sentence seems true.
                        2. "%s" if the sentence seems false.
                        3. "%s" if the sentence is ambiguous or cannot be verified.
                        My output is at this format {"result" : "output"}
                     """.format(
                                                CheckerResults.LEGIT.value,
                                                CheckerResults.FAKE.value,
                                                CheckerResults.UNSURE.value
                                )
                ) {
        private fun process(sources: List<String>, sentence: String): Pair<Int, String> {
                var sourcesStr = sources.joinToString("\n")
                val question =
                                """Here is some sources: \n $sourcesStr \n\n and a sentence: \n $sentence \n\n is it legit?."""
                val result = super.ask(question)
                var label = CheckerResults.UNSURE
                try {
                        var labelJson =
                                        JSONObject(
                                                        '{' +
                                                                        result.substringAfterLast(
                                                                                                        '{'
                                                                                        )
                                                                                        .substringBefore(
                                                                                                        '}'
                                                                                        ) +
                                                                        '}'
                                        )
                        if (labelJson.has("result"))
                                        label = FromString(labelJson.getString("result"))
                } catch (e: Exception) {
                        println("NoLabelFound")
                }
                return Pair(label.toInt(), result.substringAfterLast(AIPrefix))
        }

        fun getCheck(sources: List<String>, sentence: String): JSONObject {
                val p = process(sources, sentence)
                var jsonOutput = JSONObject()
                jsonOutput.put("label", p.first)
                jsonOutput.put("explanation", p.second)
                jsonOutput.put("sources", sources)
                jsonOutput.put("sentence", sentence)
                return jsonOutput
        }
}
