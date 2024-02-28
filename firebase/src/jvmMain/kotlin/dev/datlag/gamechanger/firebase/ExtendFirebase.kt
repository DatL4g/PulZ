package dev.datlag.gamechanger.firebase

import android.app.Application
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

fun FirebaseFactory.Companion.initialize(
    projectId: String?,
    applicationId: String,
    apiKey: String
): FirebaseFactory {
    return FirebaseFactory.Initialized(
        Firebase.initialize(
            context = Application(),
            options = FirebaseOptions(
                projectId = projectId,
                applicationId = applicationId,
                apiKey = apiKey
            )
        )
    )
}