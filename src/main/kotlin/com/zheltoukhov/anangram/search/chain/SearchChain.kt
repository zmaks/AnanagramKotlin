package com.zheltoukhov.anangram.search.chain

import com.zheltoukhov.anangram.dto.Street

/**
 * Created by Maksim on 16.12.2017.
 */
abstract class SearchChain {
    var next: SearchChain? = null

    fun go(streets: List<Street>): List<Street> {
        return next?.go(perform(streets)) ?: perform(streets)
    }

    abstract protected fun perform(streets: List<Street>): List<Street>

    protected fun highlight(name: String, searchLetters: MutableList<Char>): String {
        val letMass = name.toCharArray().toList()
        var resultStreet = ""
        for (streetChar in letMass) {
            var found = false
            val iter: MutableIterator<Char> = searchLetters.iterator()
            while (iter.hasNext()) {
                val s = iter.next()
                if (streetChar.equals(s, true)) {
                    resultStreet+="<b>$streetChar</b>"
                    iter.remove()
                    found = true
                    break
                }
            }
            if (!found) resultStreet+=streetChar
        }
        return resultStreet
    }
}
