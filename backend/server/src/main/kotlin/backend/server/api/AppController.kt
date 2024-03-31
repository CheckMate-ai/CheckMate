package backend.server.api

import backend.server.AI.FactCheckerAI
import backend.server.AI.KeyWordAI
import backend.server.core.GoogleAPI
import org.json.JSONObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin

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
        val sources = googleSearchService.getGoogleSources(keywordAI.getKeyword(question))
        var snippetLS = mutableListOf<String>()
        for (src in sources.getJSONArray("sources")) {
            val snippet = JSONObject(src.toString()).getString("article_snippet")
            snippetLS.add(snippet)
        }
        return FactCheckerAI.getCheck(snippetLS, question).toString()
    }
}
