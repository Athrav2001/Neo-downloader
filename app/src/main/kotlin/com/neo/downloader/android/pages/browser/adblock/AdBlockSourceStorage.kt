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
            id = "1dm_pack",
            name = "1DM+ Pack",
            url = "https://files.catbox.moe/8lz5w0.zip",
            enabled = true,
        ),
        AdBlockFilterSource(
            id = "easylist",
            name = "EasyList",
            url = "https://easylist.to/easylist/easylist.txt",
            enabled = true,
        ),
        AdBlockFilterSource(
            id = "adguard_mobile",
            name = "AdGuard Mobile Ads",
            url = "https://filters.adtidy.org/extension/chromium/filters/11.txt",
            enabled = true,
        ),
    )
}
