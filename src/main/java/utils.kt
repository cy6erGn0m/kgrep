package cg.kgrep

import java.util.regex.Matcher

fun CharArray.toLowerCase(): CharArray {
    val rs = CharArray(this.size)

    for (val idx in this.indices) {
        rs[idx] = Character.toLowerCase(this[idx])
    }

    return rs
}

fun CharSequence.toLowerCase() : CharArray {
    val rs = CharArray(this.size)

    for (val idx in this.length().indices) {
        rs[idx] = Character.toLowerCase(this[idx])
    }

    return rs
}

fun CharArray.indexOf(subString : CharArray) : Int {
    if (subString.size == 0) {
        return 0
    }

    val targetSize = subString.size - 1
    val m = this.size - targetSize
    val firstChar = subString[0]

    if (m == 0) {
        return -1
    }

    @main_loop
    for (var i in (0 .. m - 1)) {
        if (this[i] == firstChar) {
            for (var j in IntRange(i + 1, targetSize)) {
                if (this[j] != subString[j - i]) {
                    continue @main_loop
                }
            }
            return i
        }
    }

    return -1
}
