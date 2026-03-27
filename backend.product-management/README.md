# Product Management Service

Standalone product catalog and inventory service.

## Responsibilities

- Create, update, enable, and disable products
- Expose public product read APIs
- Expose internal inventory deduction API for order placement

## Config

Configuration is provided centrally by `backend.config-service`.

## Run

```bash
./mvnw spring-boot:run
```
