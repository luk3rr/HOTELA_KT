# Mensagens de erro
Este documento descreve as mensagens de erro para os diferentes endpoints do sistema, agrupadas por tipo de erro e com códigos numéricos específicos para cada tipo.

## Erros de Autenticação (1xx)
| Código | Mensagem                    |
|--------|-----------------------------|
| 100    | Email already registered    |
| 101    | Invalid credentials         |
| 102    | Token is invalid or expired |

## Erros de cliente (2xx)
| Código | Mensagem                        |
|--------|---------------------------------|
| 200    | Customer with id {id} not found |

## Erros de parceiro (3xx)
| Código | Mensagem                       |
|--------|--------------------------------|
| 300    | Partner with id {id} not found |

## Erros de hotel (4xx)
| Código | Mensagem                     |
|--------|------------------------------|
| 400    | Hotel with id {id} not found |

## Erros gerais (9xx)
| Código | Descrição             |
|--------|-----------------------|
| 900    | Invalid data provided |