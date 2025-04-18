# Payment
Endpoints para gerenciamento de pagamentos de reservas feitas por clientes.

---

## Endpoints

### GET `/payment/{id}`

**Descrição:** Retorna os detalhes de um pagamento pelo seu ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição             |
|-----------|------|-----------------------|
| `id`      | UUID | ID único do pagamento |

**Resposta de sucesso (`200 OK`):**

```json
{
  "booking_id": "a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89",
  "amount": 250.00,
  "method": "CREDIT_CARD",
  "status": "COMPLETED",
  "created_at": "2025-04-17T15:00:00Z"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "600",
  "message": "Payment with id {id} not found"
}
```

---

### POST `/payment/create`

**Descrição:** Realiza um novo pagamento para uma reserva existente.

**Body JSON:**

```json
{
  "booking_id": "a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89",
  "amount": 250.00,
  "method": "CREDIT_CARD"
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "{generated_uuid}",
  "message": "Payment created successfully",
  "paid_at": "2025-04-17T15:23:48Z"
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

### PUT `/payment/{id}/update`

**Descrição:** Atualiza dados de um pagamento (ex: status ou método).

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                  |
|-----------|------|----------------------------|
| `id`      | UUID | O ID único do pagamento    |

**Body JSON (exemplo):**

```json
{
  "status": "REFUNDED"
}
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Payment updated successfully"
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
  "code": "600",
  "message": "Payment with id {id} not found"
}
```

---

### DELETE `/payment/{id}/delete`

**Descrição:** Remove um pagamento do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                   |
|-----------|------|-----------------------------|
| `id`      | UUID | O ID do pagamento a excluir |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Payment deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "600",
  "message": "Payment with id {id} not found"
}
```

---

## Exemplos com `curl`

### Criar pagamento

```bash
curl -X POST http://localhost:8080/payment/create \
     -H "Content-Type: application/json" \
     -d '{
           "booking_id": "a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89",
           "amount": 250.00,
           "method": "CREDIT_CARD"
         }'
```

### Atualizar pagamento

```bash
curl -X PUT http://localhost:8080/payment/b1234567-89ab-cdef-0123-456789abcdef/update \
     -H "Content-Type: application/json" \
     -d '{"status": "REFUNDED"}'
```

### Excluir pagamento

```bash
curl -X DELETE http://localhost:8080/payment/b1234567-89ab-cdef-0123-456789abcdef/delete
```