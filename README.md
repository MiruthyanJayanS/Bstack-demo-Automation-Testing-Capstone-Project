# BStack Demo – Automation Testing Capstone Project

A capstone project built to demonstrate end-to-end automation testing skills on the [BrowserStack Demo](https://bstackdemo.com/) e-commerce website. This project goes beyond basic test scripting to simulate a real-world QA workflow — combining automation, BDD-style testing, structured project management, and CI/CD integration.

## 📌 About the Project

This capstone was designed to bring together everything learned during automation testing training into one cohesive project, covering:

- **UI Automation** of the BStack Demo site using Selenium WebDriver
- **Behavior-Driven Development (BDD)** test scenarios written with Cucumber
- **Structured project management** — test scenarios and bugs were tracked as Jira tickets, which were then resolved and closed as part of the workflow, simulating a real sprint-based QA process
- **CI/CD integration** using Jenkins, configured to automatically trigger and update test runs on GitHub

## 🛠️ Tech Stack

| Category | Tools / Frameworks |
|---|---|
| Language | Java |
| Automation Tool | Selenium WebDriver |
| BDD Framework | Cucumber |
| Test Framework | TestNG / JUnit |
| Build Tool | Maven |
| CI/CD | Jenkins |
| Project Management | Jira |

## 🎯 Key Highlights

- ✅ Automated functional test cases for the BStack Demo website (login, product listing, cart, checkout flows)
- ✅ Wrote feature files in Gherkin syntax and implemented step definitions using Cucumber
- ✅ Created and resolved Jira tickets to practice structured, sprint-style project tracking
- ✅ Set up a Jenkins pipeline to run automated tests and push status updates to GitHub, simulating a CI/CD workflow

## 📂 Project Structure

```
Bstack-demo-Automation-Testing-Capstone-Project/
├── src/
│   ├── main/java/                  # Page objects & utility classes
│   └── test/
│       ├── java/                   # Step definitions & test runners
│       └── resources/features/     # Cucumber feature files (Gherkin)
├── Jenkinsfile                     # CI/CD pipeline configuration
├── pom.xml                         # Maven dependencies and build config
└── README.md
```

## 🚀 Getting Started

### Prerequisites
- Java JDK 8+
- Maven
- Jenkins (if you want to replicate the CI/CD pipeline locally)
- Chrome/Firefox with matching WebDriver

### Setup
```bash
git clone https://github.com/MiruthyanJayanS/Bstack-demo-Automation-Testing-Capstone-Project.git
cd Bstack-demo-Automation-Testing-Capstone-Project
mvn clean install
```

### Running Tests

Run all tests via Maven:
```bash
mvn test
```

Run Cucumber feature files via the test runner class (adjust class name as per your runner):
```bash
mvn test -Dcucumber.options="src/test/resources/features"
```

### CI/CD Pipeline
The included `Jenkinsfile` defines the pipeline used to automatically run tests and push updates to GitHub on each trigger. To use it:
1. Set up a Jenkins job pointing to this repository
2. Configure the pipeline to use the `Jenkinsfile` in the repo root
3. Trigger builds manually or via webhook on push

## 🎯 Learning Outcomes

- Designing and automating real-world test scenarios using Selenium + Cucumber
- Writing clean, readable BDD feature files
- Managing test cases and defects through Jira in a structured workflow
- Setting up and running a CI/CD pipeline with Jenkins integrated with GitHub

## 📄 License

This project was built for educational and portfolio purposes as part of automation testing training.
