using Grpc.Net.Client;
// Namespace Bds360 này được tự động sinh ra từ dòng "package bds360;" trong file .proto
using Bds360; 

using Bds360.Grpc;

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

// Tạo 1 REST API bên C# để test gọi gRPC sang Java
app.MapGet("/api/v1/statistics/test-grpc", async () =>
{
    try
    {
        // 1. Mở kênh kết nối thẳng tới cổng 9092 của Listing Service (Java)
        using var channel = GrpcChannel.ForAddress("http://localhost:9092");
        
        // 2. Khởi tạo Client dựa trên Service đã định nghĩa trong file .proto
        var client = new InternalStatisticService.InternalStatisticServiceClient(channel);

        // 3. Gọi hàm GetTotalPostsAsync (C# tự động thêm chữ Async vào cuối tên hàm)
        var reply = await client.GetTotalPostsAsync(new EmptyRequest());
        
        // 4. Trả về kết quả JSON cho trình duyệt
        return Results.Ok(new {
            message = "Thành công! C# đã lấy được dữ liệu từ Java qua gRPC.",
            totalPostsFromJava = reply.TotalCount
        });
    }
    catch (Exception ex)
    {
        return Results.Problem($"Lỗi gọi gRPC: {ex.Message}");
    }
});

app.Run();