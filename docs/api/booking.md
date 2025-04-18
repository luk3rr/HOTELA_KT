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
  "customer_id": "e2f9c4d1-8e2e-4a3c-9111-4321abcd1234",
  "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "room_id": "da3c9e2b-7d2a-4c7f-a3f4-123abc789000",
  "booked_at": "2025-04-17T14:22:00Z",
  "checkin": "2025-05-01T20:00:00Z",
  "checkout": "2025-05-05T12:00:00Z",
  "guests": 2,
  "status": "CONFIRMED"
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

### POST `/booking/create`

**Descrição:** Cria uma nova reserva para um quarto em um hotel.

**Body JSON:**

```json
{
  "customer_id": "e2f9c4d1-8e2e-4a3c-9111-4321abcd1234",
  "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "room_id": "da3c9e2b-7d2a-4c7f-a3f4-123abc789000",
  "checkin": "2025-05-01T20:00:00Z",
  "checkout": "2025-05-05T12:00:00Z",
  "guests": 2
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "{generated_uuid}",
  "message": "Booking created successfully",
  "booked_at": "2025-04-17T14:22:00Z",
  "status": "CONFIRMED"
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

| Parâmetro | Tipo | Descrição                              |
|-----------|------|----------------------------------------|
| `id`      | UUID | O ID único da reserva a ser atualizada |

**Body JSON (exemplo de campos parciais):**

```json
{
  "check_out": "2025-05-06T15:00:00Z",
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

### **PUT** `/booking/{id}/cancel`

**Descrição:** Cancela uma reserva existente.  
Em vez de excluir a reserva, atualiza o status dela para `"CANCELLED"`

---

#### **Parâmetro de caminho**

| Parâmetro | Tipo | Descrição                     |
|-----------|------|-------------------------------|
| `id`      | UUID | ID da reserva a ser cancelada |

---

#### **Resposta de sucesso (`200 OK`)**

```json
{
  "message": "Booking cancelled successfully",
  "status": "CANCELLED"
}
```

---

**Resposta de erro (`404 Not Found`)**

```json
{
  "code": "600",
  "message": "Booking with id {id} not found"
}
```

---

**Resposta de erro (`409 Conflict`)**

```json
{
  "code": "601",
  "message": "Booking with id {id} already cancelled"
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
           "booked_at": "2025-04-17T14:22:00Z",
           "checkin": "2025-05-01T20:00:00Z",
           "checkout": "2025-05-05T12:00:00Z",
           "guests": 2,
           "status": "CONFIRMED"
         }'
```

### Atualizar reserva

```bash
curl -X PUT http://localhost:8080/booking/a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89/update \
     -H "Content-Type: application/json" \
     -d '{"check_out": "2025-05-06T19:00:00Z", "guests": 3}'
```

### Excluir reserva

```bash
curl -X DELETE http://localhost:8080/booking/a7d3b2c1-5c4e-4f5d-91b0-1234e56c7d89/delete
```

---

#### Cancelar uma reserva

```bash
curl -X PUT http://localhost:8080/booking/b2a1c3d4-e5f6-7890-abcd-1234567890ef/cancel
```
