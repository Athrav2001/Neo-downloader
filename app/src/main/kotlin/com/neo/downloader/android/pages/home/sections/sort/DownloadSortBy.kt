package com.neo.downloader.android.pages.home.sections.sort

import com.neo.downloader.resources.Res
import com.neo.downloader.shared.ui.widget.sort.ComparatorProvider
import com.neo.downloader.shared.util.ui.icon.MyIcons
import com.neo.downloader.monitor.IDownloadItemState
import com.neo.downloader.monitor.statusOrFinished
import ir.neo.util.compose.IconSource
import ir.neo.util.compose.StringSource
import ir.neo.util.compose.asStringSource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class DownloadSortBy(
    val selector: (IDownloadItemState) -> Comparable<*>,
    val icon: IconSource,
    val name: StringSource,
) : ComparatorProvider<IDownloadItemState> {
    override fun comparator(): Comparator<IDownloadItemState> {
        return compareBy(selector)
    }

    @Serializable
    @SerialName("name")
    object Name : DownloadSortBy(
        selector = { it.name },
        icon = MyIcons.alphabet,
        name = Res.string.name.asStringSource(),
    )

    @Serializable
    @SerialName("dateAdded")
    object DataAdded : DownloadSortBy(
        selector = { it.dateAdded },
        icon = MyIcons.clock,
        name = Res.string.date_added.asStringSource(),
    )

    @Serializable
    @SerialName("status")
    data object Status : DownloadSortBy(
        selector = { it.statusOrFinished().order },
        icon = MyIcons.info,
        name = Res.string.status.asStringSource(),
    )

    @Serializable
    @SerialName("size")
    data object Size : DownloadSortBy(
        selector = { it.contentLength },
        icon = MyIcons.data,
        name = Res.string.size.asStringSource(),
    )
}
