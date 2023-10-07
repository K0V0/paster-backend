#!/bin/bash

source _base.sh



###
### Script for manual deploying app on set-up environment (runs on localhost or SSH on virtual machine)
###



### Preparations
create_directory_if_not_exist_with_permissions "$temp_files_dir"
create_directory_if_not_exist_with_permissions "$files_dir"
echo -e "\n"



### BUILD!
cd "$working_directory_path" || exit

git pull

sh gradlew clean build -x test

cd "$working_directory_path/build/libs" || exit

systemctl restart paster-backend



### finished
exit
