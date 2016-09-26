 #!/bin/bash 
function updateHistory {
  fName=`echo $(date "+%s").$(date +"%d%h%Y").txt`
  hFile=myHistory.$fName
  pFile=myPackages.$fName

#Append any new history into the existing file. using awk to strip of the command number 
  history | awk '{$1="" ; print $0}'  >> ~/history.txt
#Clear out the duplicates & update new history 
  cat ~/history.txt | sort -u   > ~/$hFile
  cp ~/$hFile ~/history.txt
#Build package info

  cat ~/history.txt | egrep "^ apt-get|^ npm|^ sudo apt-get" >> ~/packages.txt 
  cat ~/packages.txt | sort -u   > ~/$pFile
  cp ~/$pFile ~/packages.txt

  mv ~/$hFile ~/DustBin/
  mv ~/$pFile ~/DustBin/

  exit
}
updateHistory
