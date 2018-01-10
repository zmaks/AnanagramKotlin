package com.zheltoukhov.anangram.rest

import com.google.gson.Gson
import com.zheltoukhov.anangram.dto.StreetDto
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
                                .streetPattern(req.queryParams("name"))
                                .buildingPattern(req.queryParams("building"))
                                .lengthPattern(req.queryParams("length"))
                                .options(req.queryParams("options"))
                                .search()
                                .map { s -> StreetDto(s.name, s.buildings) }
                )
            }

}

