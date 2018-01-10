package com.zheltoukhov.anangram.search.chain

import com.zheltoukhov.anangram.dto.Street
import java.util.function.Predicate
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * Created by Maksim on 18.12.2017.
 */
class RegexSearch(val regex: String?, val options: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {

        if (regex == null || regex.isEmpty())
            return streets
        var finalRegexp: String = regex.toLowerCase().trim()
        if (options != null && !options.isEmpty()) {
            when (options) {
                "start" -> finalRegexp="^"+finalRegexp
                "end" -> finalRegexp+="$"
                "part" -> finalRegexp=".*$finalRegexp.*"
                "order" -> {
                    val let = finalRegexp.toCharArray().toList()
                    finalRegexp=let.joinToString(".*")
                }
            }
        }

        val x: Predicate<String> = try {
            Pattern.compile(finalRegexp).asPredicate()
        } catch (e: PatternSyntaxException) {
            return emptyList()
        }

        return streets.filter { s -> x.test(s.value) }.map { s -> s.copy(name = highlight(s.name, regex.toLowerCase(), options!!)) }

    }

    fun highlight(name: String, part: String, option: String) =
        when (option) {
            "start" -> replaceStart(name, part)
            "end" -> replaceEnd(name, part)
            "part" -> replacePart(name, part)
            "order" -> highlight(name, part.toMutableList(), true)
            else -> name
        }

    private fun replaceStart(name: String, part: String): String {
        val newPart = part.first().toUpperCase()+part.substring(1)
        return name.replaceFirst(newPart, "<b>$newPart</b>")
    }

    private fun replaceEnd(name: String, part: String) = name.replaceFirst(part,"<b>$part</b>")

    private fun replacePart(name: String, part: String) =
            if (isStartWith(name, part))
                replaceStart(name,part)
            else
                replaceEnd(name,part)

    private fun isStartWith(name: String, part: String): Boolean {
        var res = true
        for (s in name.toLowerCase().split(" ")) {
            res = res.and(s.startsWith(part.toLowerCase()))
        }
        return res
    }

}
