package com.zheltoukhov.anangram.search

import com.zheltoukhov.anangram.db.DataProvider
import com.zheltoukhov.anangram.dto.Street
import com.zheltoukhov.anangram.search.chain.*

/**
 * Created by Maksim on 16.12.2017.
 */
class StreetSearcher {
    private var streetPattern: String? = null
    private var buildingPattern: String? = null
    private var lengthPattern: String? = null
    private var options: String? = null


    fun streetPattern(pattern: String?): StreetSearcher {
        streetPattern = pattern
        return this
    }

    fun buildingPattern(pattern: String?): StreetSearcher {
        buildingPattern = pattern
        return this
    }

    fun lengthPattern(pattern: String?): StreetSearcher {
        lengthPattern = pattern
        return this
    }

    fun options(optionsString: String?): StreetSearcher {
        options = optionsString
        return this
    }

    fun search(): List<Street> {
        if (emptyParams(streetPattern, buildingPattern, options, lengthPattern)) return emptyList()
        val inSearcher =
            if (streetPattern?.toLowerCase()?.matches(Regex("^[а-я]+-?[а-я]*\$"))?:false&&emptyParams(options))
                LettersSearch(streetPattern)
            else
                RegexSearch(streetPattern, options)
        val buildSearch = BuildingSearch(buildingPattern)
        val lengthSearch = LengthPatternSearch(lengthPattern)

        buildSearch.next = lengthSearch
        inSearcher.next = buildSearch
        return inSearcher
                .go(DataProvider.findAllStreets())
    }

    fun emptyParams(vararg params: String?): Boolean {
        var res = true
        for(s in params) {
            res = res.and(s==null || s.isEmpty())
        }
        return res
    }
}