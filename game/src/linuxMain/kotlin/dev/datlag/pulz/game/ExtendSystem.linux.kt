package dev.datlag.pulz.game

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import okio.Path
import okio.Path.Companion.toPath

actual val systemRoots: Set<Path>
    get() = setOf("/".toPath(normalize = true))

actual fun open(uri: String) {
    Command("xdg-open")
        .arg(uri)
        .stdout(Stdio.Null)
        .spawn()
}