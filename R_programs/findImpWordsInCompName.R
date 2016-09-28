#compData <- read.csv("~/Work/Analytics/T1_splitCSV/company_master_data_upto_Mar_2015_Telangana.csv")
compData <- read.csv("sampleData.txt")
compDataNames <- compData[3]; 
categoryCount <- 3
freqThreshold <- 1
compDataNamesSample <- levels(compDataNames[[1]])
#compDataNamesSample <- levels(compDataNames[[1]])[sample(1:nrow(compDataNames),20,replace=FALSE)]
splitSentence <- function(sentence){
    strsplit(sentence, " ") }

       compNameWords <- splitSentence(compDataNamesSample)
          compNameWordsSplit <- unlist(compNameWords)

             compNameWordsSplitTable <- table(compNameWordsSplit)
             impWords <-  compNameWordsSplitTable[compNameWordsSplitTable > freqThreshold]
              featureNames <- names(impWords)
#featureNames <- c('CHIT', 'CONSTRUCTIONS', 'ESTATES', 'FARMS','FINANCE', 'INDIA', '(INDIA)', 'INFRA','IT' ,'LIMITED','LTD', 'LTD.','POWER','PRIVATE', 'PROJECTS','PVT', 'SAI', 'SERVICES', 'SOFTWARE' , 'SRI', 'SYSTEMS','TECHNOLOGIES')
#featureNames <- c('CHIT', 'CONSTRUCTIONS', 'ESTATES', 'FARMS','FINANCE', 'INDIA', '(INDIA)', 'INFRA','IT' ,'POWER','PRIVATE', 'PROJECTS','PVT', 'SAI', 'SERVICES', 'SOFTWARE' , 'SRI', 'SYSTEMS','TECHNOLOGIES')
#featureNames <- c('CHIT', 'CONSTRUCTIONS', 'ESTATES', 'FARMS','FINANCE', 'INDIA', '(INDIA)', 'INFRA','IT' ,'POWER','PRIVATE', 'PROJECTS','PVT', 'SAI', 'SERVICES', 'SOFTWARE' , 'SRI', 'SYSTEMS','TECHNOLOGIES')
#featureFrame <- data.frame(matrix(rep.int(0,length(featureNames)*length(compNameWords)),nrow=length(compNameWords), ncol=length(featureNames)))

 colnames(featureFrame) <- featureNames

 extractFeature <- function(compNameWordsRow) {
   as.numeric(featureNames %in% unlist(compNameWordsRow)) }
   featureFramePopulated <- lapply(compNameWords , extractFeature)
   featureFramePopulatedAsNumeric <- matrix(unlist(featureFramePopulated), nrow=length(featureFramePopulated), byrow=TRUE)
   require(klaR)
   kResult <- kmodes(featureFramePopulatedAsNumeric,categoryCount,iter.max=100)

   for(ctgry in seq(1:categoryCount)){
         temp = compDataNamesSample[kResult$cluster==ctgry]
           tempM = matrix(unlist(temp),nrow=length(temp),byrow=TRUE)
               write(tempM,file=paste(ctgry,"ctgry.txt"))
   }
   categoriesFeatures <- matrix(unlist(kResult$modes),nrow=length(kResult$modes), byrow=TRUE)

   for(categoryType in 1:ncol(categoriesFeatures))
   {
      a <- paste(categoryType,'is :',collapse=" ")
    b <- paste(featureNames[categoriesFeatures[,categoryType]=="1"], collapse=" ")
     print(paste(a,b, collapse=" "))
   }
