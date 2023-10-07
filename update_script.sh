#!/bin/bash

###TODO vytvorenie priecinkov pre file uploady
### a nastavit prava

cd /var/www/paster-backend/paster-backend

git pull

./gradlew build

cd build/libs

systemctl restart paster-backend

exit
