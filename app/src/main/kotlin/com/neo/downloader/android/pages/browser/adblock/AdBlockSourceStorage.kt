package com.neo.downloader.android.pages.browser.adblock

import androidx.datastore.core.DataStore
import com.neo.downloader.shared.util.ConfigBaseSettingsByJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

@Serializable
data class AdBlockFilterSource(
    val id: String,
    val name: String,
    val url: String,
    val enabled: Boolean = true,
    val etag: String? = null,
    val lastModified: String? = null,
)

interface IAdBlockSourceStorage {
    val sourcesFlow: MutableStateFlow<List<AdBlockFilterSource>>
}

class AdBlockSourceDatastoreStorage(
    dataStore: DataStore<List<AdBlockFilterSource>>,
) : IAdBlockSourceStorage, ConfigBaseSettingsByJson<List<AdBlockFilterSource>>(dataStore) {
    override val sourcesFlow: MutableStateFlow<List<AdBlockFilterSource>> = data
}

fun defaultAdBlockSources(): List<AdBlockFilterSource> {
    return listOf(
        AdBlockFilterSource(
            id = "stevenblack_hosts",
            name = "StevenBlack Hosts",
            url = "https://raw.githubusercontent.com/StevenBlack/hosts/master/hosts",
            enabled = true,
        ),
        AdBlockFilterSource(
            id = "hagezi_light",
            name = "HaGeZi Light",
            url = "https://raw.githubusercontent.com/hagezi/dns-blocklists/main/hosts/light.txt",
            enabled = true,
        ),
        AdBlockFilterSource(
            id = "goodbye_ads",
            name = "GoodbyeAds",
            url = "https://raw.githubusercontent.com/jerryn70/GoodbyeAds/master/Hosts/GoodbyeAds.txt",
            enabled = true,
        ),
        AdBlockFilterSource(
            id = "ublock_filters",
            name = "uBlock Filters",
            url = "https://raw.githubusercontent.com/uBlockOrigin/uAssets/master/filters/filters.txt",
            enabled = true,
        ),
    )
}
