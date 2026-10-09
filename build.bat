@echo off
echo Compiling Java files...
javac PenaltyShootoutjava.java

echo Creating Manifest file...
echo Main-Class: PenaltyShootout > manifest.txt

echo Building executable JAR...
jar cfm PenaltyShootout.jar manifest.txt *.class

echo Cleaning up temporary build files...
del manifest.txt
del *.class

echo Process Complete! Check your sidebar.
pause

