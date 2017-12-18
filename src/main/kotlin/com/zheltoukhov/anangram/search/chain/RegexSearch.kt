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

        val x: Predicate<String>
        try {
            x = Pattern.compile(finalRegexp).asPredicate()
        } catch (e: PatternSyntaxException) {
            return emptyList()
        }

        return streets.filter { s -> x.test(s.value) }.map { s -> s.copy(name = highlight(s.name, regex, options!!)) }

    }

    fun highlight(name: String, part: String, option: String): String {
        var res: String = name
        when (option) {
            "start" -> res = replaceStart(res, part)
            "end" -> res = replaceEnd(res, part)
            "part" -> res = if (isStartWith(name, part)) replaceStart(name,part) else replaceEnd(name,part)
            "order" -> res = highlight(res, part.toMutableList())
        }

        return res
    }

    private fun replaceStart(name: String, part: String): String {
        val newPart = part.first().toUpperCase()+part.substring(1)
        return name.replace(newPart, "<b>$newPart</b>")
    }

    private fun replaceEnd(name: String, part: String) = name.replace(part,"<b>$part</b>")

    private fun isStartWith(name: String, part: String): Boolean {
        var res = true
        for (s in name.toLowerCase().split(" ")) {
            res = res.and(s.startsWith(part.toLowerCase()))
        }
        return res
    }

}
