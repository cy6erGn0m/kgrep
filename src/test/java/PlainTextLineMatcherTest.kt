package cg.kgrep

import kotlin.test.*
import org.junit.Test as test

class PlainTextLineMatcherTest {

    @test fun testMatchCSAllLine() {
        testMatch("aaa123bbb", "aaa123bbb", PlainTextMatcher(Context(), "aaa"))
    }

    @test fun testMatchCSMatchedOnly() {
        testMatch("aaa123bbb", "aaa", PlainTextMatcher(Context(false, true), "aaa"))
    }

    @test fun testNotMatch() {
        testNotMatch("zzzz", PlainTextMatcher(Context(), "ZZ"))
    }

    @test fun testMatchCIAllLine() {
        testMatch("aaa123bbb", "aaa123bbb", CaseInsensitivePlainTextMatcher(Context(), "AAA"))
    }

    @test fun testMatchCIMatchedOnly() {
        testMatch("aaa123bbb", "aaa", CaseInsensitivePlainTextMatcher(Context(true, true), "AAA"))
    }

    private fun testNotMatch(line : CharSequence, matcher : LineMatcher) {
        assertFalse(matcher.matches(line))
    }

    private fun testMatch(line : CharSequence, expected : String, matcher : LineMatcher) {
        assertTrue(matcher.matches(line))

        var sb = StringBuilder()
//        matcher.formatMatch(line, sb)

        if (matcher.context.printOnlyMatched) {
            sb.append(matcher.getMatchGroups(line)[0])
        } else {
            sb.append(line)
        }

        assertEquals(expected, sb.toString())
    }


}