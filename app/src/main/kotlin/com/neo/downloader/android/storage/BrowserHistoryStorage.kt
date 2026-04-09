package com.neo.downloader.android.storage

import com.neo.browser.logic.history.NeoBrowserHistoryEntry
import com.neo.downloader.shared.util.ConfigBaseSettingsByJson
import kotlinx.coroutines.flow.MutableStateFlow

class BrowserHistoryStorage(
    dataStore: androidx.datastore.core.DataStore<List<NeoBrowserHistoryEntry>>,
) : ConfigBaseSettingsByJson<List<NeoBrowserHistoryEntry>>(dataStore) {
    val historyFlow: MutableStateFlow<List<NeoBrowserHistoryEntry>> = data
}
