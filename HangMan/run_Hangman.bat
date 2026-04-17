@echo off
cd /d "%~dp0"
powershell -ExecutionPolicy Bypass -File "launcher.ps1"
pause