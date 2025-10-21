if not exist "build" mkdir "build"

javac -d build GameRuntime.java
cp ./sprites/*.png ./build/
cd build
java GameRuntime
cd ..
