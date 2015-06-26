# kgrep

kgrep is a small example demonstrates simple grep utility written in [Kotlin](http://kotlinlang.org)

Key features:
- search through directory tree (useful on Solaris where no fgrep available by default)
- regular expressions (Java dialect)
- custom output pattern (like awk can do)
- Java implementation can run on many architectures

Usage

```
java [-Xmx8m] -jar kgrep.jar [-iovVernhp] [--] [files or -]
    -i  Case insensitive
    -o  Print only matched part of line
    -v  invert: print only lines not matching the pattern
    -V,--version  print version
    -e  use regular expressions (Java dialect)
    -r  recursive search throuh directory tree
    -n  print line numbers
    -h  print file names
    -p  specify output pattern, overrides -o option
         pattern may contain $0 for group 0, $1 for group1, etc
         $line  line number
         $file  current file path
         $full  full line
         if search inverted (-v option) or regular expressions disabled then there is only $0 group present
            and group $0 = $full = full line

     Example:
        java -jar kgrep.jar -rn println .
      will search for all println in current directory

     Example with pattern:
      java -jar kgrep.jar "if \(([^\)]+)\)" -rnep '$1' .
        will search for if keywords in current directory and print only condition inside (group $1)
```
