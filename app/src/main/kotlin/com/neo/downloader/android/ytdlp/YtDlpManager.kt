package com.neo.downloader.android.ytdlp

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import android.webkit.CookieManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object YtDlpManager {
    private const val TAG = "YtDlpManager"
    private var initialized = false
    private lateinit var ytDlpPath: File
    private lateinit var pythonPath: String
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun init(context: Context) {
        if (initialized) return
        val baseDir = File(context.noBackupFilesDir, "ytdlp")
        baseDir.mkdirs()
        ytDlpPath = File(baseDir, "yt-dlp")
        // Copy yt-dlp from assets if not present
        if (!ytDlpPath.exists()) {
            copyYtDlpFromAssets(context)
        }
        // Set executable permission
        if (ytDlpPath.exists()) {
            ytDlpPath.setExecutable(true)
        }
        // Find Python interpreter
        pythonPath = findPythonPath()
        initialized = true
    }

    private fun copyYtDlpFromAssets(context: Context) {
        try {
            val assetManager = context.assets
            val input = assetManager.open("yt-dlp")
            val output = FileOutputStream(ytDlpPath)
            input.copyTo(output)
            output.close()
            input.close()
            Log.d(TAG, "yt-dlp copied from assets successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error copying yt-dlp from assets", e)
        }
    }

    private fun findPythonPath(): String {
        // Try to find python3 in PATH
        val runtime = Runtime.getRuntime()
        return try {
            val process = runtime.exec(arrayOf("which", "python3"))
            val reader = process.inputStream.bufferedReader()
            val path = reader.readLine()?.trim()
            reader.close()
            if (!path.isNullOrBlank()) path else "python3"
        } catch (e: Exception) {
            "python3"
        }
    }

    private fun addCookiesIfAvailable(request: YTDLRequest, url: String) {
        val cookieManager = CookieManager.getInstance()
        val cookieString = cookieManager.getCookie(url) ?: return
        request.addOption("--add-header", "Cookie: $cookieString")
    }

    suspend fun getFormats(url: String): Result<List<FormatOption>> = withContext(Dispatchers.IO) {
        try {
            val request = YTDLRequest(url).apply {
                addOption("--print", "%(formats)s")
                addOption("--skip-download")
                addOption("--quiet")
                addOption("--ignore-errors")
                addOption("--no-warnings")
            }
            addCookiesIfAvailable(request, url)
            val response = execute(request)
            if (response.exitCode != 0) {
                return@withContext Result.failure(Exception("yt-dlp exited with code ${response.exitCode}: ${response.err}"))
            }
            val formats = parseFormatsJson(response.out)
            Result.success(formats)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting formats", e)
            Result.failure(e)
        }
    }

    private fun parseFormatsJson(jsonStr: String): List<FormatOption> {
        val list = mutableListOf<FormatOption>()
        try {
            val arr = JSONArray(jsonStr.trim())
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val id = obj.optString("format_id", "")
                val note = obj.optString("format_note", "")
                val height = obj.optInt("height", 0)
                val filesize = obj.optLong("filesize", -1L)
                val filesizeApprox = obj.optLong("filesize_approx", -1L)
                val size = when {
                    filesize > 0 -> formatSize(filesize)
                    filesizeApprox > 0 -> formatSize(filesizeApprox)
                    else -> null
                }
                val quality = if (note.isNotEmpty()) note else if (height > 0) "${height}p" else "Unknown"
                list.add(FormatOption(id, quality, size, null, null, null))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse formats JSON", e)
        }
        return list.sortedByDescending { it.quality }.distinctBy { it.quality }
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

    suspend fun getDownloadUrl(url: String, formatId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = YTDLRequest(url).apply {
                addOption("-f", formatId)
                addOption("-g") // get direct URL
                addOption("--skip-download")
                addCookiesIfAvailable(this, url)
            }
            val response = execute(request)
            if (response.exitCode != 0) {
                return@withContext Result.failure(Exception("yt-dlp exited with code ${response.exitCode}: ${response.err}"))
            }
            val directUrl = response.out.trim()
            if (directUrl.isNotEmpty()) {
                Result.success(directUrl)
            } else {
                Result.failure(IOException("Empty URL"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting download URL", e)
            Result.failure(e)
        }
    }

    suspend fun downloadVideo(url: String, formatId: String, outputPath: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = YTDLRequest(url).apply {
                addOption("-f", formatId)
                addOption("-o", outputPath)
                addCookiesIfAvailable(this, url)
                // Add any other options like --no-playlist, etc.
            }
            val response = execute(request)
            if (response.exitCode != 0) {
                return@withContext Result.failure(Exception("yt-dlp exited with code ${response.exitCode}: ${response.err}"))
            }
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading video", e)
            Result.failure(e)
        }
    }

    private suspend fun execute(request: YTDLRequest): ExecutionResult = withContext(Dispatchers.IO) {
        val command = mutableListOf(pythonPath, ytDlpPath.absolutePath)
        command.addAll(request.buildCommand())
        Log.d(TAG, "Executing: ${command.joinToString(" ")}")
        val processBuilder = ProcessBuilder(command)
        // Set environment variables if needed (like ytdlnis-src)
        val process = processBuilder.start()
        val out = process.inputStream.bufferedReader().use { it.readText() }
        val err = process.errorStream.bufferedReader().use { it.readText() }
        val exitCode = process.waitFor()
        ExecutionResult(out, err, exitCode)
    }

    data class ExecutionResult(val out: String, val err: String, val exitCode: Int)
}

class YTDLRequest(private val url: String) {
    private val options = mutableListOf<String>()

    fun addOption(key: String, value: String? = null) {
        options.add(key)
        if (value != null) {
            options.add(value)
        }
    }

    fun buildCommand(): List<String> {
        val cmd = mutableListOf<String>()
        if (url.isNotEmpty()) {
            cmd.add(url)
        }
        cmd.addAll(options)
        return cmd
    }

    val id: String get() = url.hashCode().toString()
}

data class FormatOption(
    val id: String,
    val quality: String,
    val size: String? = null,
    val bitrate: Int? = null,
    val fps: Int? = null,
    val codec: String? = null,
)
