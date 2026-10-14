# mtchlvr hotel
Java 21, Spring Boot, SQLite, Thymeleaf, HTML/CSS/vanilla JS local hotel system.

## Run in GitHub Codespaces
1. Open terminal at the folder containing `pom.xml`.
2. Check Java: `java -version` (use Java 21).
3. Run `mvn spring-boot:run`.
4. Open port 8080 from the Codespaces Ports tab.

## Demo manager
Email: `manager@mtchlvr.local`
Password: `ChangeMe123!`
Change the demo password before using real customer information.

## Seeded rooms
12 rooms, 3 per type:
- Standard 101–103 — ₱1,800/night, capacity 2
- Deluxe 201–203 — ₱2,600/night, capacity 2
- Suite 301–303 — ₱4,200/night, capacity 2
- Family 401–403 — ₱3,500/night, capacity 4

SQLite database: `./data/mtchlvr.db`.

## Customer chatbot
A floating assistant is rendered only for authenticated `CUSTOMER` sessions on customer-facing pages. The chat endpoint is protected by Spring Security and answers only questions about this site's account, email verification, room, reservation, cancellation, upgrade and manager functions. Unrelated questions get a polite scope boundary. This is a rule-based local FAQ assistant, not a generative AI service.

## OTP email
Configure `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, and `MAIL_FROM` environment variables to send real email. If SMTP is unavailable, the OTP is printed to the application console for local development only. OTP is hashed in the database and expires after 10 minutes. Do not use console OTP mode with real customers.

## Scope / security
This is a local project starter, not a production deployment. Passwords are BCrypt-hashed, manager routes require the MANAGER role, and Spring Security CSRF protection is enabled. The chatbot is scoped to hotel functions. Add OTP rate limiting, login throttling, audit logs, transactional reservation locking and backup procedures before any real-world use.
