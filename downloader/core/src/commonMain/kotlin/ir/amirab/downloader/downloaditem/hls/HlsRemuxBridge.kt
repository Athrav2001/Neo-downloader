package ir.amirab.downloader.downloaditem.hls

internal expect object HlsRemuxBridge {
    fun remuxToMp4(inputPath: String, outputPath: String, timeoutMinutes: Long): Boolean
}

