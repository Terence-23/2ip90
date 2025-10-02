#!/bin/sh

if ! ls build;then
    mkdir build;
fi
javac -d build GameRuntime.java;
cd build;
java GameRuntime;
cd ..;
