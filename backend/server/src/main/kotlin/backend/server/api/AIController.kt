package backend.server.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import backend.server.AI.KeyWordAI

@RestController
class AIController {
    val keywordAI = KeyWordAI()

    @CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
    @GetMapping("/keyword")
    fun ask(@RequestParam question: String): String {
        return keywordAI.getKeyword(question).toString()
    }
}
