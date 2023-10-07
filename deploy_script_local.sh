#!/bin/bash

###
### Script for deploying app on set-up environment (runs on localhost or SSH on virtual machine)
###

###TODO vytvorenie priecinkov pre file uploady
### a nastavit prava

cd /var/www/paster-backend/paster-backend

git pull

./gradlew build

cd build/libs

systemctl restart paster-backend

exit
