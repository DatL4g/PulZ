package dev.datlag.gamechanger

object PackageName { }

actual fun getPackageName(): String {
    val clazz = PackageName::class

    return (clazz.java.packageName.ifBlank { null } ?: run {
        var cutPackage = (clazz.qualifiedName ?: clazz.java.canonicalName).substringBeforeLast(clazz.simpleName ?: clazz.java.simpleName)

        if (cutPackage.startsWith('.')) {
            cutPackage = cutPackage.substring(1)
        }
        if (cutPackage.endsWith('.')) {
            cutPackage = cutPackage.substringBeforeLast('.')
        }

        cutPackage
    }).trim()
}