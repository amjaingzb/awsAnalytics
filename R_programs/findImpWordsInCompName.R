compData <- read.csv("~/Work/Analytics/T1_splitCSV/company_master_data_upto_Mar_2015_Telangana.csv")
splitSentence <- function(sentence){
  strsplit(sentence, " ") }

  apply(compData[3],1,splitSentence)
   compNameWords <- apply(compData[3],1,splitSentence)
   compNameWordsSplit <- unlist(compNameWords)

   compNameWordsSplitTable <- table(compNameWordsSplit)
impCompWords <-   compNameWordsSplitTable[compNameWordsSplitTable > 1]
