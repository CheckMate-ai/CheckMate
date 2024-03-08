package backend.server.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import backend.server.AI.KeyWordAI

@RestController
class AIController {
    val keywordAI = KeyWordAI()

    @GetMapping("/keyword")
    fun ask(@RequestParam question: String): String {
        return keywordAI.getKeyword(question).toString()
    }
}
