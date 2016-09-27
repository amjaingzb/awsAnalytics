library(jsonlite)
#library(plyr)
df <- read.table("dataForHistogram.txt",header = FALSE)
#table(df)
myVector <- df[[1]]
myVector <- myVector[!is.na(myVector)]
h<-hist(myVector, seq(1900,2020, 10), plot=FALSE)
l1 <- list(h$breaks, h$counts)
j1 <- toJSON(l1)
write(j1, "out.json")

