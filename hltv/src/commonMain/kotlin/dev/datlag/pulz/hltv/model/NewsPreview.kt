package dev.datlag.pulz.hltv.model

import kotlinx.datetime.LocalDate

data class NewsPreview(
    val title: String,
    val comments: Int,
    val country: Country?,
    val link: String,
    val date: LocalDate?
)
