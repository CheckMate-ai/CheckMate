package backend.server.core

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.customsearch.v1.CustomSearchAPI
import com.google.api.services.customsearch.v1.model.Result
import io.github.cdimascio.dotenv.Dotenv
import java.time.LocalDate
import java.time.Month
import org.json.JSONObject

class GoogleAPI {
    private val CUSTOM_SEARCH_API_KEY: String = Dotenv.load()["CUSTOM_SEARCH_API_KEY"]
    private val SAFE_CHECK_MATE_CUSTOM_SEARCH_CX: String =
            Dotenv.load()["SAFE_CHECK_MATE_CUSTOM_SEARCH_CX"]
    private val UNSAFE_CHECK_MATE_CUSTOM_SEARCH_CX =
            Dotenv.load()["UNSAFE_CHECK_MATE_CUSTOM_SEARCH_CX"]
    private val transport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
    private val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
    private val safeCustomSearchAPI: CustomSearchAPI.Cse.List =
            CustomSearchAPI.Builder(transport, jsonFactory, null)
                    .setApplicationName("SafeCheckMateSearch")
                    .build()
                    .cse()
                    .list()
                    .setCx(SAFE_CHECK_MATE_CUSTOM_SEARCH_CX)
                    .setKey(CUSTOM_SEARCH_API_KEY)
    private val unsafeCustomSearchAPI =
            CustomSearchAPI.Builder(transport, jsonFactory, null)
                    .setApplicationName("UnsafeCheckMateSearch")
                    .build()
                    .cse()
                    .list()
                    .setCx(UNSAFE_CHECK_MATE_CUSTOM_SEARCH_CX)
                    .setKey(CUSTOM_SEARCH_API_KEY)
    private val monthMap =
            mapOf(
                    "Jan" to "JANUARY",
                    "Feb" to "FEBRUARY",
                    "Mar" to "MARCH",
                    "Apr" to "APRIL",
                    "May" to "MAY",
                    "Jun" to "JUNE",
                    "Jul" to "JULY",
                    "Aug" to "AUGUST",
                    "Sep" to "SEPTEMBER",
                    "Oct" to "OCTOBER",
                    "Nov" to "NOVEMBER",
                    "Dec" to "DECEMBER"
            )

    fun getGoogleLinks(query: String): String {
        val keywords = query.split(",").joinToString(separator = "+")
        val safeSearch = safeCustomSearchAPI.setQ(keywords).execute()
        val list: MutableList<Result>? =
                when {
                    safeSearch.isNullOrEmpty() -> null
                    else -> safeSearch.items
                }
        var response: List<JSONObject>
        if (list != null) {
            response = createResponseJson(list, true)
        } else {
            response = createResponseJson(unsafeCustomSearchAPI.setQ(keywords).execute().items, false)
        }

        return response.toString()
    }

    fun createResponseJson(resultList: MutableList<Result>, isSafe: Boolean): List<JSONObject> {
        var id = 0
        return resultList.map {
            val computedPagemap = computePagemap(JSONObject(it.pagemap))
            JSONObject()
                    .put("id", id++)
                    .put("link", it.link)
                    .put("title", it.title)
                    .put("article_snippet", it.snippet)
                    .put("image_preview_link", computedPagemap.first)
                    .put("safe", isSafe)
                    .put("date", parseDate(it.snippet))
                    .put("website", computedPagemap.second)
        }
    }

    fun computePagemap(pagemapJson: JSONObject): Pair<String, String> {
        var preview = "NO_PREVIEW"
        var websiteName = "NO_NAME"
        val cseImageArray = pagemapJson.optJSONArray("cse_image")
        if (cseImageArray != null && cseImageArray.length() > 0) {
            val cseImageObject = cseImageArray.getJSONObject(0)
            val src = cseImageObject.optString("src")
            if (src != null && src != "") preview = src.toString()
        }
        val cseMetatags = pagemapJson.optJSONArray("metatags")
        if (cseMetatags != null && cseMetatags.length() > 0) {
            val cseMetatagObject = cseMetatags.getJSONObject(0)
            val siteName = cseMetatagObject.optString("og:site_name")
            if (siteName != null && siteName != "") websiteName = siteName.toString()
        }
        return Pair<String, String>(preview, websiteName)
    }

    fun parseDate(snippet: String): String {
        val date = snippet.split(" ...")[0].toString().replace(",", "")
        var articleDate =
                when {
                    date.matches("[A-Za-z]{3} [0-9]{1,2} [0-9]{4}".toRegex()) -> {
                        val splitDate = date.split(" ")
                        val year = splitDate[2].toInt()
                        val month = monthMap[splitDate[0]]
                        val day = splitDate[1].toInt()
                        if (month != null) LocalDate.of(year, Month.valueOf(month), day)
                        else "NO DATE"
                    }
                    date.matches("[0-9]+ day(s)? ago".toRegex()) -> {
                        val daysAgo = date.split(" ")[0].toLong()
                        LocalDate.now().minusDays(daysAgo)
                    }
                    date.matches("[0-9]+ (minute|hour)(s)? ago".toRegex()) -> {
                        LocalDate.now()
                    }
                    else -> {
                        return "NO_DATE"
                    }
                }
        return articleDate.toString()
    }
}
