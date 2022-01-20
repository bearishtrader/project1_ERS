# project1_ERS
Employee Reimbursement System App including Selenium automation test cases with one testing Scenario run via Cucumber/Gherkin BDD (Behavior Driven Development) framework.

<h3>Features</h3>

Employees are able to log in to the ERS and submit expense reimbursement requests based on various categories and see the status of their submissions.  Financial managers are able to log in and filter employee reimbursement requests based on pending, accepted or rejected status.

<h3>Technologies Used</h3>

1. IntelliJ IDEA using Maven dependency and build manager.
2. Java with JDBC
3. Postgres SQL served on Amazon RDS (Relational Database Service)
4. Javalin middleware to route HTTP requests and send back responses to the front end
5. JUnit test framework and H2 in-memory database used for integration testing of DAO (Data Access Object) layer
6. JUnit with Mockito for Service layer testing
7. HTML/CSS Javascript front-end for presentation layer
8. Frontcontroller and Data Access Object design patterns with a Services layer
9. Selenium web automation tool with Cucumber/Gherkin for E2E test case.

<h3>Getting Started</h3>

git clone https://github.com/bearishtrader/project1_ERS.git<br/>
cd project1_ERS/ReimbursementsApp<br/>
mvn validate<br/>
mvn compile<br/>
mvn test<br/>
mvn package<br/>
java -jar ./target/project1_ERS-{x.y.z}-SNAPSHOT.jar<br/>

From the browser open http://localhost:9000
