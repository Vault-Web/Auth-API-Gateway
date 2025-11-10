# Project Structure

## apigateway

- 📁 **src**
  - 📁 **main**
    - 📁 **java**
      - 📁 **vaultweb**
        - 📁 **apigateway**
          - 📄 [ApiGatewayApplication.java](apigateway/src/main/java/vaultweb/apigateway/ApiGatewayApplication.java)
          - 📁 **config**
            - 📄 [GatewayAuthConfig.java](apigateway/src/main/java/vaultweb/apigateway/config/GatewayAuthConfig.java)
          - 📁 **controller**
            - 📄 [GatewayAuthController.java](apigateway/src/main/java/vaultweb/apigateway/controller/GatewayAuthController.java)
            - 📄 [GatewayCloudController.java](apigateway/src/main/java/vaultweb/apigateway/controller/GatewayCloudController.java)
            - 📄 [GatewayPasswordManagerController.java](apigateway/src/main/java/vaultweb/apigateway/controller/GatewayPasswordManagerController.java)
          - 📁 **exceptions**
            - 📄 [GlobalExceptionHandler.java](apigateway/src/main/java/vaultweb/apigateway/exceptions/GlobalExceptionHandler.java)
          - 📁 **model**
            - 📄 [User.java](apigateway/src/main/java/vaultweb/apigateway/model/User.java)
          - 📁 **service**
            - 📄 [AuthService.java](apigateway/src/main/java/vaultweb/apigateway/service/AuthService.java)
            - 📄 [RoutingService.java](apigateway/src/main/java/vaultweb/apigateway/service/RoutingService.java)
          - 📁 **util**
            - 📄 [BcryptUtil.java](apigateway/src/main/java/vaultweb/apigateway/util/BcryptUtil.java)
            - 📄 [JwtUtil.java](apigateway/src/main/java/vaultweb/apigateway/util/JwtUtil.java)
  - 📁 **test**
    - 📁 **java**
      - 📁 **vaultweb**
        - 📁 **apigateway**
          - 📄 [ApigatewayApplicationTests.java](apigateway/src/test/java/vaultweb/apigateway/ApigatewayApplicationTests.java)
