if not exist "build" mkdir "build"

javac -d build GameRuntime.java
xcopy sprites\*.png build /S /I /Y >nul
cd build
java GameRuntime
cd ..
