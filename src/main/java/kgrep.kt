package cg.kgrep

import java.io.File
import kotlin.io.forEachLine
import java.util.regex.Pattern
import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val it = args.iterator()
    var stopOptionsProcessing = false
    var forcePrintHelp = false
    var forcePrintVersion = false

    val context = Context()

    while (it.hasNext()) {
        val arg = it.next()

        if (!stopOptionsProcessing && arg.startsWith("-")) {
            if (arg.startsWith("--") && (arg.length() > 2)) {
                when (arg) {
                    "--help" -> forcePrintHelp = true
                    "--version" -> forcePrintVersion = true
                    else -> throw BadArgException("Unknown option ${arg}")
                }
            } else {
                arg.substring(1).toCharArray().forEach { ch ->
                    when (ch) {
                        '-' -> stopOptionsProcessing = true
                        'i', 'y' -> context.caseInsensitive = true
                        'o' -> context.printOnlyMatched = true
                        'v' -> context.invert = true
                        'V' -> forcePrintVersion = true
                        'e' -> context.regexp = true
                        'r' -> {context.recursive = true; context.printFileNames = true}
                        'n' -> context.printLineNumbers = true
                        'h' -> context.printFileNames = true
                        'p' -> {
                            if (it.hasNext()) {
                                context.replacementPattern = it.next()
                            } else {
                                throw BadArgException("option -p requires argument")
                            }
                        }

                        else -> throw BadArgException("Unknown option -${ch}")
                    }
                }
            }
        } else {
            if (context.pattern == null) {
                context.pattern = arg
            } else {
                context.inputs.add(arg)
            }
        }

    }

    if (context.pattern == null || forcePrintHelp) {
        printUsage()
    } else if (forcePrintVersion) {
        println("Version is 0.0.0")
    } else {
        if (context.inputs.isEmpty()) {
            context.inputs.add("-")
        }

        val matcher : LineMatcher = if (context.regexp) {
            RegexpLineMatcher(Pattern.compile(context.pattern!!), context)
        } else if (context.caseInsensitive) {
            CaseInsensitivePlainTextMatcher(context, context.pattern!!)
        } else {
            PlainTextMatcher(context, context.pattern!!)
        }

        handle(context, matcher)
    }
}

fun handle(ctx : Context, matcher : LineMatcher) {
    for (x in ctx.inputs) {
        handleFile(ctx, matcher, x)
    }
}

var stdinProcessed = false

fun handleFile(ctx : Context, matcher : LineMatcher, input : String) {
    if (input == "-") {
        if (!stdinProcessed) {
            ctx.currentFile = "(stdin)"
            handleStream(ctx, matcher, ctx.invert, BufferedReader(InputStreamReader(System.`in`)))
            stdinProcessed = true
        }
    } else {
        val f = File(input)
        if (f.isDirectory()) {
            f.listFiles()?.forEach {
                handleFile(ctx, matcher, it.getPath())
            }
        } else if (f.exists() && f.isFile()) {
            f.reader().buffered(65536).use {
                ctx.currentFile = f.path
                handleStream(ctx, matcher, ctx.invert, it)
            }
        }
    }
}

fun handleStream(ctx : Context, matcher : LineMatcher, invert : Boolean, input : BufferedReader) {
    var lineNumber = 1
    ctx.currentLineProvider = {lineNumber}

    val formatter = OutputFormatter(ctx)

    do {
        val line = input.readLine()
        if (line == null) {
            break
        }

        if (matcher.matches(line) != invert) {
            formatter.format(line, matcher, System.out)
            System.out.println()
        }

        lineNumber++
    } while (true)
}

fun printUsage() {
    println("java -Xmx8m -jar kgrep.jar [-iovVernhp] [--] [files or -]")
    println("    -i  Case insensitive")
    println("    -o  Print only matched part of line")
    println("    -v  invert: print only lines not matching the pattern")
    println("    -V,--version  print version")
    println("    -e  use regular expressions (Java dialect)")
    println("    -r  recursive search throuh directory tree")
    println("    -n  print line numbers")
    println("    -h  print file names")
    println("    -p  specify output pattern, overrides -o option")
    println("         pattern may contain $0 for group 0, $1 for group1, etc")
    println("         \$line  line number")
    println("         \$file  current file path")
    println("         \$full  full line")
    println("         if search inverted (-v option) or regular expressions disabled then there is only $0 group present")
    println("            and group $0 = \$full = full line")
    println()
    println("     Example: ")
    println("        java -jar kgrep.jar -rn println .")
    println("      will search for all println in current directory")
    println()
    println("     Example with pattern:")
    println("""      java -jar kgrep.jar "if \(([^\)]+)\)" -rnep $1 .""")
    println("      will search for if keywords in current directory and print condition inside (group $1)")

    println()
}

class BadArgException(message : String) : Exception(message)