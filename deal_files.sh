#!/bin/bash

cd $1
if [ ! -d "dealcss" ];then
  mkdir dealcss
fi

SOURCE=$2
PREFIX="${SOURCE%%.*}"
SUFIX="${SOURCE##*.}"

if [ "rar" =$SUFIX ];then
unrar x $2 
fi

if [ "zip" = $SUFIX ];then
unzip $2
fi

cd $PREFIX

for file in $(ls *)
do
  NAME=$file
  THISSUFIX="${NAME##*.}"
  if [ $THISSUFIX = $3 ];then
    if [ $3 = "scss" ];then
  	sass $NAME ../dealcss/"${NAME%%.*}".css --sourcemap=none
    fi
    if [ $3 = "less" ];then
	/opt/install/node_modules/less/bin/lessc $NAME ../dealcss/"${NAME%%.*}".css
    fi
  fi  
done

cd ..

if [ "rar" = $SUFIX ];then
rar a deal_$PREFIX.rar dealcss           
fi

if [ "zip" = $SUFIX ];then
zip deal_$PREFIX.zip dealcss
fi
