# ArkaneGames

ArkaneGames is a web application for managing and discovering games. It provides user authentication through both username/password and OAuth2 providers (Google and GitHub), game metadata management, and user profiles.

## Technologies Used

- Java 21
- Spring Boot
- Spring Security with OAuth2
- MongoDB for data storage
- Docker and Docker Compose for containerization
- Thymeleaf for server-side templating

## Prerequisites

To run the project, you need to have the following installed:
- Docker
- Docker Compose

> Note: While JDK 21 is mentioned in the prerequisites, you don't actually need to have Java installed locally if you're using Docker. The multi-stage Dockerfile will handle building the application within the container.

## How to Run

### Using Docker (Recommended)

1. Clone the repository:
   ```
   git clone https://github.com/your-username/ArkaneGames.git
   cd ArkaneGames
   ```

2. Create a `.env` file in the project root with your OAuth credentials:
   ```
   GITHUB_CLIENT_ID=your_github_client_id
   GITHUB_CLIENT_SECRET=your_github_client_secret
   GOOGLE_CLIENT_ID=your_google_client_id
   GOOGLE_CLIENT_SECRET=your_google_client_secret
   ```

3. Start the application with Docker Compose:
   ```
   docker-compose up --build
   ```

4. Access the application at http://localhost:8080

### Running Locally (Requires JDK 21)

1. Make sure MongoDB is running on localhost:27017

2. Build the application:
   ```
   ./gradlew build
   ```

3. Run the application:
   ```
   java -jar build/libs/ArkaneGames-0.0.1-SNAPSHOT.jar
   ```

4. Access the application at http://localhost:8080

## Features

- **User Authentication**:
  - Traditional username/password login with MongoDB backend
  - OAuth2 social login with Google and GitHub
  - User registration system

- **Game Management**:
  - Browse and search game metadata
  - View detailed game information
  - (Admin) Add, update, and remove games

- **User Profiles**:
  - Personalized user dashboards
  - Profile management

## API Access with Postman

To access protected APIs in ArkaneGames using Postman, you'll need to authenticate first. Here are the methods:

### Method 1: Using Session Cookies

1. **Authenticate via the form login endpoint**:
   - Send a POST request to `http://localhost:8080/login`
   - In the Body tab, select `x-www-form-urlencoded`
   - Add key-value pairs:
     - `username`: your_email@example.com
     - `password`: your_password
   - Make sure "Automatically follow redirects" is enabled
   - Send the request

2. **Use the session cookie for subsequent requests**:
   - After successful login, Postman will store the session cookie (JSESSIONID)
   - All subsequent requests will automatically include this cookie
   - Make requests to any protected endpoint (e.g., `http://localhost:8080/api/games`)

### Method 2: Using OAuth2 Tokens

For a more API-friendly approach when using tools like Postman:

1. **Log in through the web interface** with your OAuth2 provider (Google or GitHub)
2. **Use browser developer tools** to copy your JSESSIONID cookie
3. **In Postman**, add the cookie to your requests:
   - Go to the "Cookies" section
   - Add the JSESSIONID cookie with the value from your browser

> Note: The application uses session-based authentication. This ensures a secure and simple authentication flow for both web and API access.

## Development

### Project Structure

- `src/main/java/com/ArnabMdev/ArkaneGames`: Main source code
  - `config`: Configuration classes
  - `controller`: Web controllers
  - `entity`: Data models
  - `repository`: Data access objects
  - `service`: Business logic

- `src/main/resources`:
  - `templates`: Thymeleaf HTML templates
  - `application.properties`: Application configuration

### Contributing

1. Fork the repository
2. Create a feature branch
3. Submit a pull request

