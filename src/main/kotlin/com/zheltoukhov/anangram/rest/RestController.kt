package com.zheltoukhov.anangram.rest

import com.google.gson.Gson
import com.zheltoukhov.anangram.search.StreetSearcher
import spark.Spark.get

/**
 * Created by Maksim on 16.12.2017.
 */
object  RestController {

    fun start() =
            get("/api") { req, res ->
                res.type("application/json")
                Gson().toJson(
                        StreetSearcher()
                                .streetPattern(req.queryParams("name") ?: null)
                                .buildingPattern(req.queryParams("building") ?: null)
                                .lengthPattern(req.queryParams("length") ?: null)
                                .options(req.queryParams("options") ?: null)
                                .search()
                )
            }

}

