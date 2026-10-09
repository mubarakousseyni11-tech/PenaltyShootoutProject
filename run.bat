@echo off
setlocal
set "PROJECT_DIR=%~dp0"

call "%PROJECT_DIR%build.bat"
if errorlevel 1 exit /b 1

java -cp "%PROJECT_DIR%build" PenaltyShootout
