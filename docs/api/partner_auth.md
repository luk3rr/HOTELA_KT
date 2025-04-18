# Partner Authentication
Endpoints para registro e login de parceiros.

---

## Endpoints

### POST `/auth/partner/register`

**Descrição:** Registra um novo parceiro para autenticação no sistema.

**Body JSON:**

```json
{
  "email": "partner@email.com",
  "password": "senha_segura"
}
```

> O parceiro correspondente já deve existir previamente na tabela `partner`. O campo `email` deve ser único.

**Resposta de sucesso (`201 Created`):**

```json
{
  "message": "Partner registered successfully"
}
```

**Resposta de erro (`400 Bad Request`):**

```json
{
  "code": "300",
  "message": "Email already registered"
}
```

---

### POST `/auth/partner/login`

**Descrição:** Realiza o login do parceiro e retorna um token JWT.

**Body JSON:**

```json
{
  "email": "partner@email.com",
  "password": "senha_segura"
}
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expireAt": "2025-04-18T14:00:00Z"
}
```

**Resposta de erro (`401 Unauthorized`):**

```json
{
  "code": "101",
  "message": "Invalid credentials"
}
```

---

### POST `/auth/partner/logout`

**Descrição:** Realiza o logout do parceiro e invalida o token atual.

**Header obrigatório:**

```
Authorization: Bearer <token>
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "message": "Logout successful"
}
```

**Resposta de erro (`401 Unauthorized`):**

```json
{
  "code": "102",
  "message": "Token is invalid or expired"
}
```

---

## Exemplos com `curl`

### Registrar parceiro

```bash
curl -X POST http://localhost:8080/auth/partner/register \
     -H "Content-Type: application/json" \
     -d '{"email": "lucas@hotelx.com", "password": "senha_segura"}'
```

### Login do parceiro

```bash
curl -X POST http://localhost:8080/auth/partner/login \
     -H "Content-Type: application/json" \
     -d '{"email": "lucas@hotelx.com", "password": "senha_segura"}'
```

### Logout do parceiro

```bash
curl -X POST http://localhost:8080/auth/partner/logout \
     -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```