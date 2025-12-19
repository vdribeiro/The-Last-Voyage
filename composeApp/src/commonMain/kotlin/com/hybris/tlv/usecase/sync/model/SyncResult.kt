package com.hybris.tlv.usecase.sync.model

internal data class SyncResult(
    val archive: DataSource,
    val translations: DataSource,
    val catastrophes: DataSource,
    val engines: DataSource,
    val stellarHosts: DataSource,
    val planets: DataSource,
    val events: DataSource,
    val achievements: DataSource,
    val credits: DataSource
)
