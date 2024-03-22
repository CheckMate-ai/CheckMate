package backend.server.api

import backend.server.AI.AdviceAI
import backend.server.AI.KeyWordAI
import backend.server.core.GoogleAPI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AIController {
    val keywordAI = KeyWordAI()
    val adviceAI = AdviceAI()
    val googleSearchService = GoogleAPI()

    @GetMapping("/keyword")
    fun ask(@RequestParam question: String): String {
        return keywordAI.getKeyword(question).toString()
    }

    @GetMapping("/advice")
    fun advice(@RequestParam question: String): String {
        val keywords = keywordAI.getKeyword(question)
        val linksJson = googleSearchService.getGoogleSources(keywords)
        linksJson.put("question", question)
        linksJson.put("keywords", keywords)
        return linksJson.toString()
    }
}
