#!/bin/bash
#Author: Rohtash Lakra
# Generates the RSA private/public key pair.
export SERVICE=$1
export SIZE=$2

# Usage
function usage() {
  echo
  echo -e "\tUsage: $0 <ServiceName> <Size>"
  echo -e "\t <Size> can be one of [2048, 4096]."
  echo
  echo -e "\t\t./newRSAKeyPair paymentService 2048"
  echo
}

if [ "$#" -lt 2 ]; then
  usage
  exit
fi

if [ -z "${SERVICE}" ]; then
  usage
  exit
fi

# Generate RSA Private Key
openssl genrsa -out "${SERVICE}_private.key" $SIZE
openssl rsa -in "${SERVICE}_private.key" -pubout > "${SERVICE}_public.key"
echo
