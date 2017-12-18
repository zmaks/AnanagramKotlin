package com.zheltoukhov.anangram.search.chain

import com.zheltoukhov.anangram.db.DataProvider
import com.zheltoukhov.anangram.dto.Street
import java.util.function.Predicate
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * Created by Maksim on 16.12.2017.
 */
abstract class SearchChain {
    var next: SearchChain? = null

    fun go(streets: List<Street>): List<Street> {
        return next?.perform(streets) ?: streets
    }
    abstract protected fun perform(streets: List<Street>): List<Street>
}

class InitialSearch: SearchChain() {
    // streets: List<Street> = DataProvider.findAllStreets()

    override fun perform(streets: List<Street>): List<Street> {
        return streets
    }

}