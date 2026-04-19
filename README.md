# 🎮 Hangman Game (Jogo da Forca)

## 📌 Descrição do Projeto

Este projeto consiste na implementação do clássico **Jogo da Forca** utilizando a linguagem **Java**.
O objetivo deste Jogo da Forca em modo multijogador síncrono, no qual 2 a 4 jogadores participam em simultâneo,é colaborar na tentativa de descobrir uma palavra oculta comum.
Existe também uma vertente mais competitiva no jogo com a implementacao de um sistema de pontos que faz com que este jogo se torne mais competitivo e acrescente um nivell de estratégia maior.

O projeto foi desenvolvido com fins educativos, permitindo praticar conceitos fundamentais de programação como:

* Estruturas de controlo (if/else)
* Ciclos (loops)
* Manipulação de strings
* Listas
* Funções
* Entrada e saída de dados
* Concorrência

---

## 🧠 Como o Jogo Funciona

1. O programa escolhe uma palavra aleatória a partir de uma lista.
2. O jogador tenta adivinhar a palavra introduzindo letras.
3. Se a letra estiver correta:

   * Ela é revelada na posição correspondente.
4. Se a letra estiver errada:

   * O jogador perde uma vida.
5. O jogo termina quando:

   * O jogador adivinha todas as letras ou a palavra (vitória)
   * O número de vidas chega a zero (derrota)

---

## 🚀 Funcionalidades

* Seleção aleatória de palavras
* Sistema de vidas
* Sistema de pontos
* Verificação de letras repetidas
* Broadcasts e Mensagens unicas devidamente sincronizadas
* Tratamento de erros de desconexão
* Atualização dinâmica da palavra
* Condição de vitória e derrota
* Interface em consola
* Estrutura modular do código

---

## 🛠 Tecnologias Utilizadas

* Java
* PowerShell
* Shell
* Batchfile

---

## 📁 Estrutura do Projeto

```
## 📁 Estrutura do Projeto

HangMan/
│
├── bin/                     # Ficheiros compilados (.class)
│
├── src/                     # Código-fonte do projeto
│   │
│   ├── client/              # Lógica do cliente
│   │   ├── Client.java
│   │   ├── Client_Read.java
│   │   └── Client_Write.java
│   │
│   ├── game/                # Componentes principais do jogo
│   │   ├── Game.java
│   │   ├── Lobby.java
│   │   ├── Room.java
│   │   └── Word.java
│   │
│   ├── server/              # Lógica do servidor
│   │   ├── Connection.java
│   │   ├── Origin.java
│   │   └── Server.java
│   │
│   └── utils/               # Classes utilitárias
│       ├── Ler.java
│       ├── Menus.java
│       └── Message.java
│
├── palavras.txt             # Lista principal de palavras
├── palavrasD.txt            # Palavras - dificuldade D
├── palavrasF.txt            # Palavras - dificuldade F
├── palavrasM.txt            # Palavras - dificuldade M
│
├── run_Hangman.bat          # Script de execução (Windows)
├── run_Hangman.sh           # Script de execução (Linux/Mac)
├── launcher.ps1             # Script de execução (PowerShell)
│
├── .classpath               # Configuração do projeto Java
├── .project                 # Configuração do IDE (ex: Eclipse)
└── README.md                # Documentação do projeto


```


---

## ▶️ Como Executar o Projeto

### 1. Clonar o repositório

```
git clone https://github.com/KJBruninho/Hangman_Game.git
```

### 2. Entrar na pasta do projeto

```
cd Hangman_Game
```

### 3. Executar o jogo

```
./run_Hangman.sh
```
or
```
run_Hangman.bat
```
---

## 🎯 Objetivos de Aprendizagem

Este projeto foi desenvolvido para:

* Praticar lógica de programação
* Compreender estruturas de decisão
* Trabalhar com ciclos e listas
* Aprender a organizar código em módulos
* Aprender a lidar com concorrencia
* Desenvolver um pequeno jogo em consola

---

## 🔮 Possíveis Melhorias Futuras

* Interface gráfica (GUI)
* Níveis de dificuldade melhorados
* Sistema de pontuação melhorados
* Suporte a diferentes idiomas
* Guardar histórico de jogos
* Replay e reutilizacao de salas

---
## 👨‍💻 Autor

* Bruno Marinho
* Miguel Lameiras

---

## 📄 Licença

MIT License

Copyright (c) 2026 Bruno Marinho

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

