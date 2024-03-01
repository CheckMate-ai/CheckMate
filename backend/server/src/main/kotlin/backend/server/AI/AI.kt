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

open class AI(val Context: String, open val Model: String = DefaultModel) {
        val CONTEXT = AIPrefix + Context + "\n\n"
        val ENDPOINT = APIURL + Model

        protected fun ask(question: String): String {
                val prompt = CONTEXT + HumanPrefix + question
                val headers = HttpHeaders()
                headers.contentType = MediaType.APPLICATION_JSON
                headers.setBearerAuth(APIKEY)
                val request = HttpEntity(mapOf("inputs" to prompt), headers)
                val response =
                                RestTemplate().exchange(
                                                                ENDPOINT,
                                                                HttpMethod.POST,
                                                                request,
                                                                String::class.java
                                                )
                /* Handle errors and show msg */
                if (response.statusCode.isError)
                                throw Exception("Error: ${response.statusCode} ${response.body}")
                val responseJSON =
                                JSONObject(
                                                response.body!!.substringAfter('[')
                                                                .substringBeforeLast(']')
                                )
                return responseJSON.get("generated_text").toString().substringAfterLast(HumanPrefix)
        }
}

class KeyWordAI() :
                AI(
                                """Hello.
                        I have one main function:
                        Extract five key words that summarize the main points of a given text at this format {"result" : [keywords]}
                     """,
                ) {

        fun getKeyword(statement: String): JSONObject {
                val question =
                                """Here is a statement: $statement\n
                        give me key words."""
                val rawJson = super.ask(question).substringAfterLast('{').substringBefore('}')
                return JSONObject('{' + rawJson + '}')
        }
}
