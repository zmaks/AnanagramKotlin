package com.zheltoukhov.anangram.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

/**
 * Created by Maksim on 16.12.2017.
 */
object DBConnection {

    private var conn: Connection? = null
    private const val USERNAME = "postgres"
    private const val PASSWORD = "121212"
    private val url = "jdbc:postgresql://localhost:5432/streets_db"

    init {
        val connectionProps = Properties()
        connectionProps.put("user", USERNAME)
        connectionProps.put("password", PASSWORD)
        try {
            Class.forName("org.postgresql.Driver").newInstance()
            conn = DriverManager.getConnection(url, connectionProps)
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }

    fun getConnection() : Connection? = conn

}