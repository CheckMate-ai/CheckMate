package backend.server.api

import backend.server.core.GoogleAPI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController {
    val googleSearchService = GoogleAPI()

    @GetMapping("/search")
    fun search(@RequestParam query: String): String {
        return googleSearchService.getGoogleLinks(query)
    }
}
