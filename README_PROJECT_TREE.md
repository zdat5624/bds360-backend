
```
bds360-backend
├─ api-gateway
│  ├─ gradle
│  │  └─ wrapper
│  │     ├─ gradle-wrapper.jar
│  │     └─ gradle-wrapper.properties
│  ├─ gradlew
│  ├─ gradlew.bat
│  ├─ HELP.md
│  └─ src
│     ├─ main
│     │  ├─ java
│     │  │  └─ com
│     │  │     └─ example
│     │  │        └─ api_gateway
│     │  │           └─ ApiGatewayApplication.java
│     │  └─ resources
│     │     └─ application.yml
│     └─ test
│        └─ java
│           └─ com
│              └─ example
│                 └─ api_gateway
│                    └─ ApiGatewayApplicationTests.java
├─ bds360.code-workspace
├─ docker
│  └─ mysql
│     └─ init
│        └─ init.sql
├─ docker-compose.yml
├─ identity-service
│  ├─ gradle
│  │  └─ wrapper
│  │     ├─ gradle-wrapper.jar
│  │     └─ gradle-wrapper.properties
│  ├─ gradlew
│  ├─ gradlew.bat
│  ├─ HELP.md
│  └─ src
│     ├─ main
│     │  ├─ java
│     │  │  └─ com
│     │  │     └─ example
│     │  │        └─ identity_service
│     │  │           ├─ grpc
│     │  │           │  └─ InternalIdentityGrpcService.java
│     │  │           ├─ IdentityServiceApplication.java
│     │  │           └─ modules
│     │  │              └─ user
│     │  │                 └─ controller
│     │  │                    └─ TestController.java
│     │  └─ resources
│     │     ├─ application.yaml
│     │     ├─ static
│     │     └─ templates
│     └─ test
│        └─ java
│           └─ com
│              └─ example
│                 └─ identity_service
│                    └─ IdentityServiceApplicationTests.java
├─ listing-service
│  ├─ gradle
│  │  └─ wrapper
│  │     ├─ gradle-wrapper.jar
│  │     └─ gradle-wrapper.properties
│  ├─ gradlew
│  ├─ gradlew.bat
│  ├─ HELP.md
│  └─ src
│     ├─ main
│     │  ├─ java
│     │  │  └─ com
│     │  │     └─ example
│     │  │        └─ listing_service
│     │  │           ├─ ListingServiceApplication.java
│     │  │           └─ modules
│     │  │              └─ post
│     │  │                 └─ grpc
│     │  │                    └─ InternalStatisticGrpcService.java
│     │  └─ resources
│     │     ├─ application.yml
│     │     ├─ static
│     │     └─ templates
│     └─ test
│        └─ java
│           └─ com
│              └─ example
│                 └─ listing_service
│                    └─ ListingServiceApplicationTests.java
├─ media-service
│  ├─ .env.local
│  ├─ .prettierrc
│  ├─ eslint.config.mjs
│  ├─ nest-cli.json
│  ├─ package-lock.json
│  ├─ package.json
│  ├─ README.md
│  ├─ src
│  │  ├─ app.controller.ts
│  │  ├─ app.module.ts
│  │  ├─ app.service.ts
│  │  ├─ main.ts
│  │  └─ modules
│  │     └─ media
│  │        ├─ media.controller.ts
│  │        └─ media.service.ts
│  ├─ test
│  │  ├─ app.e2e-spec.ts
│  │  └─ jest-e2e.json
│  └─ tsconfig.json
├─ notification-service
│  ├─ .prettierrc
│  ├─ eslint.config.mjs
│  ├─ nest-cli.json
│  ├─ package-lock.json
│  ├─ package.json
│  ├─ README.md
│  ├─ src
│  │  ├─ app.controller.ts
│  │  ├─ app.module.ts
│  │  ├─ app.service.ts
│  │  └─ main.ts
│  ├─ test
│  │  ├─ app.e2e-spec.ts
│  │  └─ jest-e2e.json
│  └─ tsconfig.json
├─ payment-service
│  ├─ gradle
│  │  └─ wrapper
│  │     ├─ gradle-wrapper.jar
│  │     └─ gradle-wrapper.properties
│  ├─ gradlew
│  ├─ gradlew.bat
│  ├─ HELP.md
│  └─ src
│     ├─ main
│     │  ├─ java
│     │  │  └─ com
│     │  │     └─ example
│     │  │        └─ payment_service
│     │  │           └─ PaymentServiceApplication.java
│     │  └─ resources
│     │     ├─ application.yml
│     │     ├─ static
│     │     └─ templates
│     └─ test
│        └─ java
│           └─ com
│              └─ example
│                 └─ payment_service
│                    └─ PaymentServiceApplicationTests.java
├─ README_PROJECT_TREE.md
├─ statistic-service
│  ├─ appsettings.Development.json
│  ├─ appsettings.json
│  ├─ Program.cs
│  ├─ Properties
│  │  └─ launchSettings.json
│  ├─ Protos
│  │  └─ greet.proto
│  ├─ Services
│  │  └─ GreeterService.cs
│  └─ statistic-service.csproj
└─ _shared
   └─ proto
      ├─ bds360_internal.proto
      ├─ common.proto
      ├─ identity.proto
      ├─ listing.proto
      ├─ notification.proto
      └─ statistic.proto

```