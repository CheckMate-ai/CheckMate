package backend.server.api

import backend.server.core.GoogleAPI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.json.JSONObject

@RestController
class SearchController {
    val googleSearchService = GoogleAPI()

    @GetMapping("/search")
    fun search(@RequestParam keywords: String): String {
        val keywordsList = keywords.split(",")
        val query = JSONObject().put("result",keywordsList)
        return googleSearchService.getGoogleSources(query).toString()
    }
}
