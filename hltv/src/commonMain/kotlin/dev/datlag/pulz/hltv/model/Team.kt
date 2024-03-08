package dev.datlag.pulz.hltv.model

import dev.datlag.tooling.async.scopeCatching
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

data class Team(
    val name: String,
    val imageLight: String? = null,
    val imageDark: String? = null,
    val social: Social,
    val country: Country? = null,
    val chart: Chart?
) {
    data class Social(
        val facebook: String? = null,
        val twitter: String? = null,
        val instagram: String? = null,
    ) {

        val facebookName: String? = facebook?.let {
            scopeCatching {
                Url(facebook).pathSegments.firstOrNull()
            }.getOrNull()
        }?.ifBlank {
            null
        } ?: facebook?.substringAfter("facebook.com/")?.ifBlank {
            null
        }

        val twitterName: String? = twitter?.let {
            scopeCatching {
                Url(twitter).pathSegments.firstOrNull()
            }.getOrNull()
        }?.ifBlank {
            null
        } ?: twitter?.substringAfter("twitter.com/")?.ifBlank {
            null
        } ?: twitter?.substringAfter("x.com/")?.ifBlank {
            null
        }

        val instaName: String? = (instagram?.let {
            scopeCatching {
                Url(instagram).pathSegments.firstOrNull()
            }.getOrNull()
        }?.ifBlank {
            null
        } ?: instagram?.substringAfter("instagram.com/")?.ifBlank {
            null
        })?.let {
            if (it.startsWith("http")) {
                return@let it
            }

            if (it.startsWith("@")) {
                it
            } else {
                "@$it"
            }
        }

        fun isEmpty(): Boolean {
            return facebook == null && twitter == null && instagram == null
        }
    }

    @Serializable
    data class Chart(
        @SerialName("dataSource") val dataSource: DataSource
    ) {

        @Serializable
        data class DataSource(
            @SerialName("chart") val chartData: ChartData,
            @SerialName("categories") val categories: List<CategoryContainer>,
            @SerialName("dataset") val dataSet: List<DataSet>
        ) {

            @Serializable
            data class ChartData(
                @SerialName("yAxisMinValue") val yAxisMinValue: Float,
                @SerialName("yAxisValuesStep") val yAxisValuesStep: Float
            )

            @Serializable
            data class CategoryContainer(
                @SerialName("category") val category: List<Category>
            ) {

                @Serializable
                data class Category(
                    @SerialName("label") val label: String,
                    @SerialName("vline") val vLine: Boolean = false
                )
            }

            @Serializable
            data class DataSet(
                @SerialName("seriesname") val seriesName: String? = null,
                @SerialName("data") val data: List<Data>
            ) {

                @Serializable
                data class Data(
                    @SerialName("value") val value: Float
                )
            }
        }
    }
}
