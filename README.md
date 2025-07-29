# secure-utils

General Security Utilities for Web Applications

## Description
Secure Utils is a Java-based security toolkit for web applications, offering JSON Web Token (JWT) creation and verification, plus a suite of input validation utilities for common patterns like email, URL, and more. It streamlines secure development by packaging robust authentication and data validation into an easy-to-use library.

## Features
- JWT generation and verification
- Input validation: email, URL, UUID, regex patterns
- Extensible validators for custom rules

## Installation
Clone the repository and install with Maven:
```bash
git clone https://github.com/grimimirg/secure-utils.git
cd secure-utils
mvn clean install
```

## Usage
### JWT Utilities
```java
// Initialize with your secret key
JwtUtils jwt = new JwtUtils("your-secret-key");
// Create a token valid for 2 hours
String token = jwt.createToken("user123", Duration.ofHours(2));
// Validate and parse
yes = jwt.validateToken(token);
String subject = jwt.getSubject(token);
```

### Validation Utilities
```java
boolean validEmail = ValidationUtils.isEmail("example@example.com");
boolean validUrl   = ValidationUtils.isUrl("https://example.com");
```

## Contributing
Contributions are welcome! Please fork the repository and open a pull request.

## License
See [LICENSE](LICENSE) for details.

## Author
Created by [@grimimirg](https://github.com/grimimirg).
