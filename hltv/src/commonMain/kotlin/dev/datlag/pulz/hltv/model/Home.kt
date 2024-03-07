package dev.datlag.pulz.hltv.model

data class Home(
    val event: Event?,
    val hero: Hero?,
    val news: List<News>,
    val teams: List<Team>
) {
    data class Event(
        val live: Boolean,
        val title: String,
        val image: String,
        val href: String
    )
    data class Hero(
        val image: String,
        val href: String
    )

    data class News(
        val image: String? = null,
        val country: Country? = null,
        val title: String,
        val text: String? = null,
        val link: String,
    )

    data class Team(
        val name: String,
        val rank: String,
        val imageLight: String? = null,
        val imageDark: String? = null,
        val href: String
    )
}
