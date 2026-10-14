
```
notification-service
├─ .prettierrc
├─ eslint.config.mjs
├─ nest-cli.json
├─ package-lock.json
├─ package.json
├─ README.md
├─ src
│  ├─ app.controller.ts
│  ├─ app.module.ts
│  ├─ app.service.ts
│  ├─ common
│  │  ├─ exception
│  │  │  ├─ app.exception.ts
│  │  │  └─ error-code.enum.ts
│  │  ├─ filter
│  │  │  └─ global-exception.filter.ts
│  │  └─ interceptor
│  │     └─ transform.interceptor.ts
│  ├─ main.ts
│  └─ modules
│     └─ notification
│        ├─ entity
│        │  └─ notification.entity.ts
│        ├─ notification.controller.ts
│        ├─ notification.gateway.ts
│        ├─ notification.module.ts
│        └─ notification.service.ts
├─ test
│  ├─ app.e2e-spec.ts
│  └─ jest-e2e.json
└─ tsconfig.json

```