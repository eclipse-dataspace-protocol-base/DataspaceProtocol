#!/bin/bash
set -e

mkdir -p HEAD
git fetch --all --tags
tags_string=$(git tag)
echo got tag string
echo $tags_string
tags_array=($tags_string)
mv $(ls --ignore=HEAD) HEAD/
for tag in "${tags_array[@]}"
do
  echo starting with tag $tag
  mkdir $tag
  cd $tag
  git clone https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol.git --depth 1 --branch ${tag} --quiet
  mv ./DataspaceProtocol/* .
  cd ..
done
for dir in */; do
  echo "$dir"
  pwd
  ls -l
  if [ -f "$dir/index.html" ]; then
      cd "$dir/artifacts"
      ./gradlew build
      ./gradlew generateTablesFromSchemas
      cd ..
      pwd
      mkdir ./figures
      mkdir -p ./message/schema
      mkdir -p ./message/example
      mkdir -p ./message/table
      cp -r ./artifacts/src/main/resources/**/*-schema.json ./message/schema
      echo "successfully copied message schemas"
      cp -r ./artifacts/src/main/resources/context/*.jsonld ./message/schema
      echo "successfully copied jsonld contexts"
      cp -r ./artifacts/src/main/resources/**/example/*.json ./message/example
      echo "successfully copied message examples"
      cp -r ./specifications/**/figures/*.png ./figures/
      echo "successfully copied figures"
      cp -r ./artifacts/build/generated/tables/*.html ./message/table
      echo "successfully copied message property tables"
      cd ..
  else
    echo "index does not exist. No copy operations"
  fi
done