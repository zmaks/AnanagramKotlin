package com.zheltoukhov.anangram.search.chain

import com.zheltoukhov.anangram.dto.Street

/**
 * Created by Maksim on 18.12.2017.
 */
class LettersSearch(val letters: String?): SearchChain() {
    override fun perform(streets: List<Street>): List<Street> {

        if (letters == null || letters.isEmpty())
            return streets

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
        return resStreets

    }

}