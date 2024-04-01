package backend.server.api

import backend.server.AI.FactCheckerAI
import backend.server.AI.KeyWordAI
import backend.server.core.GoogleAPI
import org.json.JSONObject
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AppController {
    val keywordAI = KeyWordAI()
    val FactCheckerAI = FactCheckerAI()
    val googleSearchService = GoogleAPI()

    @CrossOrigin
    @GetMapping("/getSources")
    fun ask(@RequestParam question: String): String {
        val keywords = keywordAI.getKeyword(question)
        val linksJson = googleSearchService.getGoogleSources(keywords)
        linksJson.put("question", question)
        linksJson.put("keywords", keywords)
        return linksJson.toString()
    }

    @CrossOrigin
    @GetMapping("/getAdvice")
    fun getAdvice(question: String): String {
        val sources =
                googleSearchService
                        .getGoogleSources(keywordAI.getKeyword(question))
                        .getJSONArray("sources")
        var snippetLS = mutableListOf<String>()
        for (src in sources) {
            val snippet = JSONObject(src.toString()).getString("article_snippet")
            snippetLS.add(snippet)
        }
        var output = FactCheckerAI.getCheck(snippetLS, question)
        output.put("sources", sources)
        return output.toString()
    }
}
