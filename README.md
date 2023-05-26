# SampleCompany Computer - Spring Boot

This is a sample Spring Boot application for managing computers issued by SampleCompany. It provides a REST interface to store and retrieve computer details in an arbitrary database. The application also notifies the system administrator when an employee is assigned 3 or more computers using Spring's Application Event mechanism.
## Technologies Used

- Java 11
- Spring Boot 2.7.11
- Spring Data JPA
- H2 Database
- Spring MVC
- Maven

## API Endpoints

The following REST endpoints are available:

- `GET /api/computer/`: Get all computers
- `POST /api/computer/`: Create a new computer
- `GET /api/computer/{id}`: Get the data of a single computer
- `PUT /api/computer/{id}`: Update the details of a computer
- `DELETE /api/computer/{id}`: Remove a computer
- `PATCH /api/computer/{id}/{abbreviation}`: Assign the computer to the employee
- `PATCH /api/computer/{id}`: Unassign the computer from the employee
- `GET /api/computer/assign/{abbreviation}`: Get all assigned computers for an employee

## Event Notification

The application uses Spring's Application Event mechanism to send notifications to the system administrator when an employee is assigned 3 or more computers. When assigning a computer to an employee, an internal event is triggered. The event carries the necessary information such as the employee's abbreviation.

The `MaxAssignmentsReachedEvent` class represents this event and is automatically published by the application. The event is then handled by the `MaxAssignmentsReachedEventListener`, which listens for the event and triggers the notification process.

The notification is sent to the system administrator team using a messaging service. The messaging service runs in a Docker container and can be accessed at `POST http://localhost:8080/api/notify`. The expected body of the REST endpoint is defined as follows:

```json
{
  "level": "warning",
  "employeeAbbreviation": "mmu",
  "message": "some message"
}
```
- **Note:** When I tried to use this command: ` docker pull greenbone/exercise-admin-notification` the docker threw an error that the image is incompatible with macbook pro m1 processors. So I cloned the [repository](https://github.com/greenbone/exercise-admin-notification), and built the image again which is consistent with Mac m1. 


## Prerequisites

- [Java Development Kit (JDK)](https://adoptopenjdk.net/) - Make sure JDK 11 or higher is installed.
- [Maven](https://maven.apache.org/) - Make sure Maven is installed.

## Getting Started

Follow the steps below to build, test, and run the Spring Boot application.

### 1. Clone the Repository

Clone the Git repository to your local machine:

```bash
git clone https://github.com/ozgen/sample-company.git
cd sample-company
```
### 2. Build the Application
Build the application using Maven:

```bash
mvn clean package
```

### 3. Build the Application
Run the application using the Spring Boot Maven plugin:

```bash
mvn spring-boot:run
```
The application will start and listen on `http://localhost:8081`.


### 4. Test the Application

Run the tests to verify the functionality of the application:

```bash
mvn test 
```

### 5. Test the Application with Postman

- Download the Postman application from the official website and install it on your machine.
- Launch Postman and click on the "Import" button located in the top-left corner of the application.
- Choose the "Import From File" option and browse for the downloaded [greenbone.postman_collection.json](greenbone.postman_collection.json).
- Select the file and click "Open" to import the collection into Postman.
- The imported collection will now appear in the Postman workspace.

### 6. Additional Notes and Improvements

- **Database Configuration**: By default, this application uses an in-memory H2 database. For a production-ready setup, consider configuring a different database like MySQL, PostgreSQL, or Oracle. Update the database configuration in the `application.yaml` file.

- **Security**: In a real-world scenario, it would be important to implement security measures such as authentication and authorization to protect the REST endpoints and ensure data confidentiality. Consider using Spring Security to add these features.

- **Dependency Vulnerability Check**: Regularly checking for known vulnerabilities in the project's dependencies is crucial for maintaining a secure application. Consider using tools like the OWASP Dependency-Check to perform automated vulnerability checks on your project's dependencies. Integrating this check into your CI/CD pipeline ensures that vulnerabilities are identified and addressed early in the development process.

- **API Documentation**: Improve the documentation of the API endpoints by using tools like Swagger or Spring RestDocs. These tools help generate comprehensive API documentation, including endpoint descriptions, request/response examples, and possible error scenarios. Well-documented APIs facilitate easier integration and understanding of the application by other developers or consumers.

- **Containerization**: Dockerize the application to enable easy deployment and scalability. Create a Dockerfile and docker-compose.yml file to define the container environment and dependencies. Additionally, integrate the application with Kubernetes for container orchestration. Create Kubernetes deployment and service files to deploy and manage the application in a Kubernetes cluster.

- **End-to-End Testing**: Implement end-to-end (E2E) tests to validate the complete functionality of the application from a user's perspective. Use tools like Selenium or Cypress to automate browser-based E2E tests. Create separate test files for different scenarios, covering all CRUD operations and edge cases.

- **Continuous Integration and Deployment**: Integrate the project with a CI/CD pipeline tool (e.g., Jenkins, AzureDevops) to automate the build, test, and deployment processes. Configure the pipeline to trigger builds and tests on every commit and deploy the application to a staging or production environment based on predefined conditions.

- **Monitoring and Logging**: Implement monitoring and logging solutions (e.g., Prometheus, Grafana, ELK Stack) to gain insights into the application's performance and troubleshoot issues. Set up health checks, metrics, and logs to ensure the application is running smoothly and detect any anomalies.

- **Scalability**: Design the application with scalability in mind. Consider using technologies like Spring Cloud and Kubernetes scaling features to handle increased traffic and load. Implement load testing to determine the application's performance and identify any bottlenecks.

- **Error Handling and Resilience**: Enhance the application's error handling and resilience by implementing proper exception handling, retries, and circuit breakers using libraries like Spring Retry and Resilience4j. This ensures the application gracefully handles failures and recovers from them.

- **Monitoring Kubernetes Resources**: Utilize Kubernetes monitoring tools (e.g., Prometheus, Grafana) to monitor the health and performance of Kubernetes resources, such as pods, deployments, and services.

- **Deployment Strategies**: Explore different deployment strategies like blue-green deployment or canary releases to minimize downtime and provide seamless updates to the application.

- **Infrastructure as Code**: Use Infrastructure as Code tools (e.g., Terraform, Helm) to define and manage the application's infrastructure, including Kubernetes resources, databases, and networking components.




### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.11/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.11/maven-plugin/reference/html/#build-image)

