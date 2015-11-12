package cg.kgrep

import java.util.*

interface LineMatcher {
    val context : Context
    fun matches(line : CharSequence) : Boolean

    fun getMatchGroups(line : CharSequence) : List<String>
}

class PlainTextMatcher(ctx : Context, val text : String) : LineMatcher {

    override val context = ctx

    override fun matches(line: CharSequence) = line.toString().contains(text)

    override fun getMatchGroups(line: CharSequence): List<String> {
        return Collections.singletonList(text)
    }
}

class CaseInsensitivePlainTextMatcher(ctx : Context, text : String) : LineMatcher {

    override val context: Context = ctx
    private val text: CharArray = text.toLowerCase().toCharArray()
    private var lastIndexOf = -1

    override fun matches(line: CharSequence): Boolean {
        lastIndexOf = line.toLowerCase().indexOf(text)
        return lastIndexOf != -1
    }

    override fun getMatchGroups(line: CharSequence): List<String> {
        if (lastIndexOf == -1) {
            throw IllegalStateException()
        }

        return Collections.singletonList(line.subSequence(lastIndexOf, lastIndexOf + text.size).toString())
    }
}