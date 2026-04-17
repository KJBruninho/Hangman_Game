# HangMan Launcher - PowerShell Version

function Show-Menu {
    Clear-Host
    Write-Host "=====================================================" -ForegroundColor Cyan
    Write-Host "               HANGMAN - CONTROLO (PS)" -ForegroundColor Cyan
    Write-Host "=====================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "[1] Compilar projeto"
    Write-Host "[2] Iniciar Servidor"
    Write-Host "[3] Abrir Cliente"
    Write-Host "[4] Abrir 2 Clientes"
    Write-Host "[5] Abrir 3 Clientes"
    Write-Host "[6] Abrir 4 Clientes"
    Write-Host "[7] Abrir 5 Clientes"
    Write-Host "[8] Limpar /bin"
    Write-Host "[0] Sair"
    Write-Host ""
}

while ($true) {
    Show-Menu
    $opt = Read-Host "Escolha uma opcao"

    switch ($opt) {
        "1" {
            Clear-Host
            Write-Host "Compilando..." -ForegroundColor Yellow
            if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
            
            javac -encoding UTF-8 -d bin src/utils/*.java src/game/*.java src/server/*.java src/client/*.java
            
            if ($LASTEXITCODE -ne 0) {
                Write-Host "`nERRO NA COMPILACAO" -ForegroundColor Red
            } else {
                Write-Host "`nSUCESSO - Projeto compilado" -ForegroundColor Green
            }
            Write-Host "Pressione Enter para voltar ao menu..."
            Read-Host
        }
        "2" {
            Write-Host "A iniciar servidor..." -ForegroundColor Magenta
            Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$Host.UI.RawUI.WindowTitle='Servidor HangMan'; java -cp bin server.Origin"
        }
        "3" {
            Write-Host "A abrir cliente..."
            Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$Host.UI.RawUI.WindowTitle='Cliente HangMan'; java -cp bin client.Client"
        }
        { @("4","5","6","7") -contains $_ } {
            $num = [int]$_ - 2
            Write-Host "A abrir $num clientes..."
            for ($i = 1; $i -le $num; $i++) {
                Start-Process powershell -ArgumentList "-NoExit", "-Command", "`$Host.UI.RawUI.WindowTitle='Cliente $i'; java -cp bin client.Client"
            }
        }
        "8" {
            if (Test-Path "bin") {
                Remove-Item -Path "bin" -Recurse -Force
                Write-Host "Pasta /bin removida." -ForegroundColor Green
            } else {
                Write-Host "A pasta /bin não existe." -ForegroundColor Gray
            }
            Write-Host "Pressione Enter para continuar..."
            Read-Host
        }
        "0" {
            exit
        }
        Default {
            Write-Host "Opção inválida!" -ForegroundColor Red
            Start-Sleep -Seconds 1
        }
    }
}