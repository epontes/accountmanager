# Tutorial para Rodar a Aplicação

## Pré-requisitos

Antes de iniciar, certifique-se de ter os seguintes softwares instalados em sua máquina:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Passos para Rodar a Aplicação

1. **Clone o Repositório**

   Primeiro, clone o repositório para sua máquina local:

   ```sh
   git clone https://github.com/epontes/accountmanager.git

2. **Navegue até o seu Repositório**
   ```sh
     docker-compose up --build
3. **Acesso ao Swagger**
   -[Swagger] (http://localhost:6868/swagger-ui/index.html#/)




# Autenticação

Obtenha um Token de Acesso

Para obter um token, envie uma solicitação para o endpoint /auth/token com as seguintes credenciais:

Username: desafioapp
Password: desafio123
Você pode usar o Swagger UI para obter o token. No Swagger UI, navegue até o endpoint /auth/token e forneça o nome de usuário e a senha.

Use o Token para Requisições

Após obter o token, você deve adicioná-lo ao cabeçalho de autorização para fazer chamadas aos serviços da aplicação. No Swagger UI, clique no botão "Authorize" e insira o token gerado.
