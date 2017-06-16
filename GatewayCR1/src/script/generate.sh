#!/bin/bash

echo $1

#Step 1: Generate a Private Key
echo -------------------------------------------- 1 
openssl genrsa -des3 -out $1.key 1024

#Step 2: Generate a CSR (Certificate Signing Request)
echo -------------------------------------------- 2
openssl req -new -key $1.key -out $1.csr

#Step 3: Remove Passphrase from Key 
echo -------------------------------------------- 3
cp $1.key $1.key.org
openssl rsa -in $1.key.org -out $1.key


#Step 4: Generating a Self-Signed Certificate
echo -------------------------------------------- 4
openssl x509 -req -days 365 -in $1.csr -signkey $1.key -out $1.crt


#Step 5: Generating PEM Certificate 
echo ---------------------- 5
cat $1.crt > $1.pem
cat $1.key >> $1.pem


echo --------------------------------------------  TERMINATED



