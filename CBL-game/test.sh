
#!/bin/sh

if ! ls build;then
    mkdir build;
fi
javac -d build ColliderTest.java;
cd build;
java ColliderTest;
cd ..;
