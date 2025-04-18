# Booking  
Endpoints para gerenciamento de reservas feitas por clientes.

---

## Endpoints

### GET `/booking/{id}`

**Descrição:** Retorna os detalhes de uma reserva pelo seu ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição               |
|-----------|------|-------------------------|
| `id`      | UUID | ID único da reserva     |

**Resposta de sucesso (`200 OK`):**

```json
{
  "id": "a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89",
  "customer_id": "e2f9c4d1-8e2e-4a3c-9111-4321abcd1234",
  "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "room_id": "da3c9e2b-7d2a-4c7f-a3f4-123abc789000",
  "check_in": "2025-05-01",
  "check_out": "2025-05-05",
  "guests": 2,
  "status": "CONFIRMED",
  "created_at": "2025-04-17T14:22:00Z"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "600",
  "message": "Booking with id a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89 not found"
}
```

---

### POST `/booking/create`

**Descrição:** Cria uma nova reserva para um quarto em um hotel.

**Body JSON:**

```json
{
  "customer_id": "e2f9c4d1-8e2e-4a3c-9111-4321abcd1234",
  "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "room_id": "da3c9e2b-7d2a-4c7f-a3f4-123abc789000",
  "check_in": "2025-05-01",
  "check_out": "2025-05-05",
  "guests": 2
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "{generated_uuid}",
  "message": "Booking created successfully"
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

### PUT `/booking/{id}/update`

**Descrição:** Atualiza os dados de uma reserva.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                            |
|-----------|------|--------------------------------------|
| `id`      | UUID | O ID único da reserva a ser atualizada |

**Body JSON (exemplo de campos parciais):**

```json
{
  "check_out": "2025-05-06",
  "guests": 3
}
```

> Campos não informados permanecem inalterados.

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Booking updated successfully"
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
  "message": "Booking with id {id} not found"
}
```

---

### DELETE `/booking/{id}/delete`

**Descrição:** Remove uma reserva do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                      |
|-----------|------|--------------------------------|
| `id`      | UUID | O ID da reserva a ser removida |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Booking deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "600",
  "message": "Booking with id {id} not found"
}
```

---

## Exemplos com `curl`

### Criar reserva

```bash
curl -X POST http://localhost:8080/booking/create \
     -H "Content-Type: application/json" \
     -d '{
           "customer_id": "e2f9c4d1-8e2e-4a3c-9111-4321abcd1234",
           "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
           "room_id": "da3c9e2b-7d2a-4c7f-a3f4-123abc789000",
           "check_in": "2025-05-01",
           "check_out": "2025-05-05",
           "guests": 2
         }'
```

### Atualizar reserva

```bash
curl -X PUT http://localhost:8080/booking/a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89/update \
     -H "Content-Type: application/json" \
     -d '{"check_out": "2025-05-06", "guests": 3}'
```

### Excluir reserva

```bash
curl -X DELETE http://localhost:8080/booking/a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89/delete
```