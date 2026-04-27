import com.neo.downloader.updateapplier.UpdateInstaller
import ir.neo.util.osfileutil.FileUtils
import java.io.File

class ApkInstaller(
    private val apkFile: File,
) : UpdateInstaller {
    override fun installUpdate() {
        FileUtils.openFile(apkFile)
    }
}
