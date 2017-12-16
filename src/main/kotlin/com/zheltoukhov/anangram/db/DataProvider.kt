package com.zheltoukhov.anangram.db

import com.zheltoukhov.anangram.dto.Building
import com.zheltoukhov.anangram.dto.Street
import java.sql.Connection

/**
 * Created by Maksim on 16.12.2017.
 */
object DataProvider {

    fun findAllStreets(): List<Street> {
        val conn = DBConnection.getConnection()

        val statement = conn!!.createStatement()
        val resultset = statement.executeQuery("select * from street")

        val streets = ArrayList<Street>();
        while (resultset!!.next()) {
            streets.add(
                    Street(resultset.getLong(1), resultset.getString(3), resultset.getString(4))
            )
        }

        return streets
    }

    fun findStreetIdByBuildingPart(part: String,streetIds: List<Long>?): List<Building> {
        val conn = DBConnection.getConnection()

        var query = "select street_id, value from building where value like ?"
        if (streetIds != null && !streetIds.isEmpty()) {
            query += " and street_id in (${streetIds.joinToString(",")})"
        }
        val statement = conn!!.prepareStatement(query)
        statement.setString(1,"%$part%")
        val resultSet = statement.executeQuery()

        val buildings = ArrayList<Building>()

        while (resultSet!!.next()) {
            buildings.add(
                    Building(resultSet.getLong(1), resultSet.getString(2))
            )
           // println("${resultSet.getString(2)} - ${resultSet.getLong(1)}")
        }

        return buildings
    }
}