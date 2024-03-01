package dev.datlag.gamechanger.octane

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import dev.datlag.gamechanger.octane.model.Events

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
}