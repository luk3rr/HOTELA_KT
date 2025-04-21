# Sistema de hotel
- **Back-end:** Kotlin
- **Testes unitários:** Kotest
- **Framework:** Spring  
- **Comunicação back-front:** API Restful

## Como executar o projeto com Gradle

Certifique-se de ter o **Java 21** instalado.

### 1. Clonar o repositório
```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

### 2. Gere uma chave compatível com JWT
```bash
echo "JWT_SECRET=$(openssl rand -base64 32)" >> .env
```

Essa chave será usada para assinar os tokens JWT. Você pode verificar se ela foi criada corretamente com o comando:
```bash
cat .env
```

### 3. Gere uma senha para o banco de dados
```bash
echo "POSTGRES_PASSWORD=$(openssl rand -base64 12)" >> .env
```

Essa senha será usada para acessar o banco de dados PostgreSQL. Você pode verificar se ela foi criada corretamente com o comando:
```bash
cat .env
```

### 4. Iniciar o container com o banco de dados
Certifique-se de ter o **Docker** instalado e em execução.

Para iniciar o banco de dados PostgreSQL, execute o seguinte comando na raiz do projeto:
```bash
docker compose up -d
```

Isso iniciará um container com o banco de dados PostgreSQL. Você pode verificar se o container está em execução com o comando:
```bash
docker compose ps
```

### 5. Executar o projeto
Primeiro, leia o arquivo `.env` para carregar as variáveis de ambiente:
```bash
export $(cat .env | xargs)
```

Em seguida, execute o seguinte comando para compilar e iniciar a aplicação:
```bash
./gradlew bootRun
```
Ou, no Windows:
```bash
gradlew.bat bootRun
```
A aplicação será iniciada na porta padrão 8080.

### 6. Rodar os testes
Para executar os testes automatizados:

```bash
./gradlew test
```

### 6. Testando endpoint de exemplo
Você pode utilizar a [collection](docs/postman) do [Postman](https://www.postman.com/) criada para facilitar os testes dos endpoints da API.
Para isso, basta importar a collection e o environment no Postman.

## Documentação das APIs
A documentação dos endpoints pode ser encontrada em [docs/api](docs/api/readme.md)

Com a aplicação em execução, você pode acessar a documentação Swagger em:
```
http://localhost:8080/swagger-ui/index.html
```

## Padrões de desenvolvimento
Antes de submeter um PR, verifique se o código segue os padrões de desenvolvimento estabelecidos.
Execute o comando abaixo para verificar a formatação do código:
```bash
./gradlew spotlessCheck 
```

Caso o código não esteja formatado, você pode corrigi-lo automaticamente com:
```bash
./gradlew spotlessApply
```

## Sonarqube
Para verificar a qualidade do código, você pode acessar o sonarcloud do projeto:
```
https://sonarcloud.io/project/overview?id=luk3rr_HOTELA_KT
```