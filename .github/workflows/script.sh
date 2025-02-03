#!/bin/bash
mkdir -p ./message/schema
mkdir -p ./message/example
mkdir -p ./message/diagram
mkdir ./figures
cp ./artifacts/src/main/resources/**/* ./message/schema || true
echo "successfully copied message schemas"
cp -R "./artifacts/src/main/resources"/**/"example"/* ./message/example
echo "successfully copied message examples"
cp -R "./specifications"/**/"message/diagram"/*".png" message/diagram
echo "successfully copied message diagrams"
cp -R "./specifications"/**/"figures"/*".png" ./figures/
echo "successfully copied figures"
#rm -rf artifacts
#echo "successfully removed artifacts"