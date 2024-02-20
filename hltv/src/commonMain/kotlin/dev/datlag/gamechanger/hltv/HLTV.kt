package dev.datlag.gamechanger.hltv

import dev.datlag.gamechanger.hltv.model.Country
import dev.datlag.gamechanger.hltv.model.Home
import dev.datlag.gamechanger.hltv.model.NewsPreview
import dev.datlag.tooling.async.suspendCatching
import dev.datlag.tooling.getDigitsOrNull
import io.ktor.client.*
import kotlinx.datetime.toLocalDate
import ktsoup.*

data object HLTV {

    suspend fun home(client: HttpClient): Home {
        KtSoupParser.setClient(client)
        val doc = KtSoupParser.parseRemote(urlString = "https://www.hltv.org")

        val eventEl = doc.querySelector(".event-hub")?.querySelector("a")
        val event = eventEl?.let {
            val eventHref = it.attr("href")
            val live = it.querySelector(".event-live") != null
            val image = it.querySelector("img")?.attr("src")
            val title = it.querySelector(".event-hub-title")?.textContent()

            if (!eventHref.isNullOrBlank() && !image.isNullOrBlank() && !title.isNullOrBlank()) {
                Home.Event(
                    live = live,
                    title = title,
                    image = image,
                    href = eventHref
                )
            } else {
                null
            }
        }
        return Home(
            event = event
        )
    }

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