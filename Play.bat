@echo off
set JRE_PATH=".\OpenJDK25U-jdk_x64_windows_hotspot_25.0.4.1_1\bin\java.exe"
if exist %JRE_PATH% (
    %JRE_PATH% -jar PenaltyShootoutProject.jar
) else (
    echo Portable Java folder missing! Checking system...
    java -jar PenaltyShootoutProject.jar
)
pause
