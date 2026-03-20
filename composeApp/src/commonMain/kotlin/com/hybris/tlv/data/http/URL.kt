package com.hybris.tlv.data.http

internal sealed class URL(val path: String) {
    data object ExoplanetArchive: URL(path = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync")
    data object Configs: URL(path = "https://gist.githubusercontent.com/vdribeiro/eb23013b329c47317622981187df3f23/raw/configs.json")
    data object Translations: URL(path = "https://gist.githubusercontent.com/vdribeiro/90daf9ebde2b8e37ce893e49e8d7f7c7/raw/translations.json")
    data object Catastrophes: URL(path = "https://gist.githubusercontent.com/vdribeiro/27258c022708a20066f793996031d884/raw/catastrophes.json")
    data object CatastrophesTranslations: URL(path = "https://gist.githubusercontent.com/vdribeiro/0d3721f5140b97ac89fcd69a6ad21cfa/raw/catastrophes_translations.json")
    data object Engines: URL(path = "https://gist.githubusercontent.com/vdribeiro/4168f14c15569dd6dd4a57af4ee37a99/raw/engines.json")
    data object EnginesTranslations: URL(path = "https://gist.githubusercontent.com/vdribeiro/9fec1be524d012cccc0a85a1a373b2a2/raw/engines_translations.json")
    data object StellarHosts: URL(path = "https://gist.githubusercontent.com/vdribeiro/7e0ccc933aa6826bf1f427aa036f5793/raw/hosts.json")
    data object Planets: URL(path = "https://gist.githubusercontent.com/vdribeiro/95146e01cd2b5c322a5e49ee4b9e3261/raw/planets.json")
    data object Events: URL(path = "https://gist.githubusercontent.com/vdribeiro/c2cf6a30e9be34c512f77ceea583bc71/raw/events.json")
    data object EventsTranslations: URL(path = "https://gist.githubusercontent.com/vdribeiro/30999c73fe1f319702421c3b98ac8b9d/raw/events_translations.json")
    data object Achievements: URL(path = "https://gist.githubusercontent.com/vdribeiro/bf676c0c196c64ed40a7a1e7635035ea/raw/achievements.json")
    data object AchievementsTranslations: URL(path = "https://gist.githubusercontent.com/vdribeiro/521a84cb4bd129a8956f17849c24dfe5/raw/achievements_translations.json")
    data object Credits: URL(path = "https://gist.githubusercontent.com/vdribeiro/a0dd7e6766e8bb40d1028a62d4d8f941/raw/credits.json")
}
