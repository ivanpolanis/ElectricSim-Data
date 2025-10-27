# How to run

To run the initialization script, use the following command in your terminal:

```bash
chmod +x ./init.sh
```

If you want to down the containers, use:

```bash
chmod +x ./down.sh
```

# Endpoints

- GET /
  - Query Parameters:
    - start: LocalDate (Format: YYYY-MM-DD)
    - end: LocalDate (Format: YYYY-MM-DD)
  - Description: Retrieves CSV data for the specified date range.
    - Example: `GET /?start=2023-01-01&end=2023-01-31`

> [!WARNING]  
> Usar despues de 2025-03-10 que cammesa devuelve datos.
