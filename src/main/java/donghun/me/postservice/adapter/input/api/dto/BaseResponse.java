package donghun.me.postservice.adapter.input.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResponse<T> {
    private Result result;
    private T body;

    public static <T> BaseResponse<T> success(String message, T body) {
        return new BaseResponse<>(Result.success(message), body);
    }

    public static <T> BaseResponse<T> success(T body) {
        return success(null, body);
    }

    public static <T> BaseResponse<T> error(String message, T body) {
        return new BaseResponse<>(Result.fail(message), body);
    }
    public static <T> BaseResponse<T> error(String message) {
        return error(message, null);
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder(access = AccessLevel.PROTECTED)
    public static class Result {
        private String status;
        private String message;

        public static Result success(String message) {
            return Result.builder()
                         .status(ResultStatus.SUCCESS.description)
                         .message(message)
                         .build();
        }

        public static Result fail(String message) {
            return Result.builder()
                         .status(ResultStatus.FAIL.description)
                         .message(message)
                         .build();
        }

        @RequiredArgsConstructor
        public enum ResultStatus {
            SUCCESS("success"),
            FAIL("fail")
            ;

            private final String description;
        }

    }
}
