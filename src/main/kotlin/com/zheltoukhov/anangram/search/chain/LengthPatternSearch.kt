package com.zheltoukhov.anangram.search.chain

import com.zheltoukhov.anangram.dto.Street
import java.util.function.Predicate

/**
 * Created by Maksim on 18.12.2017.
 */

class LengthPatternSearch(val lengthPattern: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {
        if (lengthPattern == null || lengthPattern.isEmpty())
            return streets
        val min = Regex("from\\d+").find(lengthPattern)?.value?.replace("from","") ?: "-"
        val max = Regex("to\\d+").find(lengthPattern)?.value?.replace("to","") ?: "-"
        val minLength = try {Integer.parseInt(min)} catch (e: NumberFormatException) {-1}
        val maxLength = try {Integer.parseInt(max)} catch (e: NumberFormatException) {-1}

        var rule: Predicate<Int> = Predicate { _ -> true }
        if (minLength>0) rule = rule.and({ l ->minLength<=l })
        if (maxLength>0) rule = rule.and({ l ->maxLength>=l })

        return streets.filter{ s -> rule.test(s.value.length) }
    }

}