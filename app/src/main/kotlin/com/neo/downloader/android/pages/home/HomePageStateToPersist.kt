package com.neo.downloader.android.pages.home

import arrow.optics.optics
import com.neo.downloader.android.pages.home.sections.sort.DownloadSortBy
import com.neo.downloader.shared.ui.widget.sort.Sort
import kotlinx.serialization.Serializable

@optics
@Serializable
data class HomePageStateToPersist(
    val sortBy: Sort<DownloadSortBy> = Sort<DownloadSortBy>(DownloadSortBy.DataAdded, Sort.DEFAULT_IS_DESCENDING)
) {
    companion object {}
}
