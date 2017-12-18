package com.zheltoukhov.anangram.dto

/**
 * Created by Maksim on 16.12.2017.
 */
data class Street(val id: Long, val name: String, val value: String, var buildings: List<String> = ArrayList<String>())
data class StreetDto(val name: String, val buildings: List<String>)
data class Building(val streetId: Long, val value: String)
