# Review
Endpoints para gerenciamento de avaliações feitas por clientes sobre hotéis.

---

## Endpoints

### GET `/review/{id}`

**Descrição:** Retorna os detalhes de uma avaliação pelo seu ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição             |
|-----------|------|-----------------------|
| `id`      | UUID | ID único da avaliação |

**Resposta de sucesso (`200 OK`):**

```json
{
  "hotel_id": "9f8e7d6c-5b4a-3210-1234-abcdefabcdef",
  "customer_id": "45f8e7d6c-5b4a-3210-1234-abcxefabckef",
  "rating": 4,
  "comment": "Hotel bem localizado e limpo.",
  "reviewed_at": "2025-04-17T15:30:00Z"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "600",
  "message": "Review with id {id} not found"
}
```

---

### POST `/review/create`

**Descrição:** Cria uma nova avaliação para um hotel.

**Body JSON:**

```json
{
  "hotel_id": "9f8e7d6c-5b4a-3210-1234-abcdefabcdef",
  "customer_id": "45f8e7d6c-5b4a-3210-1234-abcxefabckef",
  "rating": 4,
  "comment": "Hotel bem localizado e limpo."
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "{generated_uuid}",
  "message": "Review created successfully"
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

### PUT `/review/{id}/update`

**Descrição:** Atualiza uma avaliação existente (nota ou comentário).

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição             |
|-----------|------|-----------------------|
| `id`      | UUID | ID da avaliação alvo  |

**Body JSON (exemplo):**

```json
{
  "rating": 5,
  "comment": "Excelente atendimento!"
}
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Review updated successfully"
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
  "message": "Review with id {id} not found"
}
```

---

### DELETE `/review/{id}/delete`

**Descrição:** Remove uma avaliação do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição             |
|-----------|------|-----------------------|
| `id`      | UUID | ID da avaliação alvo  |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Review deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "600",
  "message": "Review with id {id} not found"
}
```