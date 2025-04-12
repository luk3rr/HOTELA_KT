# Sistema de hotel
- Back-end: Java 21
- Testes unitários: JUnit e Mockito
- Framework: Spring
- Comunicação back-front: API Rest

# Testando endpoint de exemplo
Este exemplo expõe uma API REST simples com dois endpoints principais para criação e consulta de objetos `Example`.

## Endpoints
- `POST /example/examples/create` – Cria um novo `Example`
- `GET /example/examples/{id}` – Busca um `Example` pelo seu UUID

## Como bater no endpoint?
### 1. Criar um `Example` com UUID customizado (cliente define o ID)

```bash
curl -X POST http://localhost:8080/example/examples/create \
  -H "Content-Type: application/json" \
  -d '{"id": "123e4567-e89b-12d3-a456-426614174000", "name": "Com UUID custom"}'
```
A resposta esperada:
```text
"123e4567-e89b-12d3-a456-426614174000"
```

#### 1.1 Buscando o `Example` criado
```bash
curl -X GET http://localhost:8080/example/examples/123e4567-e89b-12d3-a456-426614174000
```
A resposta esperada:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Com UUID custom"
}
```

### 2. Criar um `Example` com UUID gerado automaticamente (cliente não define o ID)

```bash
curl -X POST http://localhost:8080/example/examples/create \
  -H "Content-Type: application/json" \
  -d '{"name": "Com UUID gerado"}'
```

A resposta esperada deve conter o UUID gerado automaticamente. Você pode aplicar o mesmo processo de busca do exemplo `1.1`, substituindo o UUID gerado no endpoint de busca.
