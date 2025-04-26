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
    "id": "b9604b6f-2ce9-437b-8682-970414f7cd70",
    "partnerId": "285fbda5-3d28-47c4-91ee-d31268332696",
    "name": "Hotel das Palmeiras",
    "address": "Rua Exemplo, 123",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "12345-678",
    "phone": "11999999999",
    "rating": 4.5,
    "description": "Hotel charmoso com café incluso",
    "website": "http://hotelpalmeiras.com",
    "latitude": -23.550520,
    "longitude": -46.633308
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

### GET `/hotel/partner/{partnerId}`

**Descrição:** Retorna uma lista de hotéis associados a um parceiro específico.

**Parâmetro de caminho:**

| Parâmetro   | Tipo | Descrição                                     |
|-------------|------|-----------------------------------------------|
| `partnerId` | UUID | ID único do parceiro responsável pelos hóteis |

**Resposta de sucesso (`200 OK`):**

```json
[
  {
    "id": "dd263f0d-6246-4b2b-a593-931913dca2de",
    "partnerId": "69bc0432-647d-417b-bbd4-bf0439bc53c4",
    "name": "Hotel das Palmeiras",
    "address": "Rua Exemplo, 123",
    "city": "São Paulo",
    "state": "SP",
    "zipCode": "12345-678",
    "phone": "11999999999",
    "rating": 4.5,
    "description": "Hotel charmoso com café incluso",
    "website": "http://hotelpalmeiras.com",
    "latitude": -23.550520,
    "longitude": -46.633308
  },
  {
    "id": "ed7e6d49-4374-4f6f-aae7-ec4cd156c373",
    "partnerId": "69bc0432-647d-417b-bbd4-bf0439bc53c4",
    "name": "Pousada Mar Azul",
    "address": "Avenida das Gaivotas, 456",
    "city": "Rio de Janeiro",
    "state": "RJ",
    "zipCode": "22040-002",
    "phone": "21988888888",
    "rating": 4.7,
    "description": "Pousada aconchegante perto da praia",
    "website": "http://pousadamarazul.com",
    "latitude": -22.971964,
    "longitude": -43.182543
  }
]
```

Se não houver hotéis associados ao parceiro, a resposta será uma lista vazia.
```json
[]
```

---

### POST `/hotel/create`

**Descrição:** Cria um novo hotel associado a um parceiro.

**Body JSON:**

```json
{
  "name": "Hotel das Palmeiras",
  "address": "Rua Exemplo, 123",
  "city": "São Paulo",
  "state": "SP",
  "zipCode": "12345-678",
  "phone": "11999999999",
  "rating": 4.5,
  "description": "Hotel charmoso com café incluso",
  "website": "http://hotelpalmeiras.com",
  "latitude": -23.55052,
  "longitude": -46.633308
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "{generated_uuid}",
  "message": "Hotel created successfully"
}
```

**Resposta de erro (`401 Unauthorized`):**
Caso o parceiro não esteja autenticado ou autorizado.

```json
{
  "code": "103",
  "message": "Access denied"
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

### PUT `/hotel/update/{id}`

**Descrição:** Atualiza os dados de um hotel.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                            |
|-----------|------|--------------------------------------|
| `id`      | UUID | O ID único do hotel a ser atualizado |

**Body JSON (exemplo de campos parciais):**

```json
{
  "name": "Hotel Bela Vista Premium",
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
           "name": "Hotel Bela Vista",
           "address": "Av. das Flores, 1000",
           "city": "São Paulo",
           "state": "SP",
           "zipCode": "01234-567",
           "phone": "(11) 1234-5678",
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
     -d '{
           "name": "Hotel Bela Vista Premium",
           "description": "Reformado em 2024 com novos quartos e spa.",
           "website": "https://www.belavistapremium.com"
         }'
```