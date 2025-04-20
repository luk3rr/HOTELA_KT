# Customer Authentication
Endpoints para registro e login de clientes.

---

## Endpoints

### POST `/auth/customer/register`

**Descrição:** Registra um novo cliente para autenticação no sistema.

**Body JSON:**

```json
{
  "email": "example@example.com",
  "password": "password123",
  "name": "John Doe",
  "phone": "1234567890",
  "idDocument": "12345678901",
  "birthDate": "1990-01-01",
  "address": "123 Example Street, City, Country"
}
```

> O cliente correspondente já deve existir previamente na tabela `customer`. O campo `email` deve ser único.

**Resposta de sucesso (`201 Created`):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expire_at": "2025-04-18T14:00:00Z"
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

### POST `/auth/customer/login`

**Descrição:** Realiza o login do cliente e retorna um token JWT.

**Body JSON:**

```json
{
  "email": "example@example.com",
  "password": "password123"
}
```

**Resposta de sucesso (`200 OK`):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expire_at": "2025-04-18T14:00:00Z"
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

## Exemplos com `curl`

### Registrar cliente

```bash
curl -X POST http://localhost:8080/auth/customer/register \
     -H "Content-Type: application/json" \
     -d '{
              "email": "example@example.com",
              "password": "password123",
              "name": "John Doe",
              "phone": "1234567890",
              "idDocument": "12345678901",
              "birthDate": "1990-01-01",
              "address": "123 Example Street, City, Country"
         }'
```

### Login do cliente

```bash
curl -X POST http://localhost:8080/auth/customer/login \
     -H "Content-Type: application/json" \
     -d '{"email": "example@example.com", "password": "password123"}'
```