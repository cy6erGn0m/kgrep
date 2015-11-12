package cg.kgrep

import kotlin.text.*

class RegexpLineMatcher(val expression : Regex, override val context : Context) : LineMatcher {

    private var lastMatch: MatchResult? = null

    init {
        assert(context.caseInsensitive == (RegexOption.IGNORE_CASE in expression.options))
    }

    override fun matches(line: CharSequence): Boolean =
        expression.find(line)?.let { matches ->
            lastMatch = matches
            true
        } ?: false

    override fun getMatchGroups(line: CharSequence): List<String> {
        val m = lastMatch ?: throw IllegalStateException()

        return m.groups.map { it?.value ?: "" }
    }
}
