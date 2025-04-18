# Cliente Autenticação
Endpoints para registro e login de clientes.

---

## Endpoints

### POST `/auth/client/register`

**Descrição:** Registra um novo cliente para autenticação no sistema.

**Body JSON:**

```json
{
  "email": "cliente@email.com",
  "password": "senha_segura"
}
```

> O cliente correspondente já deve existir previamente na tabela `cliente`. O campo `email` deve ser único.

**Resposta de sucesso (`201 Created`):**

```json
{
  "message": "Client registered successfully"
}
```

**Resposta de erro (`400 Bad Request`):**

```json
{
  "code": "100",
  "message": "Email already registered"
}
```

---

### POST `/auth/client/login`

**Descrição:** Realiza o login do cliente e retorna um token JWT.

**Body JSON:**

```json
{
  "email": "cliente@email.com",
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

### POST `/auth/client/logout`

**Descrição:** Realiza o logout do cliente e invalida o token atual.

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

### Registrar cliente

```bash
curl -X POST http://localhost:8080/auth/client/register \
     -H "Content-Type: application/json" \
     -d '{"email": "joao@email.com", "password": "minha_senha"}'
```

### Login do cliente

```bash
curl -X POST http://localhost:8080/auth/client/login \
     -H "Content-Type: application/json" \
     -d '{"email": "joao@email.com", "password": "minha_senha"}'
```

### Logout do cliente

```bash
curl -X POST http://localhost:8080/auth/client/logout \
     -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```