compData <- read.csv("~/Work/Analytics/T1_splitCSV/company_master_data_upto_Mar_2015_Telangana.csv")
#compData <- compData[sample(1:nrow(compData),500,replace=FALSE)]
splitSentence <- function(sentence){
  strsplit(sentence, " ") }

  apply(compData[3],1,splitSentence)
   compNameWords <- apply(compData[3],1,splitSentence)
   compNameWordsSplit <- unlist(compNameWords)

   compNameWordsSplitTable <- table(compNameWordsSplit)
impCompWords <-   compNameWordsSplitTable[compNameWordsSplitTable > 1]

#featureNames <- c('CHIT', 'CONSTRUCTIONS', 'ESTATES', 'FARMS','FINANCE', 'INDIA', '(INDIA)', 'INFRA','IT' ,'LIMITED','LTD', 'LTD.','POWER','PRIVATE', 'PROJECTS','PVT', 'SAI', 'SERVICES', 'SOFTWARE' , 'SRI', 'SYSTEMS','TECHNOLOGIES')
featureNames <- c('CHIT', 'CONSTRUCTIONS', 'ESTATES', 'FARMS','FINANCE', 'INDIA', '(INDIA)', 'INFRA','IT' ,'POWER','PRIVATE', 'PROJECTS','PVT', 'SAI', 'SERVICES', 'SOFTWARE' , 'SRI', 'SYSTEMS','TECHNOLOGIES')
#featureFrame <- data.frame(matrix(rep.int(0,length(featureNames)*length(compNameWords)),nrow=length(compNameWords), ncol=length(featureNames)))

 colnames(featureFrame) <- featureNames

extractFeature <- function(compNameWordsRow) {
as.numeric(featureNames %in% unlist(compNameWordsRow)) }
featureFramePopulated <- lapply(compNameWords , extractFeature)
featureFramePopulatedAsNumeric <- matrix(unlist(featureFramePopulated), nrow=length(featureFramePopulated), byrow=TRUE)
kResult <- kmeans(featureFramePopulatedAsNumeric,8)

for(ctgry in seq(1:8)){
    temp = compData[[3]][kResult$cluster==ctgry]
  tempM = matrix(unlist(temp),nrow=length(temp),byrow=TRUE)
    write(tempM,file=paste(ctgry,"ctgry.txt"))
}
