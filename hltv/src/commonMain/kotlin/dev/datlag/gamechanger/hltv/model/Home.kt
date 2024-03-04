package dev.datlag.gamechanger.hltv.model

data class Home(
    val event: Event?,
    val hero: Hero?,
    val news: List<News>
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
}
