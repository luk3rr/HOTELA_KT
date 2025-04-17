# Sistema de hotel
- **Back-end:** Java 21  
- **Testes unitários:** JUnit e Mockito  
- **Framework:** Spring  
- **Comunicação back-front:** API Rest  

## Como executar o projeto com Gradle

Certifique-se de ter o **Java 21** instalado.

### 1. Clonar o repositório
```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

### 2. Executar o projeto
Você pode iniciar a aplicação com o comando:

```bash
./gradlew bootRun
```
Ou, no Windows:
```bash
gradlew.bat bootRun
```
A aplicação será iniciada na porta padrão 8080.

### 3. Rodar os testes
Para executar os testes automatizados:

bash
Copiar
Editar
./gradlew test

### 4. Testando endpoint de exemplo
Este exemplo expõe uma API REST simples com dois endpoints principais para criação e consulta de objetos `Example`.

## Documentação das APIs
A documentação dos endpoints pode ser encontrada em [docs/api](docs/api/readme.md)
