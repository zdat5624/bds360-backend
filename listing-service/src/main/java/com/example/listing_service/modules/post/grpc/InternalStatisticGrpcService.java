package main.java.com.example.listing_service.modules.post.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.bds360.backend.grpc.EmptyRequest;
import vn.bds360.backend.grpc.InternalStatisticServiceGrpc;
import vn.bds360.backend.grpc.PostCountResponse;

@GrpcService
public class InternalStatisticGrpcService extends InternalStatisticServiceGrpc.InternalStatisticServiceImplBase {

    // TODO: Tiêm PostRepository vào đây sau (private final PostRepository
    // postRepository;)

    @Override
    public void getTotalPosts(EmptyRequest request, StreamObserver<PostCountResponse> responseObserver) {

        // Giả lập logic query DB (Tạm thời hardcode để test luồng thông mạng trước)
        // long realCount = postRepository.count();
        long fakeCount = 1500L;

        // 1. Build dữ liệu trả về theo đúng định dạng Protobuf
        PostCountResponse response = PostCountResponse.newBuilder()
                .setTotalCount(fakeCount)
                .build();

        // 2. Gửi dữ liệu về cho Client
        responseObserver.onNext(response);

        // 3. Đóng kết nối
        responseObserver.onCompleted();
    }
}