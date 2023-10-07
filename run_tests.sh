#!/bin/bash

###
### Script for running unit & integration test of app locally or by GitHub actions
###


### Settings & constants
FILES_DIR="testUploads/files"
TEMP_FILES_DIR="testUploads/temp"
TEST_LOGS_DIR="testResults"
DAYS_TO_KEEP_LOGS=30


### Obtaining and calculating basic vars
working_directory_path="$(pwd)"
user_primary_group_id=$(id -g)
user_primary_group_name=$(getent group "$user_primary_group_id" | cut -d: -f1)
current_datetime=$(date +'%Y-%m-%d-%H%M%S')

files_dir="$working_directory_path/$FILES_DIR"
temp_files_dir="$working_directory_path/$TEMP_FILES_DIR"
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


### Procedures definition
create_directory_if_not_exist_with_permissions() {
    local directory="$1"

    if [ ! -d "$directory" ]; then
        mkdir -p "$directory"
        echo "Directory created: $directory"
    else
        echo "Directory already exists: $directory"
    fi

    # Set permissions if provided
    chown :"$user_primary_group_name" "$directory"
    chmod g+rw "$directory"
    echo "Permissions set for $directory"
    echo -e "\n"
}

remove_old_folders() {
    local directory="$1"
    find "$directory" -type d -ctime +"$DAYS_TO_KEEP_LOGS" -exec rm -rf {} \;
    echo -e "Searched for old logs to be removed"
    echo -e "\n"
}

### Preparations
remove_old_folders "$test_logs_dir"
create_directory_if_not_exist_with_permissions "$temp_files_dir"
create_directory_if_not_exist_with_permissions "$files_dir"
create_directory_if_not_exist_with_permissions "$current_test_logs_dir"
echo -e "\n"


### RUN!
echo -e "Starting tests..."

sh gradlew test --rerun-tasks > "$summary_output_file" 2>&1