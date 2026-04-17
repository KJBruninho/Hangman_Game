#!/bin/bash

# Função para pausar e esperar por input (equivalente ao pause do Windows)
pause_menu() {
    read -p "Pressione [Enter] para continuar..."
}

# Função para abrir terminais (tenta usar o terminal padrão do sistema)
open_terminal() {
    local title=$1
    local cmd=$2
    # Tenta usar gnome-terminal, xterm ou konsole dependendo do que estiver instalado
    if command -v gnome-terminal >/dev/null; then
        gnome-terminal --title="$title" -- bash -c "$cmd; exec bash" &
    elif command -v xterm >/dev/null; then
        xterm -T "$title" -e bash -c "$cmd; exec bash" &
    else
        echo "Erro: Terminal (gnome-terminal/xterm) não encontrado. A executar na sessão atual:"
        eval $cmd
    fi
}

while true; do
    clear
    echo "====================================================="
    echo "               HANGMAN - CONTROLO (LINUX)"
    echo "====================================================="
    echo ""
    echo "[1] Compilar projeto"
    echo "[2] Iniciar Servidor"
    echo "[3] Abrir Cliente"
    echo "[4] Abrir 2 Clientes"
    echo "[5] Abrir 3 Clientes"
    echo "[6] Abrir 4 Clientes"
    echo "[7] Abrir 5 Clientes"
    echo "[8] Limpar /bin"
    echo "[0] Sair"
    echo ""
    read -p "Escolha uma opcao: " opt

    case $opt in
        1)
            clear
            echo "====================================================="
            echo "                COMPILACAO"
            echo "====================================================="
            mkdir -p bin
            
            javac -encoding UTF-8 -d bin \
            src/utils/*.java \
            src/game/*.java \
            src/server/*.java \
            src/client/*.java

            if [ $? -ne 0 ]; then
                echo -e "\nERRO NA COMPILACAO"
                pause_menu
            else
                echo -e "\nSUCESSO - Projeto compilado"
                pause_menu
            fi
            ;;
        2)
            echo "A iniciar servidor (Origin)..."
            open_terminal "Servidor HangMan" "java -cp bin server.Origin"
            ;;
        3)
            echo "A abrir cliente..."
            open_terminal "Cliente HangMan" "java -cp bin client.Client"
            ;;
        4|5|6|7)
            num_clients=$((opt - 2))
            echo "A abrir $num_clients clientes..."
            for i in $(seq 1 $num_clients); do
                open_terminal "Cliente $i" "java -cp bin client.Client"
            done
            ;;
        8)
            echo "A limpar pasta bin..."
            rm -rf bin
            echo "Limpeza concluida"
            pause_menu
            ;;
        0)
            exit 0
            ;;
        *)
            echo "Opção inválida."
            sleep 1
            ;;
    esac
done