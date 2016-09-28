#!/usr/bin/Rscript
suppressPackageStartupMessages(require(optparse)) # don't say "Loading required package: optparse"
# manual: http://cran.r-project.org/web/packages/optparse/optparse.pdf
# Refer to example code ~/fiddle/exampleRScript1.r

library(jsonlite)

option_list = list(
                     make_option(c("-s", "--startYear"), action="store", default=1900, type='integer', help="Start Year"),
                     make_option(c("-e", "--endYear"), action="store", default=2020, type='integer', help="End Year"),
                     make_option(c("-i", "--interval"), action="store", default=10, type='integer', help="interval/step"),
                     make_option(c("-v", "--verbose"), action="store_true", default=TRUE,
                                               help="Should the program print extra stuff out? [default %default]"),
                     make_option(c("-q", "--quiet"), action="store_false", dest="verbose", help="Make the program not be verbose.")
                     )
args = parse_args(OptionParser(option_list=option_list))

if(args$v) {
  cat("\n startYear:")
  cat(args$s)
  cat("\n endYear:")
  cat(args$e)
  cat("\n interval:")
  cat(args$i)
  cat("\n")
}

df <- read.table("dataForHistogram.txt",header = FALSE)
myVector <- df[[1]]
myVector <- myVector[!is.na(myVector)]
#h<-hist(myVector, seq(startYear, endYear, step), plot=FALSE)
h<-hist(myVector, seq(args$s, args$e, args$i), plot=FALSE)
l1 <- list(h$breaks, h$counts)
j1 <- toJSON(l1)
#write(j1, "out.json")
cat(j1)

