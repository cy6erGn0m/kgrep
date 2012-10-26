package cg.kgrep

import java.util.regex.Pattern
import java.util.regex.Matcher
import java.util.ArrayList

class RegexpLineMatcher(val expression : Pattern, ctx : Context) : LineMatcher {

    override val context: Context = ctx
    private var lastMatcher : Matcher? = null
    private val PLACEHOLDER = Pattern.compile("\\$([0-9a-zA-Z]+)", if (ctx.caseInsensitive) {Pattern.CASE_INSENSITIVE} else {0} )

    override fun matches(line: CharSequence): Boolean {
        lastMatcher = expression.matcher(line)

        return lastMatcher!!.find()
    }

    override fun getMatchGroups(line: CharSequence): List<String> {
        val m = lastMatcher
        if (m == null) {
            throw IllegalStateException()
        }

        val result = ArrayList<String>(m.groupCount() + 1)
        for (var i in (0 .. m.groupCount())) {
            result.add(m.group(i)!!)
        }

        return result
    }
}
