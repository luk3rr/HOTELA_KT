# Customer
Endpoints para gerenciamento de dados do cliente. Requer autenticação via JWT.

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
  "id": "a8787188-53e4-4a51-88d2-48c04f7afdfc",
  "name": "João Silva",
  "email": "joao@email.com",
  "phone": "+55 11 98765-4321",
  "idDocument": "123.456.789-00",
  "birthDate": "1990-05-15",
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

### PUT `/customer/update`

**Descrição:** Atualiza os dados de um cliente existente. Usa os dados no JWT para localizar o cliente.

**Body JSON:**

```json
{
  "name": "João Silva Filho",
  "phone": "+55 21 98765-4325",
  "idDocument": "123.456.789-10",
  "birthDate": "1990-05-16",
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

Caso os dados estejam inválidos ou incompletos.

```json
{
  "code": "900",
  "message": "Invalid data provided"
}
```

Pode ocorrer também se a data de nascimento não for válida. Nesse caso, o próprio protocolo HTTP retornará um erro `400 Bad Request` ao tentar fazer o cast para `LocalDate`.

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "106",
  "message": "Customer auth with id {id} not found"
}
```