package dev.datlag.gamechanger.rawg

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import dev.datlag.gamechanger.rawg.model.Games

interface RAWG {

    @GET("games")
    suspend fun games(@Query("key") key: String): Games

    @GET("games")
    suspend fun games(@Query("key") key: String, @Query("page") page: Int): Games

}