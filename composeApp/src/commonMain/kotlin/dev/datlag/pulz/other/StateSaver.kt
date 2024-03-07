package dev.datlag.pulz.other

data object StateSaver {
    var sekretLibraryLoaded: Boolean = false

    data object List {
        var discoverOverview: Int = 0
        var discoverOverviewOffset: Int = 0

        var counterStrikeOverview: Int = 0
        var counterStrikeOverviewOffset: Int = 0
    }
}