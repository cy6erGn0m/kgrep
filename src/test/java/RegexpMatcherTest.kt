package cg.kgrep

import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import java.util.regex.Pattern

class RegexpMatcherTest {

    test fun simpleTest() {
        testMatch("aaa124zzz", "aaa124zzz", RegexpLineMatcher(Pattern.compile("[0-9]+"), Context()))
    }

    test fun simpleTest2() {
        testMatch("aaa124zzz", "124", RegexpLineMatcher(Pattern.compile("[0-9]+"), Context(false, true)))
    }

    test fun replacementTest1() {
        val ctx = Context()
        ctx.replacementPattern = "$1"

        testMatch("aaa124zzz", "zzz", RegexpLineMatcher(Pattern.compile("[0-9]+([a-z]+)"), ctx))
    }

    test fun replacementTest2() {
        val ctx = Context()
        ctx.replacementPattern = "$1 \$file \$line $$ \$strange"

        testMatch("aaa124zzz", "zzz (stdin) -1 $$ \$strange", RegexpLineMatcher(Pattern.compile("[0-9]+([a-z]+)"), ctx))
    }

    fun testNotMatch(line : CharSequence, matcher : LineMatcher) {
        assertFalse(matcher.matches(line))
    }

    fun testMatch(line : CharSequence, expected : String, matcher : LineMatcher) {
        assertTrue(matcher.matches(line))

        var sb = StringBuilder()
        OutputFormatter(matcher.context).format(line, matcher, sb)

        assertEquals(expected, sb.toString())
    }
}