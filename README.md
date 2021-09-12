# Chat Multicast em Java

Este é um trabalho desenvolvido para a disciplina de Laboratório de Desenvolvimento de Aplicações Móveis e Distribuídas (LDAMD) do 5º período curso de Engenharia de Software - Pontifícia Universidade Católica de Minas Gerais (PUC Minas). O intuito deste é a criação de um chat utilizando o procolo Multicast, desenvolvido na linguagem Java com auxílio de Sockets.

**Aluno:** Altino Alves Júnior | **Professor:** Ricardo Carlini Sperandio

### Requisitos

1. O servidor deve gerenciar múltiplas salas de bate papo.
2. O cliente deve ser capaz de solicitar a lista de salas.
3. O cliente deve ser capaz de solicitar acesso à uma das salas de bate papo.
4. O servidor deve manter uma lista dos membros da sala.
5. O cliente deve ser capaz de enviar mensagens para a sala.
6. O cliente deve ser capaz de sair da sala de bate papo.

### Como executar o programa?

Para utilizar o chat deve-se executar as seguintes classes na ordem especificada abaixo:
>```bash
> 1. Server
>```

>```bash
> 2. Client
>```

Ambas classes encontram-se no **package app**.

### Comandos

Para acessar as funcionalidades do Chat basta digitar um dos comandos abaixo:

| Comando                  | Como acessar                                                                     |
|--------------------------|----------------------------------------------------------------------------------|
| Criar uma nova sala      | /criar + nome da sala + endereço IP (valores entre 224.0. 0.0 e 239.255.255.255) |
| Listar salas disponíveis | /salas                                                                           |
| Ingressar em uma sala    | /ingressar + nome da sala                                                        |
| Sair de uma sala         | /sair                                                                            |
| Desconectar do servidor  | /desconectar                                                                     |
| Ver membros do chat      | /membros                                                                         |
| Ver comandos disponíveis | /ajuda                                                                           |

### Diagrama 

Visão geral das classes, métodos, atributos e propriedades

<img width="786" alt="diagrama" src="https://user-images.githubusercontent.com/44076017/133003248-bb25cdb9-a17d-4df6-b56d-aee8ef841ca4.png">

[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=446507&assignment_repo_type=GroupAssignmentRepo)
