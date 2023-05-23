package donghun.me.postservice.adapter.input.api.dto;

import lombok.Getter;

public class DeletePost {
    @Getter
    public static class Response {
        private final Long postId;

        private Response(Long postId) {
            this.postId = postId;
        }

        public static BaseResponse<DeletePost.Response> success(Long memberId) {
            return BaseResponse.success("게시글을 삭제하였습니다.", new DeletePost.Response(memberId));
        }
    }
}
