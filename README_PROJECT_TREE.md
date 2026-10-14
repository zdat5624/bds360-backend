
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
│     │  │           │     │  ├─ TestController.java
│     │  │           │     │  ├─ UserController.java
│     │  │           │     │  └─ VerificationController.java
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
│     │  │              │  ├─ JwtProperties.java
│     │  │              │  └─ WebMvcConfig.java
│     │  │              ├─ CustomAccessDeniedHandler.java
│     │  │              ├─ CustomAuthenticationEntryPoint.java
│     │  │              ├─ CustomUserDetails.java
│     │  │              ├─ CustomUserDetailsService.java
│     │  │              ├─ dto
│     │  │              │  └─ UserContext.java
│     │  │              ├─ resolver
│     │  │              │  └─ CurrentUserArgumentResolver.java
│     │  │              ├─ SecurityConfiguration.java
│     │  │              └─ service
│     │  │                 └─ SecurityService.java
│     │  └─ resources
│     │     ├─ application.yaml
│     │     ├─ data
│     │     │  └─ users.csv
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
│     │  │        ├─ listing_service
│     │  │        │  ├─ bootstrap
│     │  │        │  │  └─ StartupRunner.java
│     │  │        │  ├─ common
│     │  │        │  │  ├─ annotation
│     │  │        │  │  │  └─ ApiGlobalResponse.java
│     │  │        │  │  ├─ config
│     │  │        │  │  │  ├─ AppProperties.java
│     │  │        │  │  │  ├─ CorsConfig.java
│     │  │        │  │  │  └─ OpenApiConfig.java
│     │  │        │  │  ├─ constant
│     │  │        │  │  │  ├─ ListingType.java
│     │  │        │  │  │  ├─ NotificationType.java
│     │  │        │  │  │  └─ Role.java
│     │  │        │  │  ├─ dto
│     │  │        │  │  │  ├─ request
│     │  │        │  │  │  │  └─ BaseFilterRequest.java
│     │  │        │  │  │  └─ response
│     │  │        │  │  │     ├─ ApiResponse.java
│     │  │        │  │  │     └─ PageResponse.java
│     │  │        │  │  ├─ exception
│     │  │        │  │  │  ├─ AppException.java
│     │  │        │  │  │  ├─ CustomErrorController.java
│     │  │        │  │  │  ├─ ErrorCode.java
│     │  │        │  │  │  └─ GlobalExceptionHandler.java
│     │  │        │  │  ├─ mapper
│     │  │        │  │  │  ├─ EntityResolver.java
│     │  │        │  │  │  └─ MapperConfiguration.java
│     │  │        │  │  └─ util
│     │  │        │  ├─ ListingServiceApplication.java
│     │  │        │  ├─ modules
│     │  │        │  │  ├─ address
│     │  │        │  │  │  ├─ config
│     │  │        │  │  │  │  └─ MapboxConfig.java
│     │  │        │  │  │  ├─ controller
│     │  │        │  │  │  │  └─ AddressController.java
│     │  │        │  │  │  ├─ dto
│     │  │        │  │  │  │  ├─ request
│     │  │        │  │  │  │  │  ├─ CreateDistrictRequest.java
│     │  │        │  │  │  │  │  ├─ CreateProvinceRequest.java
│     │  │        │  │  │  │  │  └─ CreateWardRequest.java
│     │  │        │  │  │  │  └─ response
│     │  │        │  │  │  │     ├─ CoordinateResponse.java
│     │  │        │  │  │  │     ├─ DistrictResponse.java
│     │  │        │  │  │  │     ├─ ProvinceResponse.java
│     │  │        │  │  │  │     └─ WardResponse.java
│     │  │        │  │  │  ├─ entity
│     │  │        │  │  │  │  ├─ District.java
│     │  │        │  │  │  │  ├─ Province.java
│     │  │        │  │  │  │  └─ Ward.java
│     │  │        │  │  │  ├─ mapper
│     │  │        │  │  │  │  └─ AddressMapper.java
│     │  │        │  │  │  ├─ repository
│     │  │        │  │  │  │  ├─ DistrictRepository.java
│     │  │        │  │  │  │  ├─ ProvinceRepository.java
│     │  │        │  │  │  │  └─ WardRepository.java
│     │  │        │  │  │  └─ service
│     │  │        │  │  │     ├─ AddressService.java
│     │  │        │  │  │     └─ MapboxGeocodeService.java
│     │  │        │  │  ├─ category
│     │  │        │  │  │  ├─ entity
│     │  │        │  │  │  │  └─ Category.java
│     │  │        │  │  │  └─ repository
│     │  │        │  │  │     └─ CategoryRepository.java
│     │  │        │  │  ├─ post
│     │  │        │  │  │  ├─ constant
│     │  │        │  │  │  │  ├─ CompassDirection.java
│     │  │        │  │  │  │  ├─ Furnishing.java
│     │  │        │  │  │  │  ├─ LegalStatus.java
│     │  │        │  │  │  │  └─ PostStatus.java
│     │  │        │  │  │  ├─ controller
│     │  │        │  │  │  │  ├─ ManagePostController.java
│     │  │        │  │  │  │  ├─ PostAnalyticsController.java
│     │  │        │  │  │  │  ├─ PostController.java
│     │  │        │  │  │  │  └─ SavedPostController.java
│     │  │        │  │  │  ├─ dto
│     │  │        │  │  │  │  ├─ request
│     │  │        │  │  │  │  │  ├─ ForYouPostRequest.java
│     │  │        │  │  │  │  │  ├─ ListingDetailRequest.java
│     │  │        │  │  │  │  │  ├─ PostCreateRequest.java
│     │  │        │  │  │  │  │  ├─ PostFilterRequest.java
│     │  │        │  │  │  │  │  ├─ PriceHistoryRequest.java
│     │  │        │  │  │  │  │  ├─ RelatedPostRequest.java
│     │  │        │  │  │  │  │  ├─ RenewPostRequest.java
│     │  │        │  │  │  │  │  ├─ UpdatePostRequest.java
│     │  │        │  │  │  │  │  └─ UpdatePostStatusRequest.java
│     │  │        │  │  │  │  └─ response
│     │  │        │  │  │  │     ├─ MapPostResponse.java
│     │  │        │  │  │  │     ├─ NearbyLocationPriceResponse.java
│     │  │        │  │  │  │     ├─ PostResponse.java
│     │  │        │  │  │  │     ├─ PostViewChartResponse.java
│     │  │        │  │  │  │     ├─ PriceHistoryResponse.java
│     │  │        │  │  │  │     └─ SavedPostResponse.java
│     │  │        │  │  │  ├─ entity
│     │  │        │  │  │  │  ├─ Image.java
│     │  │        │  │  │  │  ├─ ListingDetail.java
│     │  │        │  │  │  │  ├─ Post.java
│     │  │        │  │  │  │  ├─ PostViewHistory.java
│     │  │        │  │  │  │  └─ SavedPost.java
│     │  │        │  │  │  ├─ grpc
│     │  │        │  │  │  │  └─ InternalStatisticGrpcService.java.txt
│     │  │        │  │  │  ├─ mapper
│     │  │        │  │  │  │  └─ PostMapper.java
│     │  │        │  │  │  ├─ repository
│     │  │        │  │  │  │  ├─ ImageRepository.java
│     │  │        │  │  │  │  ├─ PostRepository.java
│     │  │        │  │  │  │  ├─ PostViewHistoryRepository.java
│     │  │        │  │  │  │  └─ SavedPostRepository.java
│     │  │        │  │  │  ├─ service
│     │  │        │  │  │  │  ├─ PostAnalyticsService.java
│     │  │        │  │  │  │  ├─ PostService.java
│     │  │        │  │  │  │  └─ SavedPostService.java
│     │  │        │  │  │  └─ specification
│     │  │        │  │  │     ├─ ForYouSpecification.java
│     │  │        │  │  │     └─ PostSpecification.java
│     │  │        │  │  └─ vip
│     │  │        │  │     ├─ entity
│     │  │        │  │     │  └─ Vip.java
│     │  │        │  │     └─ repository
│     │  │        │  │        └─ VipRepository.java
│     │  │        │  └─ security
│     │  │        │     ├─ annotation
│     │  │        │     │  ├─ CurrentUser.java
│     │  │        │     │  ├─ IsAdmin.java
│     │  │        │     │  ├─ IsAdminOrModerator.java
│     │  │        │     │  └─ RequireLogin.java
│     │  │        │     ├─ config
│     │  │        │     │  ├─ JwtProperties.java
│     │  │        │     │  └─ WebMvcConfig.java
│     │  │        │     ├─ CustomAccessDeniedHandler.java
│     │  │        │     ├─ CustomAuthenticationEntryPoint.java
│     │  │        │     ├─ dto
│     │  │        │     │  └─ UserContext.java
│     │  │        │     ├─ resolver
│     │  │        │     │  └─ CurrentUserArgumentResolver.java
│     │  │        │     ├─ SecurityConfiguration.java
│     │  │        │     └─ SecurityService.java
│     │  │        └─ payment_service
│     │  │           └─ security
│     │  │              └─ annotation
│     │  └─ resources
│     │     ├─ application.yml
│     │     ├─ data
│     │     │  ├─ address.json
│     │     │  └─ users.csv
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
│  │  ├─ common
│  │  │  ├─ exception
│  │  │  │  ├─ app.exception.ts
│  │  │  │  └─ error-code.enum.ts
│  │  │  ├─ filter
│  │  │  │  └─ global-exception.filter.ts
│  │  │  └─ interceptor
│  │  │     └─ transform.interceptor.ts
│  │  ├─ main.ts
│  │  └─ modules
│  │     └─ media
│  │        ├─ media.controller.ts
│  │        ├─ media.module.ts
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
│     │  │           ├─ PaymentServiceApplication.java
│     │  │           └─ security
│     │  │              ├─ annotation
│     │  │              │  ├─ CurrentUser.java
│     │  │              │  ├─ IsAdmin.java
│     │  │              │  ├─ IsAdminOrModerator.java
│     │  │              │  └─ RequireLogin.java
│     │  │              ├─ config
│     │  │              │  ├─ JwtProperties.java
│     │  │              │  └─ WebMvcConfig.java
│     │  │              ├─ CustomAccessDeniedHandler.java
│     │  │              ├─ CustomAuthenticationEntryPoint.java
│     │  │              ├─ dto
│     │  │              │  └─ UserContext.java
│     │  │              ├─ resolver
│     │  │              │  └─ CurrentUserArgumentResolver.java
│     │  │              ├─ SecurityConfiguration.java
│     │  │              └─ SecurityService.java
│     │  └─ resources
│     │     ├─ application.yml
│     │     ├─ data
│     │     │  └─ users.csv
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
   ├─ data
   │  └─ users.csv
   └─ proto
      ├─ bds360_internal.proto
      ├─ common.proto
      ├─ identity.proto
      ├─ listing.proto
      ├─ notification.proto
      └─ statistic.proto

```