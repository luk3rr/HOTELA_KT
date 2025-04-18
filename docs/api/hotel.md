# Hotel
Endpoints para gerenciamento de hotéis vinculados a parceiros.

---

## Endpoints

### GET `/hotel/{id}`

**Descrição:** Retorna os detalhes de um hotel pelo seu ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição           |
|-----------|------|---------------------|
| `id`      | UUID | ID único do hotel   |

**Resposta de sucesso (`200 OK`):**

```json
{
  "id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1",
  "partner_id": "b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae",
  "name": "Hotel Bela Vista",
  "address": "Av. das Flores, 1000",
  "city": "São Paulo",
  "state": "SP",
  "zip_code": "01234-567",
  "phone": "(11) 1234-5678",
  "rating": 4.5,
  "description": "Hotel com vista panorâmica e piscina aquecida.",
  "website": "https://www.belavista.com",
  "latitude": -23.550520,
  "longitude": -46.633308
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "400",
  "message": "Hotel with id c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1 not found"
}
```

---

### POST `/hotel/create`

**Descrição:** Cria um novo hotel associado a um parceiro.

**Body JSON:**

```json
{
  "partner_id": "b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae",
  "name": "Hotel Bela Vista",
  "address": "Av. das Flores, 1000",
  "city": "São Paulo",
  "state": "SP",
  "zip_code": "01234-567",
  "phone": "(11) 1234-5678",
  "rating": 4.5,
  "description": "Hotel com vista panorâmica e piscina aquecida.",
  "website": "https://www.belavista.com",
  "latitude": -23.550520,
  "longitude": -46.633308
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1"
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

### PUT `/hotel/{id}/update`

**Descrição:** Atualiza os dados de um hotel.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                            |
|-----------|------|--------------------------------------|
| `id`      | UUID | O ID único do hotel a ser atualizado |

**Body JSON (exemplo de campos parciais):**

```json
{
  "name": "Hotel Bela Vista Premium",
  "rating": 4.8,
  "description": "Reformado em 2024 com novos quartos e spa.",
  "website": "https://www.belavistapremium.com"
}
```

> Campos não informados permanecem inalterados.

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Hotel updated successfully"
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
  "code": "400",
  "message": "Hotel with id {id} not found"
}
```

---

### DELETE `/hotel/{id}/delete`

**Descrição:** Remove um hotel do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                    |
|-----------|------|------------------------------|
| `id`      | UUID | O ID do hotel a ser removido |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Hotel deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "400",
  "message": "Hotel with id {id} not found"
}
```

---

## Exemplos com `curl`

### Criar hotel

```bash
curl -X POST http://localhost:8080/hotel/create \
     -H "Content-Type: application/json" \
     -d '{
           "partner_id": "b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae",
           "name": "Hotel Bela Vista",
           "address": "Av. das Flores, 1000",
           "city": "São Paulo",
           "state": "SP",
           "zip_code": "01234-567",
           "phone": "(11) 1234-5678",
           "rating": 4.5,
           "description": "Hotel com vista panorâmica e piscina aquecida.",
           "website": "https://www.belavista.com",
           "latitude": -23.550520,
           "longitude": -46.633308
         }'
```

### Atualizar hotel

```bash
curl -X PUT http://localhost:8080/hotel/c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1/update \
     -H "Content-Type: application/json" \
     -d '{"name": "Hotel Bela Vista Premium", "rating": 4.8}'
```

### Excluir hotel

```bash
curl -X DELETE http://localhost:8080/hotel/c2f8e6a9-4d3e-4c3f-92e0-9a14f88ec2e1/delete
```