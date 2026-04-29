package com.neo.downloader.android.ytdlp

import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

object YtDlpManager {
    private const val TAG = "YtDlpManager"
    @Volatile
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            YoutubeDL.getInstance().init(context)
            initialized = true
        }
    }

    private fun addCookiesIfAvailable(request: YoutubeDLRequest, url: String) {
        runCatching {
            val cookieString = CookieManager.getInstance().getCookie(url) ?: return
            request.addOption("--add-header", "Cookie: $cookieString")
        }
    }

    suspend fun getFormats(url: String): Result<List<FormatOption>> = withContext(Dispatchers.IO) {
        runCatching {
            val request = YoutubeDLRequest(url).apply {
                addOption("--dump-single-json")
                addOption("--skip-download")
                addOption("--no-playlist")
                addOption("--quiet")
                addOption("--no-warnings")
            }
            addCookiesIfAvailable(request, url)
            val response = YoutubeDL.getInstance().execute(request)
            if (response.exitCode != 0) {
                throw IllegalStateException("yt-dlp failed: ${response.err}")
            }
            parseFormatsJson(response.out)
        }.onFailure { Log.e(TAG, "getFormats failed", it) }
    }

    suspend fun getDownloadUrl(url: String, formatId: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val request = YoutubeDLRequest(url).apply {
                addOption("-f", formatId)
                addOption("-g")
                addOption("--no-playlist")
                addOption("--quiet")
                addOption("--no-warnings")
            }
            addCookiesIfAvailable(request, url)
            val response = YoutubeDL.getInstance().execute(request)
            if (response.exitCode != 0) {
                throw IllegalStateException("yt-dlp failed: ${response.err}")
            }
            val directUrl = response.out
                .lineSequence()
                .map { it.trim() }
                .firstOrNull { it.startsWith("http") }
                ?: throw IllegalStateException("No direct stream URL returned")
            directUrl
        }.onFailure { Log.e(TAG, "getDownloadUrl failed", it) }
    }

    private fun parseFormatsJson(jsonStr: String): List<FormatOption> {
        val list = mutableListOf<FormatOption>()
        val root = JSONObject(jsonStr.trim())
        val formatsArray = root.optJSONArray("formats")
            ?: root.optJSONArray("requested_formats")
            ?: run {
                val entries = root.optJSONArray("entries")
                val firstEntry = entries?.optJSONObject(0)
                firstEntry?.optJSONArray("formats") ?: firstEntry?.optJSONArray("requested_formats")
            }
            ?: JSONArray()

        for (i in 0 until formatsArray.length()) {
            val obj = formatsArray.optJSONObject(i) ?: continue
            val id = obj.optString("format_id", "")
            if (id.isBlank()) continue

            val acodec = obj.optString("acodec", "")
            val vcodec = obj.optString("vcodec", "")
            val hasMedia = acodec != "none" || vcodec != "none"
            if (!hasMedia) continue

            val note = obj.optString("format_note", "")
            val ext = obj.optString("ext", "")
            val height = obj.optInt("height", 0)
            val filesize = obj.optLong("filesize", -1L)
            val filesizeApprox = obj.optLong("filesize_approx", -1L)
            val size = when {
                filesize > 0 -> formatSize(filesize)
                filesizeApprox > 0 -> formatSize(filesizeApprox)
                else -> null
            }

            val quality = when {
                height > 0 -> "${height}p"
                note.isNotBlank() -> note
                ext.isNotBlank() -> ext
                else -> "Auto"
            }

            list += FormatOption(
                id = id,
                quality = quality,
                size = size,
                bitrate = obj.optInt("tbr", 0).takeIf { it > 0 },
                fps = obj.optInt("fps", 0).takeIf { it > 0 },
                codec = listOf(vcodec.takeIf { it.isNotBlank() && it != "none" }, acodec.takeIf { it.isNotBlank() && it != "none" })
                    .filterNotNull()
                    .joinToString("+")
                    .takeIf { it.isNotBlank() },
            )
        }

        return list
            .distinctBy { it.id }
            .sortedByDescending { """\d+""".toRegex().find(it.quality)?.value?.toIntOrNull() ?: -1 }
    }

    private fun formatSize(bytes: Long): String {
        val kb = 1024.0
        val mb = kb * 1024
        val gb = mb * 1024
        return when {
            bytes >= gb -> String.format("%.1f GB", bytes / gb)
            bytes >= mb -> String.format("%.1f MB", bytes / mb)
            bytes >= kb -> String.format("%.0f KB", bytes / kb)
            else -> "$bytes B"
        }
    }
}

data class FormatOption(
    val id: String,
    val quality: String,
    val size: String? = null,
    val bitrate: Int? = null,
    val fps: Int? = null,
    val codec: String? = null,
)
