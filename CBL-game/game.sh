#!/bin/sh

if ! ls build>/dev/null;then
    mkdir build;
fi
javac -d build GameRuntime.java;
cp ./sprites/*.png ./build/
cd build;
java GameRuntime;
cd ..;
