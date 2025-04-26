# Design de APIs
Este documento descreve o design de APIs REST para o projeto. O objetivo é fornecer uma visão geral das rotas, métodos HTTP, parâmetros e exemplos de resposta.

Para qualquer endpoint descrito aqui, a URL base é:

```
http://localhost:8080/
```

## Endpoints
1. [Partner](partner.md)
2. [Partner Auth](partner_auth.md)
3. [Customer](customer.md)
4. [Customer Auth](customer_auth.md)
5. [Hotel](hotel.md)
6. [Booking](booking.md)
7. [Payment](payment.md)
8. [Review](review.md)
9. [Room](room.md)

## Erros
A definição dos códigos de erro segue o padrão definido em [error.md](error.md).

## JWT
Os endpoints que requerem autenticação usam JWT (JSON Web Token) para validação. O token deve ser enviado no cabeçalho `Authorization`. Uma documentação detalhada sobre o padrão JWT utilizado pode ser encontrada em [jwt.md](jwt.md).