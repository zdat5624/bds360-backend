using System.Text.Json.Serialization;

namespace StatisticService.Common.Responses;

public class ApiResponse<T>
{
    public int Code { get; set; }
    public string Message { get; set; } = string.Empty;

    [JsonIgnore(Condition = JsonIgnoreCondition.WhenWritingNull)]
    public T? Data { get; set; }

    public static ApiResponse<T> Success(T data, string message = "Thành công")
    {
        return new ApiResponse<T> { Code = 10000, Message = message, Data = data };
    }

    public static ApiResponse<T> Error(int code, string message)
    {
        return new ApiResponse<T> { Code = code, Message = message };
    }
}