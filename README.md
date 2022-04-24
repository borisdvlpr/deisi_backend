*Universidade Lusófona de Humanidades e Tecnologias*

# Site DEISI - Backend

## Pré-requisitos

- JDK 11 ou superior instalado. [Download](https://nodejs.org/en/download/) da versão atual.
- MySQL 8 ou superior instalado. [Download](https://nodejs.org/en/download/) da versão atual.
- IntelliJ Ultimate([Download](https://nodejs.org/en/download/)) ou IntelliJ Community([Download](https://nodejs.org/en/download/)) + SGBD (p.e DBeaver - [Download](https://nodejs.org/en/download/)).
- Git. [Download](https://git-scm.com/) da versão atual.
- Browser

### Instalação

1. Clonar o repositório:

   ```bash
   - git clone https://github.com/borisdvlpr/deisi_backend
   ```
   
2. Instalar MySQL e pôr a correr (anotar a password associada ao root!)
3. 
4. Ligação à base de dados:
  - IntelliJ Ultimate - Ligar o Intellij ao MySQL através do separador Database > New Data Source (MySQL), colocando as credencias do root. Se der erro de timezone, na opção Advanced, adicionar serverTimezone com o valor UTC.
  - 
  - DBeaver - Criar uma nova ligação à base de dados através do separador Database > New Database Connection (MySQL), colocando as credencias do root.
  - 
5. Criar um schema XXX com o nome da aplicação (ex: "gestao-tfc") e criar um utilizador XXX com o mesmo nome da aplicação e 
dar-lhe permissões sobre o schema criado no passo anterior

   ```
   create database trabalho;

   create user 'trabalho'@'localhost' identified by 'passwordtrabalho';

   grant all privileges on trabalho.* to 'trabalho'@'localhost';
   ```

* Se necessário, alterar o src/main/resources/application.properties com o nome da BD, nome e pass do utilizador

6. Correr a aplicação no IntelliJ

7. Abrir o `localhost` no browser, utilizando a porta 8080, para uma das diretorias disponíveis, p.e. <http://localhost:8080/students/list>
