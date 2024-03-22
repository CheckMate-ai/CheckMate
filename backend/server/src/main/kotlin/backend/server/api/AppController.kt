package backend.server.api

import backend.server.AI.KeyWordAI
import backend.server.core.GoogleAPI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AppController {
    val keywordAI = KeyWordAI()
    val googleSearchService = GoogleAPI()

    @GetMapping("/getSources")
    fun ask(@RequestParam question: String): String {
        val keywords = keywordAI.getKeyword(question)
        println("Keywords: $keywords")
        val linksJson = googleSearchService.getGoogleSources(keywords)
        linksJson.put("question", question)
        linksJson.put("keywords", keywords)
        return linksJson.toString()
    }
}
