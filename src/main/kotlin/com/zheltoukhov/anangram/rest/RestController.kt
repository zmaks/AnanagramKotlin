package com.zheltoukhov.anangram.rest

import spark.Spark.get

/**
 * Created by Maksim on 16.12.2017.
 */
object  RestController {

    fun start() =
            get("/hello") { req, res -> "Hello World" }

}

