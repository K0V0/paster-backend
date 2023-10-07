#!/bin/bash

cd ..

source scripts/_base.sh



###
### Script for manual running unit & integration test of app locally
### or automatically by GitHub actions on pull request
###



### Settings & constants
TEST_LOGS_DIR="testResults"



### Obtaining and calculating basic vars
test_logs_dir="$working_directory_path/$TEST_LOGS_DIR"
current_test_logs_dir="$test_logs_dir/$current_datetime"
summary_output_file="$current_test_logs_dir/summary.txt"

echo -e "\n"
echo -e "Starting built-in Unit + Integration tests for paster-backend: \n"
echo -e "Working directory path: $working_directory_path \n"
echo -e "Test uploaded temp files directory path: $temp_files_dir \n"
echo -e "Test uploaded files directory path: $files_dir \n"
echo -e "Test log files directory path: $current_test_logs_dir \n"
echo -e "Primary Group ID: $user_primary_group_id \n"
echo -e "Primary Group Name: $user_primary_group_name \n"
echo -e "\n"



### Preparations
remove_old_folders "$test_logs_dir"
create_directory_if_not_exist_with_permissions "$temp_files_dir"
create_directory_if_not_exist_with_permissions "$files_dir"
create_directory_if_not_exist_with_permissions "$current_test_logs_dir"
echo -e "\n"



### RUN!
echo -e "Starting tests..."

sh gradlew test --rerun-tasks > "$summary_output_file" 2>&1