# Aplicação exemplo em Spring Boot

## Pré-requisitos

* JDK 11 ou superior instalado
* MySQL 8 ou superior instalado

## Vídeos explicativos

* [Parte 1](https://www.youtube.com/watch?v=A06XXUxjp9M) Configuração do projeto; Arquitetura de uma aplicação Spring; Controllers
* [Parte 2](https://www.youtube.com/watch?v=Sx5g3tCkljQ) Layouts
* [Parte 3](https://www.youtube.com/watch?v=SpcmL41yOa8) Formulários
* [Parte 4](https://www.youtube.com/watch?v=xr_mVfsWIKs) Base de Dados

## Passos a seguir

* Instalar MySQL e pôr a correr (anotar a password associada ao root!)
* Ligar o Intellij ao MySQL através do separador Database > New Data Source (MySQL), colocando as credencias do root. Se der erro de timezone, na opção Advanced, adicionar serverTimezone com o valor UTC.
* Criar um schema XXX com o nome da aplicação (ex: "gestao-tfc") e criar um utilizador XXX com o mesmo nome da aplicação e 
dar-lhe permissões sobre o schema criado no passo anterior

```
create database trabalho;

create user 'trabalho'@'localhost' identified by 'passwordtrabalho';

grant all privileges on trabalho.* to 'trabalho'@'localhost';
```

* Se necessário, alterar o src/main/resources/application.properties com o nome da BD, nome e pass do utilizador

## (Opcional) Autenticação por user/pass

* Indicar na classe WebSecurityConfiguration quais são os urls que devem estar atrás do login
* A aplicação passará a pedir login para esses urls. O login default é: user / 123

## (Opcional) Autenticação através do orcid

* Verificar se foram acrescentadas as dependências spring-boot-starter-security e spring-security-oauth2-client no pom.xml 
* Descomentar todas as propriedades relacionadas com oauth no application.properties
* Descomentar o @Configuration no OauthSecurityConfiguration e retirar o exclude que está no TrabalhoApplication
* Registar o cliente na sandbox seguindo
 [estes passos](https://info.orcid.org/documentation/api-tutorials/api-tutorial-read-data-on-a-record/#Get_some_client_credentials)
 Nota: registar com um email @mailinator.com pois a sandbox só envia emails para esse provider
* Atualizar as credenciais (clientId, clientSecret) no application.properties
