# Example
Endpoint para criar e buscar objetos do tipo `Example`.

---

## Endpoints

### GET `/example/examples/{id}`

**Descrição:** Busca um objeto `Example` pelo seu ID.

**Parâmetros de path:**

| Parâmetro | Tipo | Descrição        |
|-----------|------|------------------|
| `id`      | UUID | ID do exemplo    |

**Resposta de sucesso (`200 OK`):**

```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "name": "Exemplo 1"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "000",
  "message": "Example with id f47ac10b-58cc-4372-a567-0e02b2c3d479 not found"
}
```

---

### POST `/example/examples/create`

**Descrição:** Cria um novo objeto `Example`.

**Body JSON:**

```json
{
  "name": "Novo exemplo"
}
```

> O campo `id` será gerado automaticamente se não for informado.

**Resposta de sucesso (`201 Created`):**

```json
"f47ac10b-58cc-4372-a567-0e02b2c3d479"
```

---

## Modelo de Dados: `Example`

| Campo | Tipo  | Obrigatório | Descrição            |
|-------|-------|-------------|------------------------|
| `id`  | UUID  | Não         | Identificador único   |
| `name`| String| Sim         | Nome do exemplo       |

---

## Exemplos com `curl`

### Criar exemplo

```bash
curl -X POST http://localhost:8080/example/examples/create \
     -H "Content-Type: application/json" \
     -d '{"name": "Teste de exemplo"}'
```

### Buscar exemplo

```bash
curl http://localhost:8080/example/examples/f47ac10b-58cc-4372-a567-0e02b2c3d479
```