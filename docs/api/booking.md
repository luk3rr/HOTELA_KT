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
  "customerId": "e2f9c4d1-8e2e-4a3c-9111-4321abcd1234",
  "hotelId": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "roomId": "da3c9e2b-7d2a-4c7f-a3f4-123abc789000",
  "checkin": "2025-05-01T20:00:00Z",
  "checkout": "2025-05-05T12:00:00Z",
  "guests": 2,
  "status": "CONFIRMED",
  "notes": "Please prepare the room with extra pillows."
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

### PUT `/booking/update/{id}`

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