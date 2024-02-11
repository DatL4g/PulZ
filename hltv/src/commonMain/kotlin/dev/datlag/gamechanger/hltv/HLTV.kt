package dev.datlag.gamechanger.hltv

import dev.datlag.gamechanger.hltv.model.Country
import dev.datlag.gamechanger.hltv.model.NewsPreview
import dev.datlag.tooling.async.suspendCatching
import dev.datlag.tooling.getDigitsOrNull
import io.ktor.client.*
import kotlinx.datetime.toLocalDate
import ktsoup.*

data object HLTV {

    suspend fun news(client: HttpClient): List<NewsPreview> {
        KtSoupParser.setClient(client)
        val doc = KtSoupParser.parseRemote(urlString = "https://www.hltv.org/news/archive")

        return doc.getElementsByClass("article").mapNotNull { element ->
            val link = element.attr("href")
            val title = element.querySelector(".newstext")?.textContent()

            if (link.isNullOrBlank() || title.isNullOrBlank()) {
                return@mapNotNull null
            }

            val tc = element.querySelector(".newstc")
            val commentElement = tc?.children()?.mapNotNull {
                it as? KtSoupElement
            }?.mapNotNull {
                if (it.className().isNullOrBlank()) {
                    it
                } else {
                    null
                }
            }?.lastOrNull() ?: tc?.children()?.lastOrNull()


            val commentsText = commentElement
                ?.textContent()
                ?.trim()
                ?.getDigitsOrNull()

            val comments = commentsText?.toIntOrNull() ?: 0

            val date = suspendCatching {
                element.querySelector(".newsrecent")?.textContent()?.toLocalDate()
            }.getOrNull()

            val countryFlag = element.querySelector(".newsflag")
            val countryName = countryFlag?.attr("alt")
            val countryCode = countryFlag?.attr("src")?.split('/')?.lastOrNull()?.substringBeforeLast('.')

            val country = if (countryName.isNullOrBlank() || countryCode.isNullOrBlank()) {
                null
            } else {
                Country(
                    name = countryName,
                    code = countryCode
                )
            }

            NewsPreview(
                title = title,
                link = link,
                comments = comments,
                country = country,
                date = date
            )
        }
    }
}