package com.hybris.tlv.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.hybris.tlv.core.flow.Dispatcher

/**
 * Converts a [Query] to a [Flow] of [List] of [Domain]s.
 */
internal fun <Entity: Any, Domain> Query<Entity>.asFlow(transform: (Entity) -> Domain): Flow<List<Domain>> =
    asFlow()
        .mapToList(context = Dispatcher.IO)
        .map { list -> list.map(transform) }
        .distinctUntilChanged()
        .flowOn(context = Dispatcher.Default)
