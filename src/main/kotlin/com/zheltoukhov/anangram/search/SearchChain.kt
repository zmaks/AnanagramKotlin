package com.zheltoukhov.anangram.search

import com.zheltoukhov.anangram.db.DataProvider
import com.zheltoukhov.anangram.dto.Street

/**
 * Created by Maksim on 16.12.2017.
 */
abstract class SearchChain {
    var next: SearchChain? = null

    abstract fun perform(streets: List<Street>): List<Street>
}

object InitialSearch: SearchChain() {
    val streets: List<Street> = DataProvider.findAllStreets()
    fun go(): List<Street> {
        return perform(streets)
    }
    override fun perform(streets: List<Street>): List<Street> {
        return next?.perform(streets) ?: streets
    }

}

class LettersSearch(val letters: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {

        if (letters == null || letters.isEmpty())
            return next?.perform(streets) ?: streets

        val resStreets = ArrayList<Street>()

        for(street in streets) {
            val mass = street.name.split(" ")
            var suff = ""
            if (mass[mass.size-1].contains("Улица")||mass[mass.size-1].contains("Переулок"))
                suff = mass[mass.size-1]
            var searchPart: String = street.name.replace(suff,"").replace(Regex("\\(.+\\)"), "")
            val letMass = searchPart.toCharArray().map { c -> Character.toString(c) }.toList()

            var resultStreet = ""

            var searchLetters = letters.toCharArray().toMutableList()
            for (streetChar in letMass) {
                var found = false
                val iter: MutableIterator<Char> = searchLetters.iterator()
                while (iter.hasNext()) {
                    val s = iter.next().toString()
                    if (streetChar.equals(s, true)) {
                        resultStreet+="<b>$streetChar</b>"
                        iter.remove()
                        found = true
                        break
                    }
                }
                if (!found) resultStreet+=streetChar
            }

            if (searchLetters.isEmpty()) {
                //println(searchPart)
                resStreets.add(street.copy(name = resultStreet+suff))
            }
        }
        return next?.perform(resStreets) ?: resStreets

    }

}

class RegexSearch(val regex: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {
        if (regex!!.startsWith("[")) {

        } else {
            return next?.perform(streets) ?: streets
        }
        return next?.perform(streets) ?: streets
    }

}

class BuildingSearch(val building: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {
        if (building!=null && !building.isEmpty()) {

        } else {
            return next?.perform(streets) ?: streets
        }
        return next?.perform(streets) ?: streets
    }

}