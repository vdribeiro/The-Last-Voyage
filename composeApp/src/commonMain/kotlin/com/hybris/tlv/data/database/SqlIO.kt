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
 * Extension function to transform a [Query] into a reactive [Flow] of domain models.
 *
 * This utility streamlines the data flow from the persistence layer to the domain layer by:
 * 1. **Observing Changes:** Automatically re-emitting data whenever the underlying database tables are modified.
 * 2. **Offloading I/O:** Using [mapToList] on [Dispatcher.IO] to ensure the database cursor reading doesn't block the main thread.
 * 3. **Domain Mapping:** Converting database [Entity] objects into platform-agnostic [Domain] objects via the provided [transform] lambda.
 * 4. **Optimizing Emissions:** Applying [distinctUntilChanged] to prevent downstream UI recompositions if the data remains structurally identical after a mapping.
 * 5. **Context Shifting:** Moving the final collection processing to [Dispatcher.Default] to keep the pipeline responsive.
 *
 * @param Entity The auto-generated data class representing the table row.
 * @param Domain The clean data class used within the domain layer.
 * @param transform A mapper function to convert [Entity] to [Domain].
 * @return A [Flow] emitting a list of mapped domain objects.
 */
internal fun <Entity: Any, Domain> Query<Entity>.asFlow(transform: (Entity) -> Domain): Flow<List<Domain>> =
    asFlow()
        .mapToList(context = Dispatcher.IO)
        .map { list -> list.map(transform) }
        .distinctUntilChanged()
        .flowOn(context = Dispatcher.Default)
