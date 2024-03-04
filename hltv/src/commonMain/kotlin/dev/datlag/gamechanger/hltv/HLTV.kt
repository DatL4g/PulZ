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
                    href = linkWithBase(eventHref)
                )
            } else {
                null
            }
        }

        val heroEl = doc.querySelector(".hero-con")?.querySelector("a")
        val hero = heroEl?.let {
            val href = it.attr("href")
            val image = it.querySelector("img")?.attr("src")

            if (!href.isNullOrBlank() && !image.isNullOrBlank()) {
                Home.Hero(
                    image = image,
                    href = linkWithBase(href)
                )
            } else {
                null
            }
        }

        val newsEl = doc.querySelectorAll(".standard-list").flatMap { it.querySelectorAll("a") }
        val news = newsEl.mapNotNull {
            val href = it.attr("href")

            val countryFlag = it.querySelector(".newsflag")
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

            val image = it.querySelector(".featured-newsimage")?.attr("src")
            val title = it.querySelector(".featured-newstext")?.textContent()?.ifBlank {
                null
            } ?: it.querySelector(".newstext")?.textContent()?.ifBlank { null }
            val text = it.querySelector(".featured-small-newstext")?.textContent()?.ifBlank { null }

            if (!href.isNullOrBlank() && !title.isNullOrBlank()) {
                Home.News(
                    image = image,
                    country = country,
                    title = title,
                    text = text,
                    link = linkWithBase(href)
                )
            } else {
                null
            }
        }

        return Home(
            event = event,
            hero = hero,
            news = news ?: emptyList()
        )
    }

    private fun linkWithBase(link: String): String {
        return if (link.startsWith("http://") || link.startsWith("https://")) {
            link
        } else {
            "https://hltv.org${
                if (link.startsWith("/")) {
                    link
                } else {
                    "/$link"
                }
            }"
        }
    }
}