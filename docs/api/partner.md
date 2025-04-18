# Partner 
Endpoints para gerenciamento de dados do parceiro.

---

## Endpoints

### GET `/partner/{id}`

**Descrição:** Retorna os detalhes de um parceiro pelo ID.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição           |
|-----------|------|---------------------|
| `id`      | UUID | ID único do cliente |

**Resposta de sucesso (`200 OK`):**

```json
{
  "id": "b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae",
  "name": "Hotel Bela Vista",
  "cnpj": "12.345.678/0001-99",
  "email": "contato@belavista.com",
  "phone": "(11) 1234-5678",
  "address": "Av. das Flores, 1000, São Paulo, SP",
  "contact_name": "Maria Oliveira",
  "contact_email": "maria@belavista.com",
  "contact_phone": "(11) 91234-5678",
  "contract_signed": true,
  "status": "active",
  "created_at": "2024-01-10T14:30:00",
  "notes": "Parceiro com bom histórico de reservas"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "300",
  "message": "Partner with id b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae not found"
}
```

---

### POST `/partner/create`

**Descrição:** Cria um novo parceiro no sistema.

**Body JSON:**

```json
{
  "name": "Hotel Bela Vista",
  "cnpj": "12.345.678/0001-99",
  "email": "contato@belavista.com",
  "phone": "(11) 1234-5678",
  "address": "Av. das Flores, 1000, São Paulo, SP",
  "contact_name": "Maria Oliveira",
  "contact_email": "maria@belavista.com",
  "contact_phone": "(11) 91234-5678",
  "contract_signed": true,
  "status": "active",
  "notes": "Parceiro com bom histórico de reservas"
}
```

**Resposta de sucesso (`201 Created`):**

```json
{
  "id": "b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae"
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

### PUT `/partner/{id}/update`

**Descrição:** Atualiza os dados de um parceiro existente.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                               |
|-----------|------|-----------------------------------------|
| `id`      | UUID | O ID único do parceiro a ser atualizado |

**Body JSON:**

```json
{
  "name": "Hotel Bela Vista Ltda",
  "email": "novocontato@belavista.com",
  "phone": "(11) 1234-9876",
  "contact_name": "João Pereira",
  "status": "inactive",
  "notes": "Contrato em revisão"
}
```

> Campos não informados serão mantidos com os valores atuais.

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Partner updated successfully"
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
  "code": "300",
  "message": "Partner with id {id} not found"
}
```

---

### DELETE `/partner/{id}/delete`

**Descrição:** Exclui um parceiro do sistema.

**Parâmetro de caminho:**

| Parâmetro | Tipo | Descrição                             |
|-----------|------|---------------------------------------|
| `id`      | UUID | O ID único do parceiro a ser excluído |

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Partner deleted successfully"
}
```

**Resposta de erro (`404 Not Found`):**

```json
{
  "code": "300",
  "message": "Partner with id {id} not found"
}
```

---

## Exemplos com `curl`

### Criar parceiro

```bash
curl -X POST http://localhost:8080/parceiro/create \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Hotel Bela Vista",
           "cnpj": "12.345.678/0001-99",
           "email": "contato@belavista.com",
           "phone": "(11) 1234-5678",
           "address": "Av. das Flores, 1000, São Paulo, SP",
           "contact_name": "Maria Oliveira",
           "contact_email": "maria@belavista.com",
           "contact_phone": "(11) 91234-5678",
           "contract_signed": true,
           "status": "active",
           "notes": "partner com bom histórico de reservas"
         }'
```

### Atualizar parceiro

```bash
curl -X PUT http://localhost:8080/partner/b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae/update \
     -H "Content-Type: application/json" \
     -d '{"email": "novoemail@belavista.com", "status": "inactive"}'
```

### Excluir parceiro

```bash
curl -X DELETE http://localhost:8080/partner/b1d3c1c9-5612-4aa4-9d1a-1f4206f8b6ae/delete
```