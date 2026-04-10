package ir.amirab.downloader.downloaditem.hls

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode

internal actual object HlsRemuxBridge {
    actual fun remuxToMp4(inputPath: String, outputPath: String, timeoutMinutes: Long): Boolean {
        val command = buildString {
            append("-y -i ")
            append(quote(inputPath))
            append(" -c copy -movflags +faststart ")
            append(quote(outputPath))
        }
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }

    private fun quote(path: String): String {
        return "'${path.replace("'", "'\\''")}'"
    }
}

