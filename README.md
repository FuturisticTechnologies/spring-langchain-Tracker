# Spring LangChain4j Resource Tracker

A Spring Boot application that combines [LangChain4j](https://docs.langchain4j.dev/) with the **Mistral AI** chat model to expose an AI chat assistant that can call Java "tools" to inspect the host machine's live system resources (CPU, memory, disk, JVM, network, etc.), plus a plain REST dashboard endpoint for the same data.

## How it works

```
Client (e.g. Streamlit / curl / Postman)
        |
        v
ChatController  ("/api/chat")
        |
        v
Assistant (LangChain4j AI Service proxy)
        |
        v
MistralAiChatModel  --HTTPS-->  Mistral Cloud LLM
        |
        v
SystemTools1 (@Tool methods: CPU, disk, threads, env vars, etc.)
```

The `Assistant` interface is implemented at runtime by LangChain4j. When the model decides it needs live system data to answer a question, it calls one of the `@Tool`-annotated methods in `SystemTools1`, and the result is sent back to Mistral to produce the final natural-language answer.

There is also a `SystemDashboardController` (`/api/system/**`) that returns the same kind of system metrics directly as JSON, without going through the LLM.

## Prerequisites

- **Java 21** (JDK)
- **Maven** (or use the included wrapper if present; otherwise a local Maven install)
- A **Mistral AI API key** — sign up / generate one at [console.mistral.ai](https://console.mistral.ai/)
- Internet access from the machine running the app (it calls the Mistral Cloud API)

## Project structure

```
spring-langchain4J-resourceTracker/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/proj/ai/
│   │   │   ├── AiApplicationLang4J.java     # Spring Boot entry point
│   │   │   ├── AiConfig.java                # Configures MistralAiChatModel + Assistant beans
│   │   │   ├── Assistant.java               # LangChain4j AI Service interface
│   │   │   ├── ChatController.java          # POST /api/chat — talks to the AI assistant
│   │   │   ├── SystemDashboardController.java # GET /api/system/** — raw system metrics
│   │   │   ├── SystemTools.java             # System metric helper methods
│   │   │   └── SystemTools1.java            # @Tool-annotated methods exposed to the LLM
│   │   └── resources/
│   │       └── application.properties
│   └── test/                                # Unit / integration tests
```

## Configuration

The app reads its Mistral API key from an **environment variable**, referenced in `src/main/resources/application.properties`:

```properties
spring.application.name=SpringAIProject
server.port=8082

spring.ai.mistralai.chat.options.model=mistral-small
spring.threads.virtual.enabled=true
spring.ai.mistralai.api-key=${MISTRAL_API_KEY}
```

You must set the `MISTRAL_API_KEY` environment variable before running the app — see below.

> ⚠️ **Security note:** While reviewing this project, `Langchain4j-Project-flow.txt` at the project root was found to contain a real-looking Mistral API key in plain text. Since this file is likely committed/shared, you should **revoke/rotate that key** in your Mistral console and remove it from the file (and from git history if it was ever committed) before sharing or publishing this repository.

## Running the project

### 1. Set your Mistral API key

**macOS / Linux (bash/zsh):**
```bash
export MISTRAL_API_KEY=your_mistral_api_key_here
```

**Windows (PowerShell):**
```powershell
$env:MISTRAL_API_KEY="your_mistral_api_key_here"
```

**Eclipse IDE:** Run Configurations → Environment tab → add `MISTRAL_API_KEY` with your key as the value.

**IntelliJ IDEA:** Run/Debug Configurations → Environment variables → add `MISTRAL_API_KEY`.

### 2. Build the project

From the project root (where `pom.xml` is located):

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```

Or, after building, run the packaged jar directly:

```bash
java -jar target/spring-langchain4j-resourceTracker-0.0.1-SNAPSHOT.jar
```

The app starts on **port 8082** (configured in `application.properties`).

## Using the API

### Chat with the AI assistant

```bash
curl -X POST http://localhost:8082/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the current CPU usage of this server?"}'
```

Response:
```json
{ "reply": "The current CPU usage is 23.5%, which indicates the server is running normally." }
```

Behind the scenes, the assistant may call tools like `getCpuUsage()`, `getFreeDiskSpace(path)`, `getAvailableProcessors()`, `getEnvironmentVariable(name)`, `isNetworkAvailable()`, `getMacAddress()`, `listDirectory(path)`, `getFileSizeMB(path)`, and JVM/thread metrics — all defined in `SystemTools1.java`.

### Raw system dashboard (no LLM involved)

```bash
curl http://localhost:8082/api/system/dashboard
curl http://localhost:8082/api/system/test
```

`/api/system/dashboard` returns hostname, IP, OS info, CPU/memory/disk usage, JVM info, thread counts, and network info as a single JSON object.

> Note: `SystemDashboardController` currently hardcodes the disk path as `C:\`, so disk metrics from that endpoint are Windows-specific. `SystemTools1`'s `getFreeDiskSpace(path)` tool (used by the chat assistant) accepts any path and works cross-platform.

## CORS

`ChatController` is annotated with `@CrossOrigin(origins = "http://localhost:3000")`, so by default only a frontend running on `http://localhost:3000` (e.g. a React or Streamlit dev server) can call `/api/chat` directly from a browser. Update this annotation if your frontend runs elsewhere.

## Running tests

```bash
mvn test
```

Tests are located under `src/test/java/com/proj/ai/` (`AiConfigIntegrationTest.java`, `SystemToolsTest.java`).
