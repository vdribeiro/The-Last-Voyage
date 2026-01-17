package com.hybris.tlv.data.serializer

internal sealed class JsonFile(val path: String) {
    // Configs
    data object Configs: JsonFile(path = "configs.json")
    data object Preferences: JsonFile(path = "preferences.json")

    // Archive
    data object ArchiveStellarHosts: JsonFile(path = "hosts.json")
    data object ArchivePlanets: JsonFile(path = "planets.json")
}
