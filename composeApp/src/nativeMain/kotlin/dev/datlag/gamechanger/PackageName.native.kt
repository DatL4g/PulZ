package dev.datlag.gamechanger

object PackageName { }

actual fun getPackageName(): String {
    val clazz = PackageName::class

    return run {
        var cutPackage = clazz.simpleName?.let { clazz.qualifiedName!!.substringBeforeLast(it) } ?: clazz.qualifiedName!!

        if (cutPackage.startsWith('.')) {
            cutPackage = cutPackage.substring(1)
        }
        if (cutPackage.endsWith('.')) {
            cutPackage = cutPackage.substringBeforeLast('.')
        }

        cutPackage
    }.trim()
}