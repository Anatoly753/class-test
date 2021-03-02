package io.realworld.app.config

import com.meilisearch.sdk.Client
import com.meilisearch.sdk.Config

class MeiliSearchConfig(url: String, key: String) {
    private var client: Client = Client(Config(url, key))

    fun getClient(): Client {
        return client
    }
}