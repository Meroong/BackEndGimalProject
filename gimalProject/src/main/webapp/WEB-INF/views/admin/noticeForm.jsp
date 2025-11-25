<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/admin.css">
    <title>${empty notice ? '공지 작성' : '공지 수정'}</title>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: sans-serif;
            padding: 20px;
        }
        h2 {
            margin-bottom: 20px;
        }
        form {
            max-width: 600px;
        }
        .form-group {
            margin-bottom: 12px;
        }
        label {
            display: block;
            margin-bottom: 4px;
            font-weight: bold;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        textarea {
            height: 200px;
        }
        .btn-wrap {
            margin-top: 16px;
        }
        button {
            padding: 8px 16px;
            cursor: pointer;
        }
    </style>
</head>
<body>

<h2>${empty notice ? '공지 작성' : '공지 수정'}</h2>

<form action="${pageContext.request.contextPath}/admin/notices/${empty notice ? 'write' : 'edit'}" method="post">
    <!-- 수정일 때만 값이 들어가고, 작성일 때는 null이라 신경 안 써도 됩니다 -->
    <input type="hidden" name="id" value="${notice.id}" />

    <div class="form-group">
        <label for="title">제목</label>
        <input type="text"
               id="title"
               name="title"
               required
               value="${notice.title}" />
    </div>

    <div class="form-group">
        <label for="content">내용</label>
        <textarea id="content" name="content" required>${notice.content}</textarea>
    </div>

    <div class="btn-wrap">
        <button type="submit">${empty notice ? '등록하기' : '수정하기'}</button>
        <button type="button" onclick="history.back();">취소</button>
    </div>
</form>

</body>
</html>
