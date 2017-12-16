package com.zheltoukhov.anangram.search

import com.zheltoukhov.anangram.dto.Street

/**
 * Created by Maksim on 16.12.2017.
 */
class StreetSearcher {
    var streetPattern: String? = null
    var buildingPattern: String? = null


    fun streetPattern(pattern: String): StreetSearcher {
        streetPattern = pattern
        return this
    }

    fun buildingPattern(pattern: String): StreetSearcher {
        buildingPattern = pattern
        return this
    }

    fun search(): List<Street> {
        val inSearcher = InitialSearch
        inSearcher.next = LettersSearch(streetPattern)
        return inSearcher.go()
    }
}