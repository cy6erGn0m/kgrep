package cg.kgrep

import kotlin.test.assertEquals
import org.junit.Test as test

public class IndexOfTest {
    test fun simpleIndexOf(): Unit {
        assertEquals(3, "abc123def".toCharArray().indexOf("123".toCharArray()))
    }

    test fun indexOfNearBounds() {
        assertEquals(3, "abc123".toCharArray().indexOf("123".toCharArray()))
    }

    test fun indexOfNearBounds2() {
        assertEquals(0, "123".toCharArray().indexOf("123".toCharArray()))
    }

    test fun indexOfNearBoundsShouldNotMatch() {
        assertEquals(-1, "abc12".toCharArray().indexOf("123".toCharArray()))
    }

    test fun indexOfShouldNotMatch() {
        assertEquals(-1, "abc12z".toCharArray().indexOf("123".toCharArray()))
    }

    test fun findSingleCharacter() {
        assertEquals(1, "abc12z".toCharArray().indexOf("b".toCharArray()))
    }

    test fun findSingleCharacter2() {
        assertEquals(5, "abc12z".toCharArray().indexOf("z".toCharArray()))
    }

}
