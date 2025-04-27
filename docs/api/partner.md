# Partner 
Endpoints para gerenciamento de dados do parceiro. Requer autenticação via JWT.

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
  "id": "285fbda5-3d28-47c4-91ee-d31268332696",
  "name": "Hotel Bela Vista",
  "cnpj": "12.345.678/0001-99",
  "email": "contato@belavista.com",
  "phone": "(11) 1234-5678",
  "address": "Av. das Flores, 1000, São Paulo, SP",
  "contactName": "Maria Oliveira",
  "contactEmail": "maria@belavista.com",
  "contactPhone": "(11) 91234-5678",
  "contractSigned": true,
  "status": "ACTIVE",
  "createdAt": "2024-01-10T14:30:00Z",
  "notes": "Parceiro com bom histórico de reservas"
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

### PUT `/partner/update`

**Descrição:** Atualiza os dados de um parceiro existente. Usa os dados no JWT para localizar o parceiro.

**Body JSON:**

```json
{
  "name": "Hotel Bela Vista Ltda",
  "email": "novocontato@belavista.com",
  "phone": "(11) 1234-9876",
  "contactName": "João Pereira",
  "status": "INACTIVE",
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
  "code": "107",
  "message": "Partner auth with id {id} not found"
}
```