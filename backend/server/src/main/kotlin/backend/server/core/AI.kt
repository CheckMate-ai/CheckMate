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
        return text.replace(r, "").trim()
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
                var questionclean = sanitize(question)
                return sendAndReceive(CONTEXT + HumanPrefix + questionclean, Model)
        }
}

open class AdviceAI() :
                GenericAI(
                                """Hello.
                        I have one main function:
                        Given a sentence and some context, I return an evaluation of the veracity of the sentence.
                        Here are the 3 possible outputs:
                        1. "Legit" if the sentence seems true.
                        2. "Fake" if the sentence seems false.
                        3. "Unsure" if the sentence is ambiguous or cannot be verified.
                        My output is at this format {"result" : "output"}
                     """,
                ) {

        fun getAdvice(sources: List<String>, sentence: String): String {
                val question =
                                """Here is some sources: \n $sources \n\n and a sentence: \n $sentence \n\n is it legit?."""
                val rawJson = super.ask(question).substringAfterLast('{').substringBefore('}')
                var advice = JSONObject('{' + rawJson + '}')
                return advice.getString("result")
        }
}

open class KeyWordAI() :
                GenericAI(
                                """Hello.
                        I have one main function:
                        Extract five key words that summarize the main points of a given text at this format {"result" : [keywords]}
                     """,
                ) {

        fun getKeyword(statement: String): JSONObject {
                val question = """Here is a statement:\n $statement give me key words."""
                val rawJson = super.ask(question).substringAfterLast('{').substringBefore('}')
                var keywords = JSONObject('{' + rawJson + '}')
                var keywordlist =
                                keywords.getJSONArray("result").toList().map {
                                        sanitize(it.toString())
                                }
                keywords = JSONObject().put("result", keywordlist)
                return keywords
        }
}

/* open class Convo() {
        class QA(val question: String, val answer: String)
        val QAList = mutableListOf<QA>()
        fun addQA(question: String, answer: String) {
                QAList.add(QA(question, answer))
        }
        fun getQA(): List<QA> {
                return QAList
        }
}

open class ConversationAI(val Context: String, open val Model: String = DefaultModel) {
        val CONTEXT = sanitize(AIPrefix + Context + "\n\n")
        val ENDPOINT = APIURL + Model
} */
