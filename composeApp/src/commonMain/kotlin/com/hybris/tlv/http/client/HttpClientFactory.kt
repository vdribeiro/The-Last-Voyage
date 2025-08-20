package com.hybris.tlv.http.client

import com.hybris.tlv.config.Config
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.HttpClient

internal interface HttpClientFactory {
    fun buildExoplanetHttpClient(): HttpClient

    fun buildSupabaseHttpClient(): SupabaseClient =
        createSupabaseClient(
            supabaseUrl = Config.SUPABASE_URL,
            supabaseKey = Config.SUPABASE_KEY
        ) {
            install(plugin = Postgrest)
        }
}
