package dev.datlag.pulz.hltv

import dev.datlag.pulz.hltv.model.Country
import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.hltv.model.NewsPreview
import dev.datlag.pulz.hltv.model.Team
import dev.datlag.tooling.async.scopeCatching
import dev.datlag.tooling.async.suspendCatching
import dev.datlag.tooling.getDigitsOrNull
import io.ktor.client.*
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
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

        val teamEl = doc.querySelector(".leftCol")?.querySelectorAll("aside")?.flatMap {
            it.querySelectorAll(".rank")
        } ?: emptyList()
        val teams = teamEl.mapNotNull {
            val rank = it.querySelector(".rankNum")?.textContent()?.trim()
            val images = it.querySelectorAll("img")
            val imageLight = images.firstOrNull { img -> img.className()?.contains("day-only") == true }
            val imageDark = images.firstOrNull { img -> img.className()?.contains("night-only") == true }
            val fallbackImage = images.firstNotNullOfOrNull { i -> i.attr("src")?.ifBlank { null } }

            val info = it.querySelectorAll("a").firstNotNullOfOrNull { child ->
                if (child.className()?.contains("rankNum") == true) {
                    return@firstNotNullOfOrNull null
                }

                val href = child.attr("href")
                val name = child.textContent().trim()

                if (href.isNullOrBlank() || name.isBlank()) {
                    null
                } else {
                    href to name
                }
            }

            if (info != null && !rank.isNullOrBlank()) {
                Home.Team(
                    name = info.second,
                    rank = rank,
                    imageLight = imageLight?.attr("src")?.ifBlank { null } ?: fallbackImage,
                    imageDark = imageDark?.attr("src")?.ifBlank { null },
                    href = linkWithBase(info.first)
                )
            } else {
                null
            }
        }

        return Home(
            event = event,
            hero = hero,
            news = news,
            teams = teams
        )
    }

    suspend fun team(
        client: HttpClient,
        json: Json,
        href: String
    ): Team? {
        KtSoupParser.setClient(client)

        val doc = KtSoupParser.parseRemote(urlString = href)
        val title = doc.querySelector(".profile-team-name")?.textContent()

        val teamLogos = doc.querySelectorAll(".teamlogo")
        val teamLogoLight = teamLogos.firstOrNull { it.className()?.contains("day-only") == true }
        val teamLogoDark = teamLogos.firstOrNull { it.className()?.contains("night-only") == true }
        val fallbackLogo = teamLogos.firstNotNullOfOrNull { it.attr("src")?.ifBlank { null } }

        val facebook = (doc.querySelector(".facebook")?.parent() as? KtSoupElement)?.attr("href")
        val twitter = (doc.querySelector(".twitter")?.parent() as? KtSoupElement)?.attr("href")
        val instagram = (doc.querySelector(".instagram")?.parent() as? KtSoupElement)?.attr("href")

        val chart = doc.querySelector(".graph")?.attr("data-fusionchart-config")?.let {
            suspendCatching {
                json.decodeFromString<Team.Chart>(it)
            }.getOrNull()
        }

        val players = doc.querySelector(".players-table")?.querySelector("tbody")?.querySelectorAll("tr")?.mapNotNull {
            val name = it.querySelector(".playersBox-playernick")?.querySelector(".text-ellipsis")?.textContent()?.ifBlank { null }
            val image = it.querySelector(".playersBox-img-wrapper")?.querySelector("img")?.attr("src")?.ifBlank { null }

            val countryFlag = it.querySelector(".playersBox-playernick")?.querySelector("img")
            val country = getCountry(countryFlag)
            val rating = it.querySelector(".rating-cell")?.textContent()?.toFloatOrNull()
            val type = Team.Player.Type.valueOf(it.querySelector(".player-status")?.textContent())

            if (name.isNullOrBlank() || type == null) {
                null
            } else {
                Team.Player(
                    name = name,
                    image = image,
                    country = country,
                    rating = rating ?: -1F,
                    type = type
                )
            }
        } ?: emptyList()

        val countryFlag = doc.querySelector(".team-country")?.querySelector("img")
        val country = getCountry(countryFlag)

        return if (!title.isNullOrBlank()) {
            Team(
                name = title,
                imageLight = teamLogoLight?.attr("src")?.ifBlank { null } ?: fallbackLogo,
                imageDark = teamLogoDark?.attr("src")?.ifBlank { null },
                social = Team.Social(
                    facebook = facebook?.ifBlank { null },
                    twitter = twitter?.ifBlank { null },
                    instagram = instagram?.ifBlank { null }
                ),
                country = country,
                chart = chart,
                players = players
            )
        } else {
            null
        }
    }

    private fun getCountry(element: KtSoupElement?): Country? {
        val countryName = element?.attr("alt")?.ifBlank { null } ?: element?.attr("title")?.ifBlank { null }
        val countryCode = element?.attr("src")?.split('/')?.lastOrNull()?.substringBeforeLast('.')
        return if (countryName.isNullOrBlank() || countryCode.isNullOrBlank()) {
            null
        } else {
            Country(
                name = countryName,
                code = countryCode
            )
        }
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