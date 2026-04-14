package com.hybris.tlv.data.storage

/**
 * A type-safe registry of file paths used for local persistence.
 * Each [FilePath] represent a specific JSON file stored in the application's internal data directory.
 *
 * @property path The relative filename (including extension) used on disk.
 */
internal sealed class FilePath(val path: String) {
    // Configs
    data object Configs: FilePath(path = "configs.json")
    data object Preferences: FilePath(path = "preferences.json")

    // Archive
    data object ArchiveStellarHosts: FilePath(path = "hosts.json")
    data object ArchivePlanets: FilePath(path = "planets.json")
}