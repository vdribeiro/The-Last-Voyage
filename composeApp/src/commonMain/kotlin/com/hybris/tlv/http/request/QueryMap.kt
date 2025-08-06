package com.hybris.tlv.http.request

import com.hybris.tlv.http.toBoolString

/**
 * Hash map implementation for API specific values. It maps the values to the proper query key.
 */
internal class QueryMap: MutableMap<String, String> by HashMap() {

    var paginate: Boolean? = null
        set(value) {
            field = value
            set(key = PAGINATE, value = value.toBoolString())
        }
    var offset: Long? = null
        set(value) {
            field = value
            set(key = OFFSET, value = value?.toString())
        }
    var limit: Long? = null
        set(value) {
            field = value
            set(key = LIMIT, value = value?.toString())
        }

    fun set(key: String, value: String?) = apply {
        if (value == null) remove(key = key) else put(key = key, value = value)
    }

    fun nextPage() {
        val limit = this.limit ?: return
        val offset = this.offset ?: 0
        this.offset = offset + limit
    }

    companion object {
        private const val PAGINATE = "count"
        private const val OFFSET = "offset"
        private const val LIMIT = "limit"
    }
}
