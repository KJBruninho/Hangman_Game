@echo off
setlocal
title HangMan - Launcher

:menu
cls
echo =====================================================
echo                HANGMAN - CONTROLO
echo =====================================================
echo.
echo [1] Compilar projeto
echo [2] Iniciar Servidor (Origin)
echo [3] Abrir Cliente
echo [4] Abrir 3 Clientes
echo [5] Limpar /bin
echo [0] Sair
echo.

set /p opt="Escolha uma opcao: "

if "%opt%"=="1" goto compile
if "%opt%"=="2" goto server
if "%opt%"=="3" goto client
if "%opt%"=="4" goto clients3
if "%opt%"=="5" goto clean
if "%opt%"=="0" exit

goto menu

:compile
cls
echo =====================================================
echo                COMPILACAO
echo =====================================================
echo.

if not exist bin mkdir bin

javac -encoding UTF-8 -d bin ^
src\utils\*.java ^
src\game\*.java ^
src\server\*.java ^
src\client\*.java

if %errorlevel% neq 0 (
    echo.
    echo ERRO NA COMPILACAO
    pause
    goto menu
)

echo.
echo SUCESSO - Projeto compilado
pause
goto menu

:server
cls
echo A iniciar servidor (Origin)...

start "Servidor HangMan" cmd /k java -cp bin server.Origin

goto menu

:client
cls
echo A abrir cliente...

start "Cliente HangMan" cmd /k java -cp bin client.Client

goto menu

:clients3
cls
echo A abrir 3 clientes...

start "Cliente 1" cmd /k java -cp bin client.Client
start "Cliente 2" cmd /k java -cp bin client.Client
start "Cliente 3" cmd /k java -cp bin client.Client

goto menu

:clean
cls
echo A limpar pasta bin...

if exist bin (
    rmdir /s /q bin
)

echo Limpeza concluida
pause
goto menu
