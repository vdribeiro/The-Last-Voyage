package com.hybris.tlv.data.storage

/**
 * Files used in storage.
 */
internal sealed class FilePath(val path: String) {
    // Configs
    data object Configs: FilePath(path = "configs.json")
    data object Preferences: FilePath(path = "preferences.json")

    // Archive
    data object ArchiveStellarHosts: FilePath(path = "hosts.json")
    data object ArchivePlanets: FilePath(path = "planets.json")
}