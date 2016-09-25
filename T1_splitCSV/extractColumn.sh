cat company_master_data_upto_Mar_2015_Telangana.csv | cut -d "," -f 2 > /tmp/del1
cat /tmp/del1 | cut -d "-" -f3 | cut -d "\"" -f1 > output.txt

