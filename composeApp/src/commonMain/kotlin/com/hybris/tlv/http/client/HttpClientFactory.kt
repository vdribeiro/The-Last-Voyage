package com.hybris.tlv.http.client

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.HttpClient

internal interface HttpClientFactory {
    fun buildExoplanetHttpClient(): HttpClient

    fun buildSupabaseHttpClient(): SupabaseClient =
        createSupabaseClient("supabaseUrl", "supabaseKey") {
            install(plugin = Postgrest)
        }
}
