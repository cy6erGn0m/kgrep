package cg.kgrep

val PLACEHOLDER = "\\$([0-9a-zA-Z]+)".toRegex()

class OutputFormatter(val context : Context) {

    fun format(line : CharSequence, matcher : LineMatcher, out : Appendable) {
        var count = 0

        if (context.printFileNames) {
            out.append(context.currentFile)
            count++
        }

        if (context.printLineNumbers) {
            if (count > 0) {
                out.append(':')
            }
            out.append(context.currentLine.toString())
            count++
        }

        if (count > 0) {
            out.append(':')
        }

        val pattern = context.replacementPattern
        when {
            pattern != null -> applyReplacement(out, matcher.getMatchGroups(line), line, pattern)
            context.invert -> out.append(line)
            context.printOnlyMatched -> out.append(matcher.getMatchGroups(line)[0])
            else -> out.append(line)
        }
    }

    private fun applyReplacement(out : Appendable, groups : List<String>, line : CharSequence, replacementPattern: String) {
        var lastIndex = 0

        for (match in PLACEHOLDER.findAll(replacementPattern)) {
            val name = match.groups[1]?.value
            val replacement : CharSequence? = when (name) {
                null -> null
                "$" -> null
                "file" -> context.currentFile
                "line" -> context.currentLine.toString()
                "full" ->  line

                else -> {
                    if (name.matches("[0-9]+".toRegex())) {
                        groups[Integer.parseInt(name)]
                    } else {
                        null
                    }
                }
            }

            if (replacement != null) {
                out.append(replacementPattern, lastIndex, match.range.start)
                out.append(replacement)

                lastIndex = match.range.end + 1
            }
        }

        out.append(replacementPattern, lastIndex, replacementPattern.length)
    }
}