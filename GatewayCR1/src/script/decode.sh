#!/bin/bash

echo -------------------------------------------- INPUT $1

echo --------------------------------------------  Verify CERT
openssl verify https.pem
openssl verify s2e.pem
openssl verify bdi.pem

src=$1
dest=$src.verified
echo --------------------------------------------  Verify $src -- $dest
openssl cms -verify -noverify -in $src -inform DER -out $dest

src=$dest
dest=$src.decoded
echo --------------------------------------------  Decode $src -- $dest
openssl cms -decrypt -in $src -inform DER  -recip bdi.crt -inkey bdi.key -out $dest


src=$dest
dest=$src.unziped
#echo --------------------------------------------  Un Zip $src -- $dest
unzip -p $src > $dest
