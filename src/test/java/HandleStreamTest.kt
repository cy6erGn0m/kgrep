package cg.kgrep

import org.junit.*
import kotlin.test.*

class HandleStreamTest {
    val ctx = Context()

    @Test
    fun testSimple() {
        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "b zzz", "zzz cc", "zzz"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testInvertedNoPattern() {
        ctx.invert = true

        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "aaa"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testPattern() {
        ctx.replacementPattern = "\$line: \$full"
        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "2: b zzz", "3: zzz cc", "4: zzz"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testInvertedWithPattern() {
        ctx.replacementPattern = "\$line: \$full"
        ctx.invert = true

        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "1: aaa"
        ), doHandleStream(matcher, text))
    }


    @Test
    fun testSimpleOnlyMatched() {
        ctx.printOnlyMatched = true
        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "zzz", "zzz", "zzz"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testInvertedOnlyMatched() {
        ctx.printOnlyMatched = true
        ctx.invert = true

        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "aaa"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testSimpleOnlyMatchedWithPatternSpecified() {
        // pattern should be applied
        ctx.printOnlyMatched = true
        ctx.replacementPattern = "\$full"
        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "b zzz", "zzz cc", "zzz"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testPrintLineNumbers() {
        ctx.printLineNumbers = true

        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "2:b zzz", "3:zzz cc", "4:zzz"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testPrintFileName() {
        ctx.printFileNames = true

        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "(stdin):b zzz", "(stdin):zzz cc", "(stdin):zzz"
        ), doHandleStream(matcher, text))
    }

    @Test
    fun testPrintLineNumbersAndFileNames() {
        ctx.printLineNumbers = true
        ctx.printFileNames = true

        val matcher = PlainTextMatcher(ctx, "zzz")
        val text = """
        aaa
        b zzz
        zzz cc
        zzz
        """.trimIndent()

        assertEquals(listOf(
                "(stdin):2:b zzz", "(stdin):3:zzz cc", "(stdin):4:zzz"
        ), doHandleStream(matcher, text))
    }

    private fun doHandleStream(matcher: LineMatcher, text: String): List<String> {
        val out = StringBuilder()
        handleStream(ctx, matcher, text.reader().buffered(), out)
        return out.toString().lines().let { if (it.lastOrNull() == "") it.dropLast(1) else it }
    }
}