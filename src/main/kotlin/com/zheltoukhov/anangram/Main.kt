package com.zheltoukhov.anangram

import com.zheltoukhov.anangram.db.DataProvider
import com.zheltoukhov.anangram.rest.RestController
import com.zheltoukhov.anangram.search.StreetSearcher

fun main(args: Array<String>) {
    println("с".equals("c", true))
    println("с".equals("C", false))
    println("r".equals("R", true))
    RestController.start()
}

