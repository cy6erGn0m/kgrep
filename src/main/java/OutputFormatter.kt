package cg.kgrep

import java.util.regex.Pattern
import java.util.regex.Matcher

val PLACEHOLDER = Pattern.compile("\\$([0-9a-zA-Z]+)")

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

        if (context.invert) {
//            out.append(line)
            applyReplacement(out, listOf(line.toString()), line)
        } else if (context.replacementPattern != null) {
            applyReplacement(out, matcher.getMatchGroups(line), line)
        } else if (context.printOnlyMatched) {
            out.append(matcher.getMatchGroups(line)[0])
        } else {
            out.append(line)
        }
    }

    private fun applyReplacement(out : Appendable, groups : List<String>, line : CharSequence) {
        val replacementPattern = context.replacementPattern!!

        val patternMatcher = PLACEHOLDER.matcher(replacementPattern)
        var lastIndex = 0

        while (patternMatcher.find()) {
            val name = patternMatcher.group(1)!!
            val replacement : CharSequence? = when (name) {
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
                out.append(replacementPattern, lastIndex, patternMatcher.start())
                out.append(replacement)

                lastIndex = patternMatcher.end()
            }
        }

        out.append(replacementPattern, lastIndex, replacementPattern.length())
    }
}