package dev.datlag.gamechanger.other

data object StateSaver {
    var sekretLibraryLoaded: Boolean = false

    data object List {
        var discoverOverview: Int = 0
        var discoverOverviewOffset: Int = 0
    }
}