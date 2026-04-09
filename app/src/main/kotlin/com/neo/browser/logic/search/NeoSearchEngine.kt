package com.neo.browser.logic.search

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

enum class NeoSearchEngine(
    val key: String,
    private val iconAssetPath: String,
    private val queryUrlTemplate: String,
) {
    CUSTOM(
        key = "custom",
        iconAssetPath = "file:///android_asset/neo.png",
        queryUrlTemplate = "{query}",
    ),
    GOOGLE(
        key = "google",
        iconAssetPath = "file:///android_asset/google.png",
        queryUrlTemplate = "https://www.google.com/search?client=neo&ie=UTF-8&oe=UTF-8&q={query}",
    ),
    DUCK_DUCK_GO(
        key = "duckduckgo",
        iconAssetPath = "file:///android_asset/duckduckgo.png",
        queryUrlTemplate = "https://duckduckgo.com/?t=neo&q={query}",
    ),
    BING(
        key = "bing",
        iconAssetPath = "file:///android_asset/bing.png",
        queryUrlTemplate = "https://www.bing.com/search?q={query}",
    ),
    BRAVE(
        key = "brave",
        iconAssetPath = "file:///android_asset/neo.png",
        queryUrlTemplate = "https://search.brave.com/search?q={query}",
    ),
    YAHOO(
        key = "yahoo",
        iconAssetPath = "file:///android_asset/yahoo.png",
        queryUrlTemplate = "https://search.yahoo.com/search?p={query}",
    ),
    ASK(
        key = "ask",
        iconAssetPath = "file:///android_asset/ask.png",
        queryUrlTemplate = "https://www.ask.com/web?qsrc=0&o=0&l=dir&qo=NeoBrowser&q={query}",
    ),
    START_PAGE(
        key = "startpage",
        iconAssetPath = "file:///android_asset/startpage.png",
        queryUrlTemplate = "https://www.startpage.com/do/search?language=english&cat=web&query={query}",
    );

    fun createSearchUrl(query: String, customUrl: String? = null): String {
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        return when (this) {
            CUSTOM -> {
                val template = customUrl?.takeIf { it.isNotBlank() } ?: "{query}"
                template.replace("{query}", encoded)
            }
            else -> queryUrlTemplate.replace("{query}", encoded)
        }
    }

    fun iconPath(): String = iconAssetPath
}

