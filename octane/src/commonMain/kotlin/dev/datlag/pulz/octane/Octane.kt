package dev.datlag.pulz.octane

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.datlag.pulz.octane.model.Events
import dev.datlag.pulz.octane.model.Games
import dev.datlag.pulz.octane.model.Matches

interface Octane {

    @GET("events")
    suspend fun events(
        @Query("name") name: String? = null,
        @Query("tier") tier: String? = null,
        @Query("region") region: String? = null,
        @Query("mode") mode: Int? = null,
        @Query("group") group: String? = null,
        @Query("before") before: String? = null,
        @Query("after") after: String? = null,
        @Query("date") date: String? = null,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
    ) : Events

    @GET("matches")
    suspend fun matches(
        @Query("event") event: String? = null,
        @Query("stage") stage: Int? = null,
        @Query("qualifier") qualifier: Boolean? = null,
        @Query("tier") tier: String? = null,
        @Query("region") region: String? = null,
        @Query("mode") mode: Int? = null,
        @Query("group") group: String? = null,
        @Query("before") before: String? = null,
        @Query("after") after: String? = null,
        @Query("bestOf") bestOf: Int? = null,
        @Query("reverseSweep") reverseSweep: Boolean? = null,
        @Query("reverseSweepAttempt") reverseSweepAttempt: Boolean? = null,
        @Query("player") player: String? = null,
        @Query("team") team: String? = null,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
    ) : Matches

    @GET("events/{id}/matches")
    suspend fun eventMatches(@Path("id") id: String) : Matches

    @GET("games")
    suspend fun games(
        @Query("event") event: String? = null,
        @Query("stage") stage: Int? = null,
        @Query("match") match: String? = null,
        @Query("qualifier") qualifier: Boolean? = null,
        @Query("tier") tier: String? = null,
        @Query("region") region: String? = null,
        @Query("mode") mode: Int? = null,
        @Query("group") group: String? = null,
        @Query("before") before: String? = null,
        @Query("after") after: String? = null,
        @Query("bestOf") bestOf: Int? = null,
        @Query("player") player: String? = null,
        @Query("team") team: String? = null,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
    ) : Games
}