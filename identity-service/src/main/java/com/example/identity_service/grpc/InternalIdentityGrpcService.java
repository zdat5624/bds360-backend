package com.example.identity_service.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.bds360.backend.grpc.InternalIdentityServiceGrpc;
import vn.bds360.backend.grpc.TokenValidationRequest;
import vn.bds360.backend.grpc.TokenValidationResponse;

@GrpcService
public class InternalIdentityGrpcService extends InternalIdentityServiceGrpc.InternalIdentityServiceImplBase {

    @Override
    public void validateToken(TokenValidationRequest request,
            StreamObserver<TokenValidationResponse> responseObserver) {
        // Tạm thời giả lập logic check token, sau này bốc JWT service từ monolithic
        // sang
        String token = request.getToken();
        boolean isValid = "valid-token-example".equals(token);

        TokenValidationResponse response = TokenValidationResponse.newBuilder()
                .setIsValid(isValid)
                .setUserId(isValid ? 1L : 0L)
                .setRole(isValid ? "ADMIN" : "")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}