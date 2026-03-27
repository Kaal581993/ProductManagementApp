# Order Services

Standalone cart and order service with Kafka event publishing.

## Responsibilities

- Maintain user carts
- Place orders transactionally in the local service database
- Call product service to deduct inventory
- Publish `order.placed` events to Kafka

## Config

Configuration is provided centrally by `backend.config-service`.

## Run

```bash
./mvnw spring-boot:run
```
