package backend.server.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import backend.server.core.GoogleAPI

@RestController
class SearchController {
    val googleSearchService = GoogleAPI()

    @GetMapping("/search")
    fun search(@RequestParam query: String): List<String> {
        return googleSearchService.getGoogleLinks(query)
    }
}
