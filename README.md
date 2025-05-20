# Jobs Memory - Backend 🚀

## Sobre o Jobs Memory
Bem-vindo(a) ao backend do **Jobs Memory**! Sua bússola no universo da aplicações em empregos. 🧭

O Jobs Memory nasceu da necessidade de simplificar e organizar o processo, muitas vezes caótico, de se candidatar a vagas. 

Nosso objetivo é que você, candidato(a), tenha total controle e clareza sobre cada passo da sua jornada profissional.

Este repositório contém o código-fonte do backend, construído em Java, que serve como o cérebro da aplicação, gerenciando todos os dados e a lógica/regra de negócios.

### Nossa Missão
Auxiliar candidatos(as) com uma ferramenta intuitiva e poderosa para gerenciar suas candidaturas, maximizando suas chances de sucesso e reduzindo o estresse da busca por uma nova oportunidade. 🎯

## Como Funciona?
O Jobs Memory permite que os usuários:
📝 Registrem todas as suas candidaturas em um só lugar.
📊 Acompanhem o status e a etapa de cada processo seletivo.
⏰ Definam lembretes para follow-ups, entrevistas e prazos.
📈 Visualizem estatísticas e tendências sobre suas aplicações.

O backend é responsável por:
*   Gerenciar a autenticação e segurança dos usuários.
*   Processar e armazenar os dados das candidaturas.
*   Fornecer os dados para o sistema de calendário e lembretes.
*   Calcular e disponibilizar as estatísticas para o dashboard.

## Tecnologias Utilizadas
*   **Linguagem Principal:** Java 17
*   **Framework Principal:** Spring Boot 3x
*   **Gerenciador de Dependências:** Maven
*   **Banco de Dados:** PostgreSQL
*   **Containerização:** Docker (Utilizado para o deploy)
*   **Frontend:** Angular

## Arquitetura e Deploy
*   **Backend:** API RESTful desenvolvida em Java com Spring Boot.
*   **Frontend:** Aplicação Single Page Application (SPA) desenvolvida com Angular.
*   **Deploy do Backend:** Containerizado com Docker e hospedado no Render.
*   **Deploy do Frontend:** Hospedado na Vercel.

A aplicação completa pode ser acessada em: **https://login-angular-memory.vercel.app/login** 

Importante: Como está hospedado no Render em uma versão gratuita, é necessario aguardar 1 minuto após a tentativa de login, pois a maquina fica em Standby.

## Principais Funcionalidades da API (Backend)
O backend expõe uma API RESTful para comunicação com o frontend. Alguns dos principais recursos incluem:

*   **Autenticação de Usuários:**
    *   Endpoints para registro (`/api/auth/register`) e login (`/api/auth/login`).
*   **Gerenciamento de Candidaturas (CRUD):**
    *   `GET /api/applications`: Listar candidaturas.
    *   `POST /api/applications`: Criar nova candidatura.
    *   `PUT /api/applications/{id}`: Atualizar candidatura.
    *   `DELETE /api/applications/{id}`: Remover candidatura.
*   **Gerenciamento de Lembretes e Eventos.**
*   **Fornecimento de Dados para Estatísticas do Dashboard.**

*(Para uma documentação mais detalhada da API, pode-se consultar nosso swagger ou o código-fonte).*


## Contato
*   **LinkedIn:** https://www.linkedin.com/in/geraldoaafilho/
*   **Repositório:** [https://github.com/Gerfy1/JobsMemory](https://github.com/Gerfy1/JobsMemory)
