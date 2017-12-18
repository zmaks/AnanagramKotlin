package com.zheltoukhov.anangram.search.chain

import com.zheltoukhov.anangram.db.DataProvider
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
            val optArr = options.split(",")

            for (o in optArr) {
                when (o) {
                    "start" -> finalRegexp="^"+finalRegexp
                    "end" -> finalRegexp+="$"
                    "part" -> finalRegexp=".*$finalRegexp.*"
                    "ord" -> {
                        val let = finalRegexp.toCharArray().toList()
                        finalRegexp=let.joinToString(".*")
                    }
                }
            }
        }

        val x: Predicate<String>
        try {
            x = Pattern.compile(finalRegexp).asPredicate()
        } catch (e: PatternSyntaxException) {
            return emptyList()
        }

        return streets.filter { s -> x.test(s.value) }.toList()
    }

}
