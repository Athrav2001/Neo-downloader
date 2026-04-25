package ir.neo.util.osfileutil

import ir.neo.util.platform.Platform
import ir.neo.util.platform.asDesktop

actual fun getPlatformFileUtil(): FileUtils {
    return when (Platform.asDesktop()) {
        Platform.Desktop.Windows -> WindowsFileUtils()
        Platform.Desktop.Linux -> LinuxFileUtils()
        Platform.Desktop.MacOS -> MacOsFileUtils()
    }
}
