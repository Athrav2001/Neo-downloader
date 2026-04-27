package ir.neo.util.compose.localizationmanager

import ir.neo.resources.contracts.MyLanguageResource

/**
 * at the moment we only use bundled strings
 */
class LanguageSourceProvider(
    val defaultLanguageResource: MyLanguageResource,
    val allLanguageResources: List<MyLanguageResource>,
)
