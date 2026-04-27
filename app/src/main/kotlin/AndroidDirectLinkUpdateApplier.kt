import com.neo.downloader.InstallableArch
import com.neo.downloader.updateapplier.BaseUpdateApplier
import com.neo.downloader.updateapplier.UpdateDownloader
import com.neo.downloader.updateapplier.UpdateInstaller
import com.neo.downloader.updateapplier.UpdatePreparer
import com.neo.downloader.updatechecker.UpdateInfo
import com.neo.downloader.updatechecker.UpdateSource

/**
 * this update applier works for direct downloads!
 */
class AndroidDirectLinkUpdateApplier(
    private val updateDownloader: UpdateDownloader,
) : BaseUpdateApplier() {
    override fun updateSupported(): Boolean {
        return true
    }

    override fun getUpdatePreparer(): UpdatePreparer {
        return updateDownloader
    }

    override fun getBestDownloadSource(updateInfo: UpdateInfo): UpdateSource {
        val downloadableSources =
            updateInfo.updateSource.filterIsInstance<UpdateSource.DirectDownloadLink>()
                .sortedBy {
                    // universal downloads have bigger size so we put them last
                    it.installableArch !is InstallableArch.Universal
                }
        val downloadSource = downloadableSources.find {
            isApk(it.name)
        }
        return requireNotNull(downloadSource) {
            "Can't find proper download link for your platform! Please update it manually"
        }
    }

    override fun getUpdateInstaller(preparedUpdate: UpdatePreparer.PreparedUpdate): UpdateInstaller {
        return ApkInstaller((preparedUpdate as UpdateDownloader.PreparedUpdateFile).file)
    }

    fun isApk(name: String): Boolean {
        return name.endsWith(".apk")
    }
}
