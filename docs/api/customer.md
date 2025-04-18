# Customer
Endpoints para gerenciamento de dados do cliente.

---

## Endpoints

### GET `/customer/{id}`

**Descrição:** Retorna os detalhes de um cliente pelo ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição           |
|-----------|------|---------------------|
| `id`      | UUID | ID único do cliente |

**Resposta de sucesso (`200 OK`):**

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "phone": "+55 11 98765-4321",
  "id_document": "123.456.789-00",
  "birth_date": "1990-05-15",
  "address": "Rua Exemplo, 123, São Paulo, SP"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "200",
  "message": "Customer with id {id} not found"
}
```

---

### POST `/customer/create`

**Descrição:** Cria um novo cliente no sistema.

**Body JSON:**

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "phone": "+55 11 98765-4321",
  "id_document": "123.456.789-00",
  "birth_date": "1990-05-15",
  "address": "Rua Exemplo, 123, São Paulo, SP"
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "{generated-uuid}",
  "message": "Customer created successfully"
}
```

**Resposta de erro (`400 Bad Request`):**

```json
{
  "code": "900",
  "message": "Invalid data provided"
}
```

---

### PUT `/customer/{id}/update`

**Descrição:** Atualiza os dados de um cliente existente.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                                |
|-----------|------|------------------------------------------|
| `id`      | UUID | O ID único do cliente a ser atualizado   |

**Body JSON:**

```json
{
  "name": "João Silva Filho",
  "email": "joao.silva@email.com",
  "phone": "+55 11 98765-4321",
  "id_document": "123.456.789-00",
  "birth_date": "1990-05-15",
  "address": "Rua Nova, 456, São Paulo, SP"
}
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Customer updated successfully"
}
```

**Resposta de erro (`400 Bad Request`):**

```json
{
  "code": "900",
  "message": "Invalid data provided"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "200",
  "message": "Customer with id {id} not found"
}
```

---

### DELETE `/customer/{id}/delete`

**Descrição:** Exclui um cliente do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                            |
|-----------|------|--------------------------------------|
| `id`      | UUID | O ID único do cliente a ser excluído |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Customer deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "200",
  "message": "Customer with id {id} not found"
}
```

---

## Exemplos com `curl`

### Criar cliente

```bash
curl -X POST http://localhost:8080/customer/create \
     -H "Content-Type: application/json" \
     -d '{"name": "João Silva", "email": "joao@email.com", "phone": "+55 11 98765-4321", "id_document": "123.456.789-00", "birth_date": "1990-05-15", "address": "Rua Exemplo, 123, São Paulo, SP"}'
```

### Atualizar cliente

```bash
curl -X PUT http://localhost:8080/customer/f75d8bfa-9e94-4e93-ae5b-11b8ecf022fa/update \
     -H "Content-Type: application/json" \
     -d '{"name": "João Silva Filho", "email": "joao.silva@email.com", "phone": "+55 11 98765-4321", "id_document": "123.456.789-00", "birth_date": "1990-05-15", "address": "Rua Nova, 456, São Paulo, SP"}'
```

### Excluir cliente

```bash
curl -X DELETE http://localhost:8080/customer/f75d8bfa-9e94-4e93-ae5b-11b8ecf022fa/delete
```