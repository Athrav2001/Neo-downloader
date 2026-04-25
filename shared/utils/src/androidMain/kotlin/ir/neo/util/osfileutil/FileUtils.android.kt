package ir.neo.util.osfileutil

actual fun getPlatformFileUtil(): FileUtils {
    return AndroidFileUtil()
}
