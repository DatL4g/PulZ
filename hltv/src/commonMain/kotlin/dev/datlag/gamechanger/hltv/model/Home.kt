package dev.datlag.gamechanger.hltv.model

data class Home(
    val event: Event?
) {
    data class Event(
        val live: Boolean,
        val title: String,
        val image: String,
        val href: String
    )
}
