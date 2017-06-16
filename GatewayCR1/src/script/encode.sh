#!/bin/bash

echo -------------------------------------------- INPUT $1

echo --------------------------------------------  Verify CERT
openssl verify https.pem
openssl verify s2e.pem
openssl verify bdi.pem

src=$1
dest=$src.zip
echo --------------------------------------------  Zip $src -- $dest
zip $dest $src 

src=$dest
dest=$src.p7e
echo --------------------------------------------  Encode $src -- $dest
openssl cms -encrypt -binary -aes256 -in $src -outform DER -out $dest bdi.pem

src=$dest
dest=$src.p7m
echo --------------------------------------------  Sign $src -- $dest
openssl cms -sign -binary -in $src -out $dest -nodetach -outform DER -signer s2e.pem







