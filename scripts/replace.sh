cp scripts/respec-template.html index.html
mkdir ./message/diagram -p
cp ./**/message/diagram/*.png ./message/diagram/
cp ./releases/* . -r
index=`cat index.html`
ovph="THIS IS THE PLACEHOLDER FOR THE OVERVIEW"
modph="THIS IS THE PLACEHOLDER FOR THE MODEL"
comph="THIS IS THE PLACEHOLDER FOR THE COMMON FUNCTIONALITIES"
cataph="THIS IS THE PLACEHOLDER FOR THE CATALOG PROTOCOL"
cnph="THIS IS THE PLACEHOLDER FOR THE CONTRACT NEGOTIATION PROTOCOL"
tpph="THIS IS THE PLACEHOLDER FOR THE TRANSFER PROCESS PROTOCOL"
ov=`cat README.md`
mod="`cat model/terminology.md`"$'\n\n'"`cat model/model.md`"
cata="`cat catalog/catalog.protocol.md`"$'\n\n'"`cat catalog/catalog.binding.https.md`"
com="`cat common/common.protocol.md`"$'\n\n'"`cat common/common.binding.https.md`"
cata="`cat catalog/catalog.protocol.md`"$'\n\n'"`cat catalog/catalog.binding.https.md`"
cn="`cat negotiation/contract.negotiation.protocol.md`"$'\n\n'"`cat negotiation/contract.negotiation.binding.https.md`"
tp="`cat transfer/transfer.process.protocol.md`"$'\n\n'"`cat transfer/transfer.process.binding.https.md`"
index="${index/$ovph/"$ov"}"
index="${index/$modph/"$mod"}"
index="${index/$comph/"$com"}"
index="${index/$cataph/"$cata"}"
index="${index/$cnph/"$cn"}"
index="${index/$tpph/"$tp"}"
echo "$index" > index.html -e