# chatappexample

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.native.enabled=true
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/chatappexample-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/gradle-tooling>.

## Related Guides

- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin
- Hibernate Reactive with Panache ([guide](https://quarkus.io/guides/hibernate-reactive-panache)): Reactive ORM with PostgreSQL
- SmallRye JWT ([guide](https://quarkus.io/guides/security-jwt)): JWT RBAC authentication
- SmallRye JWT Build ([guide](https://quarkus.io/guides/security-jwt-build)): Sign and encrypt JWTs

## Endpoints

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| POST | `/api/auth/register` | None | Register a new user |
| POST | `/api/auth/login` | None | Login, returns JWT token |
| POST | `/api/chat/send` | JWT Bearer | Send a chat message |
| WS | `/chat/{userId}` | None | WebSocket chat connection |

### Usage

1. Start infrastructure: `docker compose up -d`
2. Start app: `./gradlew quarkusDev`
3. Register: `curl -X POST http://localhost:8080/api/auth/register -H 'Content-Type: application/json' -d '{"username":"alice","password":"secret"}'`
4. Login: `curl -X POST http://localhost:8080/api/auth/login -H 'Content-Type: application/json' -d '{"username":"alice","password":"secret"}'`
5. Use token: `curl -X POST http://localhost:8080/api/chat/send -H 'Authorization: Bearer <token>' -H 'Content-Type: application/json' -d '{...}'`
