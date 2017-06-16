#!/bin/bash

echo $1

#echo --------------------------------------------  Zip $1
#zip $1.zip $1


echo --------------------------------------------  Verify CERT
openssl verify https.pem
openssl verify s2e.pem
openssl verify bdi.pem

echo --------------------------------------------  Encode $1.p7e
openssl cms -encrypt -binary -aes256 -in $1 -outform DER -out $1.p7e bdi.pem
openssl cms -encrypt -binary -aes256 -in $1 -outform PEM -out $1.dem.p7e bdi.pem

#echo --------------------------------------------  Decode $1
#openssl cms -decrypt -in $1.p7e -inform DER  -recip bdi.crt -inkey bdi.key -out $1.p7e.decoded
#openssl cms -decrypt -in $1.dem.p7e -inform PEM  -recip bdi.crt -inkey bdi.key -out $1.dem.p7e.decoded


echo --------------------------------------------  Sign $1.p7e.p7m
#openssl smime -sign -binary -in $1.p7e -out $1.p7e.p7m -nodetach -outform DER -signer s2e.pem
#openssl smime -sign -binary -in $1.dem.p7e -out $1.dem.p7e.p7m -nodetach -outform PEM -signer s2e.pem
openssl cms -sign -binary -in $1.p7e -out $1.p7e.p7m -nodetach -outform DER -signer s2e.pem
openssl cms -sign -binary -in $1.dem.p7e -out $1.dem.p7e.p7m -nodetach -outform PEM -signer s2e.pem

echo --------------------------------------------  Parse $1.p7e.p7m
openssl asn1parse -i -inform DER -in $1.p7e.p7m

echo --------------------------------------------  Verify $1.p7e.verified
#openssl smime -verify -noverify -in $1.p7e.p7m -inform DER -out $1.p7e.verified
openssl cms -verify -noverify -in $1.p7e.p7m -inform DER -out $1.p7e.verified

echo --------------------------------------------  Decode $1.decoded
openssl cms -decrypt -in $1.p7e.verified -inform DER  -recip bdi.crt -inkey bdi.key -out $1.decoded

echo --------------------------------------------  Certificate $1.pem.certificate
openssl pkcs7 -inform DER -in $1.p7e.p7m -print_certs -out $1.pem.certificate
