package dev.datlag.gamechanger.game

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import okio.Path
import okio.Path.Companion.toPath

// ToDo("resolve all system roots natively")
actual val systemRoots: Set<Path>
    get() = setOf("C:".toPath(normalize = true))

actual fun open(uri: String) {
    Command("rundll32")
        .args(listOf("url.dll,FileProtocolHandler", uri))
        .stdout(Stdio.Null)
        .spawn()
}