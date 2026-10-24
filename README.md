# Liskart E2E Automation

This project is an end-to-end (E2E) automation framework for Liskart, built using Java, Selenium WebDriver, TestNG, and Maven. It covers customer, seller, and guest flows, including registration, login, cart, checkout, and address validation.

## Features
- Page Object Model (POM) design for maintainability
- Data-driven testing using Excel and TestNG DataProviders
- Robust logging and reporting with ExtentReports
- Handles coupon, delivery charges, and non-serviceable pincode scenarios
- Screenshots on failure and for reporting
- Modular utilities for API, PDF, and file handling

## Project Structure

```
LisKartTestAutomation/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/liskart/
│   │   │   ├── actiondriver/      # Custom WebDriver actions
│   │   │   ├── base/              # Base test setup/teardown
│   │   │   ├── listeners/         # TestNG listeners
│   │   │   ├── pages/             # Page Object classes
│   │   │   └── utilities/         # Utilities (API, PDF, Data, etc.)
│   │   └── resources/             # Config, log4j, META-INF
│   └── test/
│       ├── java/com/liskart/test/ # Test classes
│       └── resources/             # testng.xml, ExtentReport, screenshots, testdata
└── target/                        # Build output
```

## Prerequisites
- Java 8 or above
- Maven 3.x
- Chrome browser (recommended)
- ChromeDriver (compatible with your Chrome version)

## Setup Instructions
1. **Clone the repository**
	```
	git clone <repo-url>
	cd LisKartTestAutomation
	```
2. **Install dependencies**
	```
	mvn clean install
	```
3. **Configure properties**
	- Update `src/main/resources/config.properties` as needed (URLs, credentials, etc.)
	- Ensure `log4j2.xml` is configured for logging
4. **Test Data**
	- Place Excel or other data files in `src/test/resources/testdata/`

## Running Tests

### Using Maven
```
mvn test
```

### Using TestNG Suite
You can run specific suites or tests by editing `src/test/resources/testng.xml`.

### Example Test Classes
- `B2CRegistrationTest` - Customer registration flow
- `B2BRegistrationTest` - Seller registration with GST
- `B2CGuestToCheckoutTest` - Guest add-to-cart and checkout, including non-serviceable pincode handling

## Reports & Screenshots
- HTML reports: `test-output/`
- Screenshots: `src/test/resources/screenshots/`
- ExtentReports: `src/test/resources/ExtentReport/`

## Customization
- Add new page objects in `src/main/java/com/liskart/pages/`
- Add/modify tests in `src/test/java/com/liskart/test/`
- Update locators and flows as per UI changes

## Troubleshooting
- Ensure ChromeDriver version matches your Chrome browser
- Clean screenshots folder regularly to avoid clutter
- Check logs in `logs/` for debugging

## Contribution
Pull requests and suggestions are welcome! Please follow the existing code style and structure.

---
**Author:** Liskart QA Automation Team

