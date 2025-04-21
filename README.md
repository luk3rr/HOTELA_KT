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
export JWT_SECRET=$(openssl rand -base64 64)
```

Essa chave será usada para assinar os tokens JWT. Você pode verificar se a variável foi criada corretamente com o comando:
```bash
echo $JWT_SECRET
```

### 3. Executar o projeto
Você pode iniciar a aplicação com o comando:

```bash
./gradlew bootRun
```
Ou, no Windows:
```bash
gradlew.bat bootRun
```
A aplicação será iniciada na porta padrão 8080.

### 4. Rodar os testes
Para executar os testes automatizados:

bash
Copiar
Editar
./gradlew test

### 5. Testando endpoint de exemplo
Este exemplo expõe uma API REST simples com dois endpoints principais para criação e consulta de objetos `Example`.

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