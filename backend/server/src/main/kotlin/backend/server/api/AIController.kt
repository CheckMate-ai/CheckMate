package backend.server.api

import backend.server.AI.FactCheckerAI
import backend.server.AI.KeyWordAI
import backend.server.core.GoogleAPI
import org.json.JSONObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AIController {
    val keywordAI = KeyWordAI()
    val FactCheckerAI = FactCheckerAI()

    @GetMapping("/keyword")
    fun ask(@RequestParam question: String): String {
        return keywordAI.getKeyword(question).toString()
    }

    @GetMapping("/advice")
    fun advice(@RequestParam sources: List<String>, @RequestParam question: String): String {
        return FactCheckerAI.getCheck(sources, question).toString()
    }
}
