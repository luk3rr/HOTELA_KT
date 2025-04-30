# Room
Endpoints para gerenciamento de quartos vinculados a um hotel. Requer autenticação via JWT.

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
  "id": "29523f15-2e03-47a3-947e-54eb3df6941e",
  "hotelId": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "number": "101",
  "floor": 1,
  "type": "Deluxe",
  "price": 350.00,
  "capacity": 2,
  "status": "AVAILABLE",
  "description": "Quarto com vista para o mar, cama king size e ar-condicionado."
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

### POST `/room/create`

**Descrição:** Cria um novo quarto associado a um hotel.

**Body JSON:**

```json
{
  "hotelId": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "number": "101",
  "floor": 1,
  "type": "Deluxe",
  "price": 350.00,
  "capacity": 2,
  "status": "AVAILABLE",
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

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "400",
  "message": "Hotel with id {id} not found"
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

### PUT `/room/update/{id}`

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

### DELETE `/room/delete/{id}`

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