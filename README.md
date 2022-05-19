*Universidade Lusófona de Humanidades e Tecnologias*

# Site DEISI - Backend

## Pré-requisitos

- JDK 11 ou superior instalado. [Download](https://www.oracle.com/pt/java/technologies/javase/jdk11-archive-downloads.html) da versão atual.
- MySQL 8 ou superior instalado. [Download](https://dev.mysql.com/downloads/mysql/) da versão atual.
- IntelliJ Ultimate ou IntelliJ Community([Download](https://www.jetbrains.com/idea/download/#section=mac) + SGBD (p.e DBeaver - [Download](https://dbeaver.io/download/).
- Git. [Download](https://git-scm.com/) da versão atual.
- Browser

### Instalação

1. Clonar o repositório:

   ```bash
   - git clone https://github.com/borisdvlpr/deisi_backend
   ```
   
2. Instalar MySQL e pôr a correr (anotar a password associada ao root!)

3. Ligação à base de dados:
  - IntelliJ Ultimate - Ligar o Intellij ao MySQL através do separador Database > New Data Source (MySQL), colocando as credencias do root. Se der erro de timezone, na opção Advanced, adicionar serverTimezone com o valor UTC.
  - 
  - DBeaver - Criar uma nova ligação à base de dados através do separador Database > New Database Connection (MySQL), colocando as credencias do root.

4. Criar um schema com o nome `deisi_backend`, criar um utilizador com o mesmo nome do schema e com a palavra passe `deisi_2022` e dar-lhe permissões sobre o schema.

   ```
   create database deisi_backend;

   create user 'deisi_backend'@'localhost' identified by 'deisi_backend';

   grant all privileges on deisi_backend.* to 'deisi_backend'@'localhost';
   ```

* Se necessário, alterar o src/main/resources/application.properties com o nome da BD, nome e pass do utilizador

5. Correr a aplicação no IntelliJ

6. Abrir o `localhost` no browser, utilizando a porta 8080, para a diretoria do backoffice, <http://localhost:8080/backoffice>

7. Realizar o login com o utilizador `user1` e password `123`

NOTA: Após a criação da base de dados, esta não tem quaisquer dados para visualizar. Para tal, é necessário correr o script `initial-files.sql` de modo a adicionar os dados à base de dados.
