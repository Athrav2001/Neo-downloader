package com.neo.downloader.downloaditem.hls

import java.util.concurrent.TimeUnit

internal actual object HlsRemuxBridge {
    actual fun remuxToMp4(inputPath: String, outputPath: String, timeoutMinutes: Long): Boolean {
        val process = ProcessBuilder(
            "ffmpeg",
            "-y",
            "-i", inputPath,
            "-c", "copy",
            "-movflags", "+faststart",
            outputPath
        )
            .redirectErrorStream(true)
            .start()
        val finished = process.waitFor(timeoutMinutes, TimeUnit.MINUTES)
        if (!finished) {
            process.destroyForcibly()
            return false
        }
        return process.exitValue() == 0
    }
}

