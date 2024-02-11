package dev.datlag.gamechanger.hltv.model

import kotlin.jvm.JvmOverloads
import dev.datlag.tooling.country.Country as ToolingCountry

data class Country @JvmOverloads constructor(
    val name: String,
    val code: String,
    val tooling: ToolingCountry? = ToolingCountry.forCodeOrNull(code)
)
