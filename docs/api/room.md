# Room
Endpoints para gerenciamento de quartos vinculados a um hotel.

---

## Endpoints

### GET `/room/{id}`

**Descrição:** Retorna os detalhes de um quarto pelo seu ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição          |
|-----------|------|--------------------|
| `id`      | UUID | ID único do quarto |

**Resposta de sucesso (`200 OK`):**

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-1234567890ef",
  "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "number": "101",
  "floor": 1,
  "type": "Deluxe",
  "price": 350.00,
  "capacity": 2,
  "status": 1,
  "description": "Quarto com vista para o mar, cama king size e ar-condicionado."
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "500",
  "message": "Room with id a1b2c3d4-e5f6-7890-abcd-1234567890ef not found"
}
```

---

### POST `/room/create`

**Descrição:** Cria um novo quarto associado a um hotel.

**Body JSON:**

```json
{
  "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "number": "101",
  "floor": 1,
  "type": "Deluxe",
  "price": 350.00,
  "capacity": 2,
  "status": 1,
  "description": "Quarto com vista para o mar, cama king size e ar-condicionado."
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "{generated_uuid}",
  "message": "Room created successfully"
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

### PUT `/room/{id}/update`

**Descrição:** Atualiza os dados de um quarto.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                              |
|-----------|------|----------------------------------------|
| `id`      | UUID | O ID único do quarto a ser atualizado  |

**Body JSON (exemplo de campos parciais):**

```json
{
  "price": 390.00,
  "description": "Agora com frigobar incluso e TV 55 polegadas."
}
```

> Campos não informados permanecem inalterados.

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Room updated successfully"
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
  "code": "500",
  "message": "Room with id {id} not found"
}
```

---

### DELETE `/room/{id}/delete`

**Descrição:** Remove um quarto do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                     |
|-----------|------|-------------------------------|
| `id`      | UUID | O ID do quarto a ser removido |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Room deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "500",
  "message": "Room with id {id} not found"
}
```

---

## Exemplos com `curl`

### Criar quarto

```bash
curl -X POST http://localhost:8080/room/create \
     -H "Content-Type: application/json" \
     -d '{
           "hotel_id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
           "number": "101",
           "floor": 1,
           "type": "Deluxe",
           "price": 350.00,
           "capacity": 2,
           "status": 1,
           "description": "Quarto com vista para o mar, cama king size e ar-condicionado."
         }'
```

### Atualizar quarto

```bash
curl -X PUT http://localhost:8080/room/a1b2c3d4-e5f6-7890-abcd-1234567890ef/update \
     -H "Content-Type: application/json" \
     -d '{"price": 390.00, "description": "Agora com frigobar incluso e TV 55 polegadas."}'
```

### Excluir quarto

```bash
curl -X DELETE http://localhost:8080/room/a1b2c3d4-e5f6-7890-abcd-1234567890ef/delete
```