package cg.kgrep

import java.util.regex.Matcher

fun CharArray.toLowerCase(): CharArray {
    val rs = CharArray(this.size())

    for (idx in indices) {
        rs[idx] = Character.toLowerCase(this[idx])
    }

    return rs
}

fun CharSequence.toLowerCase() : CharArray {
    val rs = CharArray(length())

    for (idx in 0..length() - 1) {
        rs[idx] = Character.toLowerCase(this[idx])
    }

    return rs
}

fun CharArray.indexOf(subString : CharArray) : Int {
    if (subString.isEmpty()) {
        return 0
    }

    val firstChar = subString[0]
    val subStringLastIndex = subString.lastIndex

    mainLoop@
    for (i in 0..size() - subString.size()) {
        if (this[i] == firstChar) {
            for (j in 1 .. subStringLastIndex) {
                if (this[i + j] != subString[j]) {
                    continue@mainLoop
                }
            }
            return i
        }
    }

    return -1
}
