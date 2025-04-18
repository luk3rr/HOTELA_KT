# Cliente
Endpoints para gerenciamento de dados do cliente.

---

## Endpoints

### GET `/client/{id}`

**Descrição:** Retorna os detalhes de um cliente pelo ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição           |
|-----------|------|---------------------|
| `id`      | UUID | ID único do cliente |

**Resposta de sucesso (`200 OK`):**

```json
{
  "id": "f75d8bfa-9e94-4e93-ae5b-11b8ecf022fa",
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "+55 11 98765-4321",
  "documento_identificacao": "123.456.789-00",
  "data_nascimento": "1990-05-15",
  "endereco": "Rua Exemplo, 123, São Paulo, SP"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "200",
  "message": "Client not found"
}
```

---

### POST `/client/create`

**Descrição:** Cria um novo cliente no sistema.

**Body JSON:**

```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "+55 11 98765-4321",
  "documento_identificacao": "123.456.789-00",
  "data_nascimento": "1990-05-15",
  "endereco": "Rua Exemplo, 123, São Paulo, SP"
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "f75d8bfa-9e94-4e93-ae5b-11b8ecf022fa"
}
```

**Resposta de erro (`400 Bad Request`):**

```json
{
  "code": "201",
  "message": "Invalid data provided"
}
```

---

### PUT `/client/{id}/update`

**Descrição:** Atualiza os dados de um cliente existente.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                               |
|-----------|------|-----------------------------------------|
| `id`      | UUID | O ID único do cliente a ser atualizado |

**Body JSON:**

```json
{
  "nome": "João Silva Filho",
  "email": "joao.silva@email.com",
  "telefone": "+55 11 98765-4321",
  "documento_identificacao": "123.456.789-00",
  "data_nascimento": "1990-05-15",
  "endereco": "Rua Nova, 456, São Paulo, SP"
}
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Client updated successfully"
}
```

**Resposta de erro (`400 Bad Request`):**

```json
{
  "code": "201",
  "message": "Invalid data provided"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "200",
  "message": "Client not found"
}
```

---

### DELETE `/client/{id}/delete`

**Descrição:** Exclui um cliente do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                            |
|-----------|------|--------------------------------------|
| `id`      | UUID | O ID único do cliente a ser excluído |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Client deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "200",
  "message": "Client not found"
}
```

---

## Exemplos com `curl`

### Criar cliente

```bash
curl -X POST http://localhost:8080/client/create \
     -H "Content-Type: application/json" \
     -d '{"nome": "João Silva", "email": "joao@email.com", "telefone": "+55 11 98765-4321", "documento_identificacao": "123.456.789-00", "data_nascimento": "1990-05-15", "endereco": "Rua Exemplo, 123, São Paulo, SP"}'
```

### Atualizar cliente

```bash
curl -X PUT http://localhost:8080/client/f75d8bfa-9e94-4e93-ae5b-11b8ecf022fa/update \
     -H "Content-Type: application/json" \
     -d '{"nome": "João Silva Filho", "email": "joao.silva@email.com", "telefone": "+55 11 98765-4321", "documento_identificacao": "123.456.789-00", "data_nascimento": "1990-05-15", "endereco": "Rua Nova, 456, São Paulo, SP"}'
```

### Excluir cliente

```bash
curl -X DELETE http://localhost:8080/client/f75d8bfa-9e94-4e93-ae5b-11b8ecf022fa/delete
```