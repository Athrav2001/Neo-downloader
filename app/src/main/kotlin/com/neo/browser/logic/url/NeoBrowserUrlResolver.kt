package com.neo.browser.logic.url

import android.util.Patterns
import android.webkit.URLUtil
import com.neo.browser.logic.search.NeoSearchEngineProvider
import java.util.Locale
import java.util.regex.Pattern

class NeoBrowserUrlResolver(
    private val searchEngineProvider: NeoSearchEngineProvider,
) {
    fun resolve(input: String): String {
        var inUrl = input.trim()
        val hasSpace = inUrl.contains(' ')
        val matcher = ACCEPTED_URI_SCHEMA.matcher(inUrl)
        if (matcher.matches()) {
            val scheme = matcher.group(1).orEmpty()
            val normalizedScheme = scheme.lowercase(Locale.getDefault())
            if (normalizedScheme != scheme) {
                inUrl = normalizedScheme + matcher.group(2).orEmpty()
            }
            if (hasSpace && Patterns.WEB_URL.matcher(inUrl).matches()) {
                inUrl = inUrl.replace(" ", URL_ENCODED_SPACE)
            }
            return inUrl
        }
        if (!hasSpace && Patterns.WEB_URL.matcher(inUrl).matches()) {
            return URLUtil.guessUrl(inUrl)
        }
        return searchEngineProvider.createSearchUrl(inUrl)
    }

    companion object {
        private val ACCEPTED_URI_SCHEMA = Pattern.compile(
            "(?i)((?:http|https|file)://|(?:inline|data|about|javascript):|(?:.*:.*@))(.*)"
        )
        private const val URL_ENCODED_SPACE = "%20"
    }
}
