if not exist "build" mkdir "build"

javac -d build GameRuntime.java
cd build
java GameRuntime
cd ..
