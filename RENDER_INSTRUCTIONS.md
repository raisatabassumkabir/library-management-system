# Render Deployment Instructions

Since your `application.properties` file is git-ignored (for security), you need to provide the database configuration directly to Render using **Environment Variables**.

## 1. Create Web Service
- Connect your GitHub repository to Render.
- Select your repository.
- **Runtime**: Docker

## 2. Environment Variables
In the Render Dashboard for your service, go to the **Environment** tab and add the following variables. These overrides the defaults in Spring Boot.

| Key | Value (Example) | Description |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://<host>:3306/<database>` | Your live database URL (e.g., from generic external host or Render PostgreSQL if adapted) |
| `SPRING_DATASOURCE_USERNAME` | `<your-db-username>` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `<your-db-password>` | Database password |
| `PORT` | `8080` | Port for the application |

> **Note**: Since you are using MySQL, make sure your Render service can reach your MySQL database. If you use a Render-hosted database (PostgreSQL is native, but MySQL is also available as a private service), ensure the connection details are correct.

## 3. Deployment
- Push your `Dockerfile` to GitHub.
- Render will automatically detect it and start building.
