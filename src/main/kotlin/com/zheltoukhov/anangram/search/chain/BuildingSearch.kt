package com.zheltoukhov.anangram.search.chain

import com.zheltoukhov.anangram.db.DataProvider
import com.zheltoukhov.anangram.dto.Street

/**
 * Created by Maksim on 18.12.2017.
 */

class BuildingSearch(val building: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {
        if (building == null || building.isEmpty())
            return streets

        val buildingsMap = DataProvider.findStreetIdByBuildingPart(building)
        for (s in streets) {
            s.buildings = buildingsMap.getOrDefault(s.id, emptyList())
        }
        return streets.filter { s -> !s.buildings.isEmpty() }
    }
}