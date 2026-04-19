# Hangman Game

## Project Description

This project consists of an implementation of the classic **Hangman Game** using **Java**.
The objective is to provide a synchronous multiplayer Hangman experience where 2 to 4 players participate simultaneously, collaborating to discover a common hidden word.
The game also features a competitive edge with an implemented scoring system, adding strategy and engagement to the cooperative gameplay.

This project was developed for educational purposes, allowing the practice of fundamental programming concepts such as:

* Control structures (if/else)
* Loops
* String manipulation
* Lists
* Functions
* Input and output (I/O)
* Concurrency

---

## How the Game Works

1. The program selects a random word from a predefined list.
2. Players attempt to guess the word by entering letters.
3. If the letter is correct:
   * It is revealed in its corresponding position(s).
4. If the letter is incorrect:
   * The player loses a life.
5. The game ends when:
   * The players guess all the letters or the full word (Victory).
   * The number of lives reaches zero (Defeat).

---

## Features

* Random word selection
* Life/Health system
* Scoring system
* Duplicate letter verification
* Synchronized broadcasts and unique messaging
* Disconnection error handling
* Dynamic word updates
* Victory and defeat conditions
* Console-based interface
* Modular code structure

---

## Technologies Used

* Java
* PowerShell
* Shell
* Batchfile

---

## Project Structure
## 📁 Estrutura do Projeto

```
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

## How to Run the Project

### 1. Clone the repository

```bash
git clone https://github.com/KJBruninho/Hangman_Game.git
```

### 2. Enter the project directory

```
cd Hangman_Game
```

### 3. Run the game

```
./run_Hangman.sh
```
or
```
run_Hangman.bat
```
---

## Learning Objectives

This project was developed to:

* Practice programming logic
* Understand decision structures
* Work with loops and lists
* Learn to organize code into modules
* Learn to handle concurrency
* Develop a small console-based game

---

## Future Improvements

* Graphical User Interface (GUI)
* Improved difficulty levels
* Enhanced scoring systems
* Support for multiple languages
* Save game history
* Room replay and reuse

---

## Authors

* Bruno Marinho
* Denise Brandão
* Miguel Lameiras

---

## Reviewers

* Bruno Marinho
* Manuel Salvador Santos
* Miguel Lameiras
* Tânia Lopes Marinho
