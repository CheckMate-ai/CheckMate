package backend.server.core

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.customsearch.v1.CustomSearchAPI
import io.github.cdimascio.dotenv.Dotenv
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class GoogleAPI {
        init {

                print("HERE")
        }

        val CUSTOM_SEARCH_API_KEY: String =
                        URLEncoder.encode(
                                        Dotenv.load()["CUSTOM_SEARCH_API_KEY"],
                                        StandardCharsets.UTF_8.toString()
                        )
        val CUSTOM_SEARCH_CX: String =
                        URLEncoder.encode(
                                        Dotenv.load()["CHECK_MATE_CUSTOM_SEARCH_CX"],
                                        StandardCharsets.UTF_8.toString()
                        )
        val transport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
        val customSearchAPI: CustomSearchAPI.Cse.List =
                        CustomSearchAPI.Builder(transport, jsonFactory, null)
                                        .setApplicationName("CheckMateSearch")
                                        .build()
                                        .cse()
                                        .list()
                                        .setCx(CUSTOM_SEARCH_CX)
                                        .setKey(CUSTOM_SEARCH_API_KEY)

        fun getGoogleLinks(query: String): List<String> {
                val keywords = query.split(",").joinToString(separator = "+")

                val list = customSearchAPI.setQ(keywords).execute().items
                val result = list.map { it.link }
                return result
        }
}
