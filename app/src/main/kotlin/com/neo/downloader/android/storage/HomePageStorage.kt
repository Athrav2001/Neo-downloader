package com.neo.downloader.android.storage

import androidx.datastore.core.DataStore
import com.neo.downloader.android.pages.home.HomePageStateToPersist
import com.neo.downloader.android.pages.home.sortBy
import com.neo.downloader.shared.util.ConfigBaseSettingsByJson

class HomePageStorage(
    dataStore: DataStore<HomePageStateToPersist>,
) : ConfigBaseSettingsByJson<HomePageStateToPersist>(
    dataStore = dataStore,
) {
    val sortBy = from(HomePageStateToPersist.sortBy)
}
