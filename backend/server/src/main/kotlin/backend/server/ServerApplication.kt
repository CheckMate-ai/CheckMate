package backend.server

import backend.server.AI.KeyWordAI
import backend.server.googleAPI.GoogleAPI
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication class ServerApplication
fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

@RestController
class MessageController {
    @GetMapping("/") fun index(@RequestParam("name") name: String) = "Hello, $name!"
}

@RestController
class SearchController {

    val googleSearchService = GoogleAPI()

    @GetMapping("/search")
    fun search(@RequestParam query: String): List<String> {
        return googleSearchService.getGoogleLinks(query)
    }
}

@RestController
class AIController {
    val keywordAI = KeyWordAI()

    @GetMapping("/keyword")
    fun ask(@RequestParam question: String): String {
        return keywordAI.getKeyword(question).toString()
    }
}
