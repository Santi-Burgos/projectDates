package san.projectdates.core.dtos;

public record ApiResponse<T>(
  boolean success,
  String message,
  T data,
  long timestamp
){
  public static <T> ApiResponse<T> success(T data, String message){
    return new ApiResponse<T>(true, message, data, System.currentTimeMillis());
  }

  public static<T> ApiResponse<T> error(String message){
    return new ApiResponse<T>(false, message, null, System.currentTimeMillis());
  }
}
