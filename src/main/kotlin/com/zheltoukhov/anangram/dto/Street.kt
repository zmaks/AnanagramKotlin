package com.zheltoukhov.anangram.dto

/**
 * Created by Maksim on 16.12.2017.
 */
data class Street(val id: Long, val name: String, val value: String, val buildings: List<String> = ArrayList<String>())
data class Building(val streetId: Long, val value: String)