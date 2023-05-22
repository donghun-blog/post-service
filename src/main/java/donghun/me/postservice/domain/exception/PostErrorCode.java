package donghun.me.postservice.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode {

    TAG_NOT_VALID("유효하지 않는 태그입니다."),
    TAG_NOT_FOUND("태그가 존재하지 않습니다."),
    TAG_MAXIMUM_OVER("최대 등록 가능한 태그 수가 초과하였습니다."),

    POST_TITLE_EMPTY("게시글 제목이 비었습니다."),
    POST_TITLE_LENGTH("게시글 제목의 길이를 확인해주세요."),
    POST_CONTENTS_EMPTY("게시글 본문이 비었습니다."),
    POST_TAG_DUPLICATE("태그가 중복되었습니다."),
    POST_NOT_FOUND("게시글이 존재하지 않습니다."),

    IMAGE_EXTENSION_NOT_SUPPORT("지원하지 않는 이미지 확장자입니다."),
    IMAGE_EMPTY("이미지가 존재하지 않습니다."),
    IMAGE_UPLOAD_FAIL("이미지 업로드에 실패하였습니다.")
    ;


    private final String message;
}
