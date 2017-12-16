package com.zheltoukhov.anangram

import com.zheltoukhov.anangram.db.DataProvider
import com.zheltoukhov.anangram.rest.RestController
import com.zheltoukhov.anangram.search.StreetSearcher

fun main(args: Array<String>) {
    println("Hello, World")
    /*DataProvider.findAllStreets()
            .forEach(::println)
    DataProvider.findStreetIdByBuildingPart("2", listOf(5,6,7,8,9,2,1,3))*/
    StreetSearcher().streetPattern("увв").search().forEach(::println)
    //RestController.start()
}

