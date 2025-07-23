# Court E-Filing System: Spring Boot + Angular

A complete court e-filing system with Spring Boot REST API backend and Angular frontend with Bootstrap styling. Modernized from legacy JSP to Angular components.

## Tech Stack

### Backend
- **Spring Boot 3.2.0** - Main framework
- **Spring Data JPA** - Database abstraction layer
- **Hibernate Validator** - Bean validation
- **H2 Database** - In-memory database for development
- **Maven** - Dependency management

### Frontend
- **Angular 17** - Frontend framework with standalone components
- **Bootstrap 5.3** - CSS framework
- **TypeScript** - Programming language
- **RxJS** - Reactive programming
- **Reactive Forms** - Form handling and validation

## Project Structure

```
fullstack-app/
├── backend/                 # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/example/fullstack/
│   │       ├── FullstackBackendApplication.java
│   │       └── config/
│   │           └── CorsConfig.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── frontend/                # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.component.ts
│   │   │   ├── app.component.html
│   │   │   ├── app.component.css
│   │   │   ├── models/
│   │   │   │   └── case.model.ts
│   │   │   ├── services/
│   │   │   │   └── case.service.ts
│   │   │   └── components/
│   │   │       ├── case-form/
│   │   │       │   ├── case-form.component.ts
│   │   │       │   ├── case-form.component.html
│   │   │       │   └── case-form.component.css
│   │   │       └── case-list/
│   │   │           ├── case-list.component.ts
│   │   │           ├── case-list.component.html
│   │   │           └── case-list.component.css
│   │   ├── index.html
│   │   ├── main.ts
│   │   └── styles.css
│   ├── angular.json
│   ├── package.json
│   └── tsconfig.json
└── README.md
```

## Features

- **Case Management CRUD Operations**
  - Create new court case filings
  - View all cases with status tracking
  - Update existing case information
  - Delete cases
- **Court Filing Form** (converted from JSP)
  - Dynamic dropdowns (Court Location, Case Category, Case Type)
  - Filing fee calculation and exemptions
  - Missouri statute compliance (RSMo 514.040)
  - Notes to clerk functionality
- **Form Validation** (both frontend and backend)
- **Responsive Design** with Bootstrap
- **Error Handling** and loading states
- **CORS Configuration** for cross-origin requests

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- npm or yarn

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. The backend will start on `http://localhost:8080`

4. Access H2 Database Console (optional):
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: `password`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the Angular development server:
   ```bash
   npm start
   ```

4. The frontend will start on `http://localhost:4200`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cases` | Get all cases |
| GET | `/api/cases/{id}` | Get case by ID |
| POST | `/api/cases` | Create new case |
| PUT | `/api/cases/{id}` | Update case |
| DELETE | `/api/cases/{id}` | Delete case |
| GET | `/api/cases/court-locations` | Get court locations |
| GET | `/api/cases/categories` | Get case categories |
| GET | `/api/cases/types/{categoryId}` | Get case types by category |

## Case Model

```json
{
  "id": 1,
  "submissionNumber": "SUB-2024-001",
  "courtCaseNumber": "24CV-001234",
  "courtLocation": "STL_CITY",
  "caseCategory": "CIVIL",
  "caseType": "CIVIL_GENERAL",
  "filerReferenceNumber": "REF-2024-001",
  "filingFee": 165.00,
  "exempt": false,
  "govAttorney": false,
  "informaPauperis": false,
  "waived": false,
  "noteToClerk": "Please expedite processing",
  "createdDate": "2024-01-15T10:30:00Z",
  "updatedDate": "2024-01-15T14:45:00Z"
}
```

## Validation Rules

- **Court Location**: Required, must be valid court location
- **Case Category**: Required, must be valid category
- **Case Type**: Required, must be valid type for selected category
- **Filer Reference Number**: Optional, max 30 characters
- **Filing Fee**: Required if no exemptions selected, must be positive number
- **Fee Exemptions**: At least one required if no filing fee
- **Notes to Clerk**: Optional, max 1000 characters

## Development

### Backend Development
- The application uses H2 in-memory database for development
- Database is recreated on each restart
- Spring DevTools is included for hot reload

### Frontend Development
- Angular CLI is configured for development
- Hot reload is enabled
- Bootstrap is integrated via CDN

## Production Deployment

### Backend
1. Update `application.properties` to use a production database (MySQL, PostgreSQL, etc.)
2. Build the application: `./mvnw clean package`
3. Run the JAR file: `java -jar target/fullstack-backend-0.0.1-SNAPSHOT.jar`

### Frontend
1. Build for production: `npm run build`
2. Deploy the `dist/` folder to a web server

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License.
