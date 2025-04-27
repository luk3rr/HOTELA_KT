# Partner Authentication
Endpoints para registro e login de parceiros.

---

## Endpoints

### POST `/auth/partner/register`

**Descrição:** Registra um novo parceiro para autenticação no sistema.

**Body JSON:**

```json
{
  "email": "contato@pousadadomar.com.br",
  "password": "senhaSuperSecreta123",
  "name": "Pousada do Mar",
  "cnpj": "12.345.678/0001-99",
  "phone": "+55 31 99999-8888",
  "address": "Rua das Gaivotas, 123 - Ubatuba, SP",
  "contactName": "Maria Silva",
  "contactPhone": "+55 11 98888-7777",
  "contactEmail": "maria.silva@pousadadomar.com.br",
  "contractSigned": true,
  "notes": "Contrato assinado digitalmente no dia 20/03/2025"
}
```

> O parceiro correspondente já deve existir previamente na tabela `partner`. O campo `email` deve ser único.

**Resposta de sucesso (`201 Created`):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
}
```

**Resposta de erro (`409 Conflict`):**

```json
{
  "code": "100",
  "message": "Email already registered"
}
```

---

### POST `/auth/partner/login`

**Descrição:** Realiza o login do parceiro e retorna um token JWT.

**Body JSON:**

```json
{
  "email": "contato@pousadadomar.com.br",
  "password": "senhaSuperSecreta123",
}
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
}
```

**Resposta de erro (`401 Unauthorized`):**

```json
{
  "code": "101",
  "message": "Invalid credentials"
}
```