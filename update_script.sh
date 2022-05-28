#!/bin/bash

cd /var/www/paster-backend/paster-backend

git pull

./gradlew build

cd build/libs

systemctl restart paster-backend

exit
