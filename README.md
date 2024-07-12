
# 🎭 [Playwright](https://playwright.dev) with Kotlin

**Version: 0.0.0**

This project is a template for using Playwright with Kotlin for end-to-end testing.

## 🚀 Prerequisites

- Java 21 or higher
- Gradle
- Node.js

## 🛠️ Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/playwright-kotlin.git
   cd playwright-kotlin
   ```

2. **Running Tests**

   ```bash
   ./gradlew test
   ```

## 🧑‍💻 **Commands**

1. **To open the Playwright Recorder**, use the following command:

   ```bash
   ./gradlew run --args="open"
   ```

2. **To display the Trace viewer locally**, use the following command:

   **Note:** Replace _{path/file_name}.zip_ with your specific file path.

   ```bash
   ./gradlew run --args="show-trace {path/file_name}.zip"
   ```
