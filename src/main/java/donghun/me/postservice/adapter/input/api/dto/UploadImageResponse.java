package donghun.me.postservice.adapter.input.api.dto;

import lombok.Getter;

public class UploadImageResponse {
    @Getter
    public static class Response {
        private final String path;

        private Response(String path) {
            this.path = path;
        }

        public static BaseResponse<UploadImageResponse.Response> success(String path) {
            return BaseResponse.success("이미지를 업로드 하였습니다..", new UploadImageResponse.Response(path));
        }
    }
}
