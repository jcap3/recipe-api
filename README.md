# Recipe API

## Project Overview

Recipe API is a RESTful Spring Boot service for managing cooking recipes. It supports full CRUD operations and a search endpoint with optional filters for vegetarian recipes, serving size, included ingredients, excluded ingredients, and instruction text.

## Technology Stack

| Area | Technology                          |
| --- |-------------------------------------|
| Language | Java 17                             |
| Framework | Spring Boot 3.5.0                   |
| Persistence | Spring Data JPA, Hibernate          |
| Database | PostgreSQL 14+                      |
| Build | Maven Wrapper                       |
| Boilerplate reduction | Project Lombok                      |
| Local infrastructure | Docker Compose                      |
| Automated tests | H2 in PostgreSQL compatibility mode |

## Prerequisites

- Java 17
- Docker

## Running the Application

Start the local PostgreSQL database:

```bash
docker compose up -d
```

Run the application:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell, use:

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs on `http://localhost:8080`.

## Running Tests

```bash
./mvnw test
```

On Windows PowerShell, use:

```powershell
.\mvnw.cmd test
```

Local runtime uses PostgreSQL through Docker Compose. Automated tests use an H2 in-memory database in PostgreSQL compatibility mode.

## API Endpoints

Base path: `/v0/recipes`

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/v0/recipes` | Create a recipe |
| `GET` | `/v0/recipes` | List active recipes |
| `GET` | `/v0/recipes/{id}` | Get a recipe by ID |
| `PUT` | `/v0/recipes/{id}` | Update a recipe |
| `DELETE` | `/v0/recipes/{id}` | Soft delete a recipe |
| `GET` | `/v0/recipes/search` | Search recipes with optional filters |

Manual request examples are available in the [`http/`](http/) directory and can be run from IntelliJ IDEA or any compatible HTTP client.

### Search Query Parameters

| Parameter | Description |
| --- | --- |
| `vegetarian` | Filters by vegetarian status. Example: `true` |
| `servings` | Finds recipes that can serve the requested number of people |
| `includeIngredients` | Comma-separated ingredient names that must be present |
| `excludeIngredients` | Comma-separated ingredient names that must not be present |
| `instructionContains` | Text that must appear in at least one instruction step |

## Sample Create Recipe Request

```http
POST /v0/recipes HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

```json
{
  "title": "Tomato Basil Pasta",
  "description": "A simple vegetarian pasta with cherry tomatoes, garlic, and fresh basil.",
  "servingsMin": 2,
  "servingsMax": 4,
  "vegetarian": true,
  "ingredients": [
    {
      "ingredientName": "Cherry Tomatoes",
      "displayText": "2 cups cherry tomatoes, halved",
      "quantity": 2,
      "unit": "cups",
      "preparationNote": "halved",
      "optionalIngredient": false,
      "position": 1
    },
    {
      "ingredientName": "Spaghetti",
      "displayText": "250 g spaghetti",
      "quantity": 250,
      "unit": "g",
      "preparationNote": null,
      "optionalIngredient": false,
      "position": 2
    },
    {
      "ingredientName": "Fresh Basil",
      "displayText": "1 handful fresh basil leaves",
      "quantity": 1,
      "unit": "handful",
      "preparationNote": "torn",
      "optionalIngredient": false,
      "position": 3
    },
    {
      "ingredientName": "Parmesan",
      "displayText": "grated parmesan, to serve",
      "quantity": null,
      "unit": null,
      "preparationNote": "grated",
      "optionalIngredient": true,
      "position": 4
    }
  ],
  "instructions": [
    {
      "stepNumber": 1,
      "instruction": "Boil the spaghetti in salted water until al dente.",
      "durationSeconds": 600
    },
    {
      "stepNumber": 2,
      "instruction": "Simmer cherry tomatoes with olive oil and garlic until softened.",
      "durationSeconds": 480
    },
    {
      "stepNumber": 3,
      "instruction": "Toss the pasta with the tomato sauce and finish with basil.",
      "durationSeconds": 120
    }
  ],
  "tags": [
    "Vegetarian",
    "Pasta",
    "Quick Dinner"
  ]
}
```

## Sample Search Requests

```http
GET /v0/recipes/search?vegetarian=true
GET /v0/recipes/search?servings=4
GET /v0/recipes/search?includeIngredients=tomato,basil
GET /v0/recipes/search?excludeIngredients=chicken
GET /v0/recipes/search?instructionContains=boil
GET /v0/recipes/search?vegetarian=true&servings=4&includeIngredients=tomato&excludeIngredients=mushroom
```

## Assumptions and Design Decisions

- Recipes contain ordered ingredients and ordered instruction steps.
- Ingredients are normalized into a separate table to support include and exclude ingredient filtering.
- `recipe_ingredients.displayText` preserves the original user-entered ingredient text.
- Tags are modeled as a many-to-many relationship for flexible categorization.
- `isVegetarian` is stored directly on the recipe and supplied by the client.
- Recipes are soft deleted using `deletedAt`.
- Search is implemented using Spring Data JPA Specifications to dynamically compose optional filters.
- Specifications improve maintainability and code scalability; database scalability is addressed through indexing and schema design.
- Hibernate uses `ddl-auto=create-drop` for challenge simplicity.
- In production, schema migrations would be managed using Flyway or Liquibase.
- Request DTOs use Jakarta Bean Validation.
- Validation and application errors are returned as structured JSON responses.
