package dev.datlag.pulz.rawg

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.datlag.pulz.rawg.model.Game
import dev.datlag.pulz.rawg.model.Games

interface RAWG {

    @GET("games")
    suspend fun games(
        @Query("key") key: String,
        @Query("page") page: Int? = null,
        @Query("dates") dates: String? = null,
        @Query("metacritic") metacritic: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("tags") tags: String? = null,
        @Query("search") search: String? = null
    ): Games

    @GET("games/{slug}")
    suspend fun game(
        @Path("slug") slug: String,
        @Query("key") key: String,
    ): Game

}