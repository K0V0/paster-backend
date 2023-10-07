#!/bin/bash



###
### Base script containing constants, vars and commonly used procedures and other shared content for scripts
###



### Settings & constants
FILES_DIR="testUploads/files"
TEMP_FILES_DIR="testUploads/temp"
DAYS_TO_KEEP=30



### shared and calculated basic vars
working_directory_path=$(dirname "$(pwd)")
user_primary_group_id=$(id -g)
user_primary_group_name=$(getent group "$user_primary_group_id" | cut -d: -f1)
current_datetime=$(date +'%Y-%m-%d-%H%M%S')

files_dir="$working_directory_path/$FILES_DIR"
temp_files_dir="$working_directory_path/$TEMP_FILES_DIR"



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
    find "$directory" -type d -ctime +"$DAYS_TO_KEEP" -exec rm -rf {} \;
    echo -e "Searched for old folders to be removed"
    echo -e "\n"
}