package com.neo.downloader.android.storage

import com.neo.browser.logic.session.NeoBrowserSessionState
import com.neo.downloader.shared.util.ConfigBaseSettingsByJson
import kotlinx.coroutines.flow.MutableStateFlow

class BrowserSessionStorage(
    dataStore: androidx.datastore.core.DataStore<NeoBrowserSessionState>,
) : ConfigBaseSettingsByJson<NeoBrowserSessionState>(dataStore) {
    val sessionFlow: MutableStateFlow<NeoBrowserSessionState> = data
}
