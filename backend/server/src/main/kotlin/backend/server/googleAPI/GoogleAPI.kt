package backend.server.googleAPI

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class GoogleAPI() {
    fun getGoogleLinks(query: String): List<String> {
        val queries = query.split(",")
        val googleUrl = "https://www.google.com/search?q=" + queries.joinToString(separator="+")
        val document: Document = Jsoup.connect(googleUrl).get()
        val links = document.select("div.tF2Cxc a[href]")
        return links.map { it.absUrl("href") }
    }
}