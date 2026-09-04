package com.example.listing_service.modules.post.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.bds360.backend.grpc.EmptyRequest;
import vn.bds360.backend.grpc.InternalStatisticServiceGrpc;
import vn.bds360.backend.grpc.PostCountResponse;

@GrpcService
public class InternalStatisticGrpcService extends InternalStatisticServiceGrpc.InternalStatisticServiceImplBase {

    @Override
    public void getTotalPosts(EmptyRequest request, StreamObserver<PostCountResponse> responseObserver) {
        long fakeCount = 1500L;
        PostCountResponse response = PostCountResponse.newBuilder()
                .setTotalCount(fakeCount)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}