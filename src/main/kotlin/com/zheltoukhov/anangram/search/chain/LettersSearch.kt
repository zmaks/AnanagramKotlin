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
            val suff = if (mass[mass.size-1].contains("Улица")||mass[mass.size-1].contains("Переулок"))
                mass[mass.size-1] else ""
            val searchPart = street.name.replace(suff,"").replace(Regex("\\(.+\\)"), "")

            val searchLetters = letters.toMutableList()
            val resultStreet = highlight(searchPart, searchLetters, false)

            if (searchLetters.isEmpty()) {
                resStreets.add(street.copy(name = resultStreet+suff))
            }
        }
        return resStreets

    }

}