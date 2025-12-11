<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${empty notice ? '공지 작성' : '공지 수정'}</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/admin.css">
</head>
<body>

<div class="admin-page">
    <div class="admin-page-header">
        <h1 class="admin-page-title">
            ${empty notice ? '공지 작성' : '공지 수정'}
        </h1>
        <p class="admin-page-subtitle">
            관리자 공지사항을 등록·수정하는 페이지입니다.
        </p>
    </div>

    <div class="notice-form-wrapper">
        <div class="notice-card">

            <form action="${pageContext.request.contextPath}/admin/notices/${empty notice ? 'write' : 'edit'}"
                  method="post">

                <input type="hidden" name="id" value="${notice.id}" />

                <div class="notice-field">
                    <label for="title" class="notice-label">제목</label>
                    <input type="text"
                           id="title"
                           name="title"
                           class="notice-input"
                           required
                           placeholder="공지 제목을 입력해 주세요."
                           value="${notice.title}" />
                </div>

                <div class="notice-field">
                    <label for="content" class="notice-label">내용</label>
                    <textarea id="content"
                              name="content"
                              class="notice-textarea"
                              rows="10"
                              required
                              placeholder="공지 내용을 자세히 입력해 주세요.">${notice.content}</textarea>
                </div>

                <div class="notice-actions">
                    <button type="submit" class="btn btn-primary">
                        ${empty notice ? '등록하기' : '수정하기'}
                    </button>
                    <button type="button"
                            class="btn btn-outline"
                            onclick="history.back();">
                        취소
                    </button>
                </div>

            </form>

        </div>
    </div>
</div>

</body>
</html>