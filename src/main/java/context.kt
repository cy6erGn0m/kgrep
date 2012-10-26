package cg.kgrep

class Context(var caseInsensitive : Boolean = false, var printOnlyMatched : Boolean = false) {
    var currentFile : String = "(stdin)"
    var currentLineProvider : () -> Int = {-1}

    var currentLine : Int = -1
        get() = currentLineProvider()

    var invert = false
    var replacementPattern : String? = null
    var inputs : MutableList<String> = arrayList<String>()
    var pattern : String? = null
    var regexp = false
    var recursive = false

    var printLineNumbers = false
    var printFileNames = false
}
