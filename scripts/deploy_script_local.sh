#!/bin/bash

cd ..

source scripts/_base.sh



###
### Script for manual deploying app on set-up environment (runs on localhost or SSH on virtual machine)
###



### Preparations
create_directory_if_not_exist_with_permissions "$temp_files_dir"
create_directory_if_not_exist_with_permissions "$files_dir"
echo -e "\n"



### BUILD!
cd /var/www/paster-backend/paster-backend

git pull

./gradlew clean build -x test

cd build/libs

systemctl restart paster-backend

exit
