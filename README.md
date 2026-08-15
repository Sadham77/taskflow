# TaskFlow — Task Management Tool

A Trello-style task management app built with **Spring Boot**, **React**, and **MySQL**,
featuring JWT authentication and drag-and-drop cards.

## Features
- User registration/login with JWT-based authentication (Spring Security)
- Create boards, lists (columns), and cards
- Drag-and-drop cards between lists (`@hello-pangea/dnd`)
- REST API with per-user data isolation (you can only see your own boards)

## Tech Stack
**Backend:** Java 17, Spring Boot 3, Spring Security, Spring Data JPA, MySQL, JWT (jjwt)
**Frontend:** React 18, Vite, React Router, Axios, @hello-pangea/dnd

## Project Structure
```
taskflow/
├── backend/     Spring Boot API (port 8080)
└── frontend/    React app (port 5173)
```

## Getting Started

### 1. Backend setup
```bash
cd backend
```
1. Create a MySQL database (or let `createDatabaseIfNotExist=true` handle it).
2. Edit `src/main/resources/application.properties`:
   - Set `spring.datasource.username` / `password` to your MySQL credentials.
   - Replace `app.jwt.secret` with your own secret — generate one with:
     ```bash
     openssl rand -base64 32
     ```
3. Run it:
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will start on `http://localhost:8080`.

### 2. Frontend setup
```bash
cd frontend
npm install
npm run dev
```
The app will start on `http://localhost:5173`.

### 3. Try it out
1. Open `http://localhost:5173/register` and create an account.
2. Create a board, add a few lists (e.g. "To Do", "In Progress", "Done").
3. Add cards and drag them between lists.

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Create an account |
| POST | `/api/auth/login` | Log in, returns a JWT |
| GET | `/api/boards` | List your boards |
| POST | `/api/boards` | Create a board |
| GET | `/api/boards/{id}/lists` | Get lists (with cards) for a board |
| POST | `/api/boards/{boardId}/lists` | Create a list |
| POST | `/api/lists/{listId}/cards` | Create a card |
| PUT | `/api/cards/{id}` | Update a card |
| PUT | `/api/cards/{id}/move` | Move a card to a new list/position |
| DELETE | `/api/cards/{id}` | Delete a card |

All endpoints except `/api/auth/**` require an `Authorization: Bearer <token>` header.

## Deployment Notes
- **Backend:** Deploy to Render or Railway. Set environment variables for
  `spring.datasource.url`, `username`, `password`, and `app.jwt.secret` instead of
  hardcoding them (use `${ENV_VAR}` syntax in `application.properties`).
- **Frontend:** Deploy to Vercel or Netlify. Update the `baseURL` in
  `src/api/client.js` to your deployed backend URL, and update
  `SecurityConfig.corsConfigurationSource()` on the backend to allow your deployed
  frontend origin.

## What This Project Demonstrates
- MVC architecture end-to-end (mirrors the E-Shopper Book Store project, but with
  a modern React front end instead of JSP)
- Stateless JWT authentication with Spring Security
- Relational schema design (Users → Boards → Lists → Cards)
- Optimistic UI updates synced with a REST backend
- Ownership-based authorization (users can only access their own data)

## Possible Next Steps
- Add board sharing / multiple collaborators per board
- Add due-date reminders (email or in-app notifications)
- Add card labels/tags and filtering
- Write unit tests for `CardService.moveCard` (the trickiest logic in the app)
- Containerize with Docker + docker-compose for one-command local setup
