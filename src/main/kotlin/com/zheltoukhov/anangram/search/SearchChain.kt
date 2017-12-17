package com.zheltoukhov.anangram.search

import com.zheltoukhov.anangram.db.DataProvider
import com.zheltoukhov.anangram.dto.Street
import java.util.function.Predicate
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * Created by Maksim on 16.12.2017.
 */
abstract class SearchChain {
    var next: SearchChain? = null

    abstract fun perform(streets: List<Street>): List<Street>
}

class InitialSearch: SearchChain() {
    // streets: List<Street> = DataProvider.findAllStreets()

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
                resStreets.add(street.copy(name = resultStreet+suff))
            }
        }
        return next?.perform(resStreets) ?: resStreets

    }

}

class RegexSearch(val regex: String?, val options: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {
        if (regex == null || regex.isEmpty())
            return next?.perform(streets) ?: streets
        var finalRegexp: String = regex.toLowerCase().trim()
        if (options != null && !options.isEmpty()) {
            val optArr = options.split(",")

            for (o in optArr) {
                when (o) {
                    "start" -> finalRegexp="^"+finalRegexp
                    "end" -> finalRegexp+="$"
                    "part" -> finalRegexp=".*$finalRegexp.*"
                    "ord" -> {
                        val let = finalRegexp.toCharArray().toList()
                        finalRegexp=let.joinToString(".*")
                    }
                }
            }
        }

        val x: Predicate<String>
        try {
             x = Pattern.compile(finalRegexp).asPredicate()
        } catch (e: PatternSyntaxException) {
            return emptyList()
        }

        val res = streets.filter { s -> x.test(s.value) }.toList()
        return next?.perform(res) ?: res
    }

}

class BuildingSearch(val building: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {
        if (building == null || building.isEmpty())
            return next?.perform(streets) ?: streets

        val buildingsMap = DataProvider.findStreetIdByBuildingPart(building)
        for (s in streets) {
            s.buildings = buildingsMap.getOrDefault(s.id, emptyList())
        }
        val res = streets.filter { s -> !s.buildings.isEmpty() }.toList()
        return next?.perform(res) ?: res
    }
}

class LengthPatternSearch(val lengthPattern: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {
        if (lengthPattern == null || lengthPattern.isEmpty())
            return next?.perform(streets) ?: streets
        val min = Regex("from\\d+").find(lengthPattern)?.value?.replace("from","") ?: "-"
        val max = Regex("to\\d+").find(lengthPattern)?.value?.replace("to","") ?: "-"
        val minLength = try {Integer.parseInt(min)} catch (e: NumberFormatException) {-1}
        val maxLength = try {Integer.parseInt(max)} catch (e: NumberFormatException) {-1}

        var rule: Predicate<Int> = Predicate { _ -> true }
        if (minLength>0) rule = rule.and({ l ->minLength<=l })
        if (maxLength>0) rule = rule.and({ l ->maxLength>=l })
        val res = streets.filter{ s -> rule.test(s.value.length) }
        return next?.perform(res) ?: res
    }

}