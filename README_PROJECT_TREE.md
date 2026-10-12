
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
│  └─ src
│     ├─ main
│     │  ├─ java
│     │  │  └─ com
│     │  │     └─ example
│     │  │        └─ identity_service
│     │  │           ├─ bootstrap
│     │  │           │  └─ StartupRunner.java
│     │  │           ├─ common
│     │  │           │  ├─ annotation
│     │  │           │  │  └─ ApiGlobalResponse.java
│     │  │           │  ├─ config
│     │  │           │  │  ├─ AppProperties.java
│     │  │           │  │  ├─ CorsConfig.java
│     │  │           │  │  └─ OpenApiConfig.java
│     │  │           │  ├─ constant
│     │  │           │  │  ├─ ListingType.java
│     │  │           │  │  ├─ NotificationType.java
│     │  │           │  │  └─ Role.java
│     │  │           │  ├─ dto
│     │  │           │  │  ├─ request
│     │  │           │  │  │  └─ BaseFilterRequest.java
│     │  │           │  │  └─ response
│     │  │           │  │     ├─ ApiResponse.java
│     │  │           │  │     └─ PageResponse.java
│     │  │           │  ├─ exception
│     │  │           │  │  ├─ AppException.java
│     │  │           │  │  ├─ CustomErrorController.java
│     │  │           │  │  ├─ ErrorCode.java
│     │  │           │  │  └─ GlobalExceptionHandler.java
│     │  │           │  ├─ mapper
│     │  │           │  │  ├─ EntityResolver.java
│     │  │           │  │  └─ MapperConfiguration.java
│     │  │           │  └─ util
│     │  │           ├─ grpc
│     │  │           │  └─ InternalIdentityGrpcService.txt
│     │  │           ├─ IdentityServiceApplication.java
│     │  │           ├─ modules
│     │  │           │  ├─ auth
│     │  │           │  │  ├─ controller
│     │  │           │  │  │  └─ AuthController.java
│     │  │           │  │  ├─ dto
│     │  │           │  │  │  ├─ request
│     │  │           │  │  │  │  ├─ ChangePasswordRequest.java
│     │  │           │  │  │  │  ├─ ForgotPasswordRequest.java
│     │  │           │  │  │  │  ├─ GoogleLoginRequest.java
│     │  │           │  │  │  │  ├─ LoginRequest.java
│     │  │           │  │  │  │  ├─ RegisterRequest.java
│     │  │           │  │  │  │  └─ ResetPasswordRequest.java
│     │  │           │  │  │  └─ response
│     │  │           │  │  │     └─ AuthResponse.java
│     │  │           │  │  ├─ entity
│     │  │           │  │  │  └─ PasswordResetToken.java
│     │  │           │  │  ├─ mapper
│     │  │           │  │  ├─ repository
│     │  │           │  │  │  └─ PasswordResetTokenRepository.java
│     │  │           │  │  └─ service
│     │  │           │  │     ├─ AuthService.java
│     │  │           │  │     └─ ForgotPasswordService.java
│     │  │           │  └─ user
│     │  │           │     ├─ constant
│     │  │           │     │  ├─ Gender.java
│     │  │           │     │  └─ VerificationStatus.java
│     │  │           │     ├─ controller
│     │  │           │     │  └─ TestController.java
│     │  │           │     ├─ dto
│     │  │           │     │  ├─ request
│     │  │           │     │  │  ├─ CreateUserRequest.java
│     │  │           │     │  │  ├─ ReviewVerificationRequest.java
│     │  │           │     │  │  ├─ SubmitVerificationRequest.java
│     │  │           │     │  │  ├─ UpdateProfileRequest.java
│     │  │           │     │  │  ├─ UpdateUserRequest.java
│     │  │           │     │  │  ├─ UserFilterRequest.java
│     │  │           │     │  │  └─ VerificationFilterRequest.java
│     │  │           │     │  └─ response
│     │  │           │     │     ├─ UserResponse.java
│     │  │           │     │     └─ VerificationResponse.java
│     │  │           │     ├─ entity
│     │  │           │     │  ├─ User.java
│     │  │           │     │  └─ VerificationSubmission.java
│     │  │           │     ├─ mapper
│     │  │           │     │  ├─ UserMapper.java
│     │  │           │     │  └─ VerificationMapper.java
│     │  │           │     ├─ repository
│     │  │           │     │  ├─ UserRepository.java
│     │  │           │     │  └─ VerificationSubmissionRepository.java
│     │  │           │     ├─ service
│     │  │           │     │  ├─ UserService.java
│     │  │           │     │  └─ VerificationService.java
│     │  │           │     └─ specification
│     │  │           │        ├─ UserSpecification.java
│     │  │           │        └─ VerificationSpecification.java
│     │  │           └─ security
│     │  │              ├─ annotation
│     │  │              │  ├─ CurrentUser.java
│     │  │              │  ├─ IsAdmin.java
│     │  │              │  ├─ IsAdminOrModerator.java
│     │  │              │  └─ RequireLogin.java
│     │  │              ├─ config
│     │  │              │  ├─ GoogleProperties.java
│     │  │              │  └─ JwtProperties.java
│     │  │              ├─ CustomAccessDeniedHandler.java
│     │  │              ├─ CustomAuthenticationEntryPoint.java
│     │  │              ├─ CustomUserDetailsService.java
│     │  │              ├─ resolver
│     │  │              ├─ SecurityConfiguration.java
│     │  │              └─ SecurityService.java
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
│     │  │                    └─ InternalStatisticGrpcService.java.txt
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
│     │  │           ├─ common
│     │  │           │  ├─ annotation
│     │  │           │  │  └─ ApiGlobalResponse.java
│     │  │           │  ├─ config
│     │  │           │  │  ├─ AppProperties.java
│     │  │           │  │  ├─ CorsConfig.java
│     │  │           │  │  └─ OpenApiConfig.java
│     │  │           │  ├─ constant
│     │  │           │  │  ├─ ListingType.java
│     │  │           │  │  ├─ NotificationType.java
│     │  │           │  │  └─ Role.java
│     │  │           │  ├─ dto
│     │  │           │  │  ├─ request
│     │  │           │  │  │  └─ BaseFilterRequest.java
│     │  │           │  │  └─ response
│     │  │           │  │     ├─ ApiResponse.java
│     │  │           │  │     └─ PageResponse.java
│     │  │           │  ├─ exception
│     │  │           │  │  ├─ AppException.java
│     │  │           │  │  ├─ CustomErrorController.java
│     │  │           │  │  ├─ ErrorCode.java
│     │  │           │  │  └─ GlobalExceptionHandler.java
│     │  │           │  ├─ mapper
│     │  │           │  │  ├─ EntityResolver.java
│     │  │           │  │  └─ MapperConfiguration.java
│     │  │           │  └─ util
│     │  │           ├─ modules
│     │  │           │  └─ transaction
│     │  │           │     ├─ config
│     │  │           │     │  └─ VnPayProperties.java
│     │  │           │     ├─ constant
│     │  │           │     │  ├─ TransactionStatus.java
│     │  │           │     │  └─ TransactionType.java
│     │  │           │     ├─ controller
│     │  │           │     │  ├─ PaymentController.java
│     │  │           │     │  └─ TransactionController.java
│     │  │           │     ├─ dto
│     │  │           │     │  ├─ request
│     │  │           │     │  │  ├─ CreatePaymentRequest.java
│     │  │           │     │  │  └─ TransactionFilterRequest.java
│     │  │           │     │  └─ response
│     │  │           │     │     ├─ PaymentLinkResponse.java
│     │  │           │     │     └─ TransactionResponse.java
│     │  │           │     ├─ entity
│     │  │           │     │  └─ Transaction.java
│     │  │           │     ├─ mapper
│     │  │           │     │  └─ TransactionMapper.java
│     │  │           │     ├─ repository
│     │  │           │     │  └─ TransactionRepository.java
│     │  │           │     ├─ service
│     │  │           │     │  ├─ TransactionService.java
│     │  │           │     │  └─ VNPAYService.java
│     │  │           │     ├─ specification
│     │  │           │     │  └─ TransactionSpecification.java
│     │  │           │     └─ util
│     │  │           │        └─ VnPayUtil.java
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