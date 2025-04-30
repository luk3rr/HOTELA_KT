# Mensagens de erro

Este documento descreve as mensagens de erro para os diferentes endpoints do sistema, agrupadas por tipo de erro e com
códigos numéricos específicos para cada tipo.

## Erros de Autenticação (1xx)

| Código | Mensagem                     |
|--------|------------------------------|
| 100    | Email already registered     |
| 101    | Invalid credentials          |
| 102    | Token is invalid or expired  |
| 103    | Access denied                |
| 104    | Customer auth already exists |
| 105    | Partner auth already exists  |
| 106    | Customer auth not found      |
| 107    | Partner auth not found       |

## Erros de cliente (2xx)

| Código | Mensagem                             |
|--------|--------------------------------------|
| 200    | Customer with id {id} not found      |
| 201    | Customer with id {id} already exists |

## Erros de parceiro (3xx)

| Código | Mensagem                            |
|--------|-------------------------------------|
| 300    | Partner with id {id} not found      |
| 301    | Partner with id {id} already exists |

## Erros de hotel (4xx)

| Código | Mensagem                          |
|--------|-----------------------------------|
| 400    | Hotel with id {id} not found      |
| 401    | Hotel with id {id} already exists |

## Erros de room (5xx)

| Código | Mensagem                         |
|--------|----------------------------------|
| 500    | Room with id {id} not found      |
| 501    | Room with id {id} already exists |

## Erros de booking (6xx)

| Código | Mensagem                               |
|--------|----------------------------------------|
| 600    | Booking with id {id} not found         |
| 601    | Booking with id {id} already cancelled |

## Erros de payment (7xx)

| Código | Mensagem                       |
|--------|--------------------------------|
| 700    | Payment with id {id} not found |

## Erros de review (8xx)

| Código | Mensagem                      |
|--------|-------------------------------|
| 800    | Review with id {id} not found |

## Erros gerais (9xx)

| Código | Descrição             |
|--------|-----------------------|
| 900    | Invalid data provided |