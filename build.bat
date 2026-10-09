@echo off
setlocal
set "PROJECT_DIR=%~dp0"

where javac >nul 2>nul
if errorlevel 1 (
    echo JDK 17 or newer is required. Install a JDK and add its bin folder to PATH.
    exit /b 1
)

if not exist "%PROJECT_DIR%build" mkdir "%PROJECT_DIR%build"
javac -Xlint:all -d "%PROJECT_DIR%build" "%PROJECT_DIR%PenaltyShootout.java"
if errorlevel 1 exit /b 1

echo Build complete.
