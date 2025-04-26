# Documentação do Padrão JWT - Hotela
## Introdução

A aplicação **Hotela** utiliza o padrão **JWT (JSON Web Token)** para autenticação de parceiros e clientes.  
Os tokens são assinados com o algoritmo **HS256** e possuem informações relevantes para a identificação e autorização dos usuários.

## Configurações do Token

- **Algoritmo:** HS256
- **Issuer:** `hotela_backend`
- **Duração do Token:** 60 minutos
- **Assinatura:** Chave secreta simétrica (mínimo 32 caracteres)

## Estrutura do Payload

O payload do JWT contém as seguintes informações:

| Campo      | Descrição                                                 | Tipo   |
|------------|-----------------------------------------------------------|--------|  
| `iss`      | Emissor do token (identifica o backend que gerou o token) | String |
| `iat`      | Timestamp de emissão do token (em segundos desde Epoch)   | Long   |
| `exp`      | Timestamp de expiração do token (em segundos desde Epoch) | Long   |
| `sub`      | Email do usuário autenticado (cliente ou parceiro)        | String |
| `userId`   | UUID do usuário                                           | UUID   |
| `authId`   | UUID de autenticação do usuário                           | UUID   |  
| `role`     | Papel do usuário (`CUSTOMER` ou `PARTNER`)                | String |

## Exemplo de Payload

Para um **cliente**:

```json
{
  "iss": "hotela_backend",
  "iat": 1714147200,
  "exp": 1714150800,
  "sub": "cliente@email.com",
  "userId": "8dd9d429-eb5c-4901-b6d1-46b5b0d0a132",
  "authId": "1c0a10d7-d835-4e38-80d8-cd4275e67a9f",
  "role": "CUSTOMER"
}
```

Para um **parceiro**:

```json
{
  "iss": "hotela_backend",
  "iat": 1714147200,
  "exp": 1714150800,
  "sub": "parceiro@email.com",
  "userId": "d14d91fa-2dfd-4009-b78a-acc7eca05eca",
  "authId": "9c891261-2128-4d71-94d9-04e33a830286",
  "role": "PARTNER"
}
```