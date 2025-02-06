#!/bin/bash
set -e

mkdir ./figures
mkdir -p ./message/schema
mkdir -p ./message/example
mkdir -p ./message/table
cp ./artifacts/src/main/resources/**/* ./message/schema || true
echo "successfully copied message schemas"
cp -r "./artifacts/src/main/resources"/**/"example"/* ./message/example
echo "successfully copied message examples"
cp -r "./specifications"/**/"figures"/*".png" ./figures/
echo "successfully copied figures"
cp -r "./artifacts/build/generated/tables"/*".html" ./message/table
echo "successfully copied message property tables"

#rm -rf artifacts
#echo "successfully removed artifacts"