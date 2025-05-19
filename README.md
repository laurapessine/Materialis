# Materialis
**Materialis** é uma aplicação web desenvolvida em Java com Spring Boot, seguindo o padrão MVC, que permite a estudantes compartilhar e solicitar o empréstimo de recursos (livros, calculadoras, kits de eletrônica, etc.) de forma simples e segura. O sistema oferece:

* **Cadastro de Usuários** (papéis ADMIN e STUDENT) com autenticação e autorização via Spring Security.
* **Gerenciamento de Materiais**: CRUD completo, com múltiplas fotos, estado de conservação e categorização.
* **Busca e Filtragem**: listagem pública com filtros por categoria, palavra-chave e localização aproximada.
* **Solicitações de Empréstimo**: fluxo de pedido, aprovação/recusa, sugestões de datas alternativas e notificação por e-mail.
* **Agendamento de Empréstimos**: controle de conflitos de calendário, garantindo um empréstimo ativo por vez.
* **Avaliações Pós-Empréstimo**: rating por estrelas e comentários, promovendo confiança entre usuários.
* **Internacionalização (i18n)** em Português e Inglês, facilitando expansão para outras instituições.

## Tecnologias Principais

* **Backend:** Java, Spring Boot (MVC, Data JPA, Security, Mail)
* **Frontend:** Thymeleaf + JavaScript (ou framework leve como Vue.js/React)
* **Banco de Dados:** PostgreSQL (via Spring Data JPA)
* **Build & Deploy:** Maven, GitHub Actions (CI/CD)
* **Internacionalização:** Resource Bundles do Spring
* **Controle de Versão:** Git, GitHub

## Motivação e Objetivos
Este projeto visa fomentar a colaboração entre estudantes, reduzindo desperdício de recursos acadêmicos e promovendo a economia compartilhada no ambiente universitário. Além disso, consolida a aplicação prática de conceitos de engenharia de software, como arquitetura em camadas, segurança, tratamento de erros e testes automatizados.
