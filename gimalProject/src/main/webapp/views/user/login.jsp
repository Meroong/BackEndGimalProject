<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<style>
    @charset "UTF-8";

    /* ==================== 전체 스타일 ==================== */
    body {
        margin: 0;
        padding: 0;
        background: #F5F6FA;
        font-family: 'Pretendard', sans-serif;
        color: #222;
    }

    /* 컨테이너 중앙 정렬 */
    .container {
        width: 360px;
        max-width: 90%;
        margin: 80px auto;
        background: #fff;
        padding: 40px;
        border-radius: 20px;
        box-shadow: 0 6px 24px rgba(0,0,0,0.06);
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    /* 제목 중앙 정렬 */
    h2 {
        text-align: center;
        font-size: 28px;
        font-weight: 700;
        color: #FF7C40;
        margin-bottom: 30px;
    }

    /* 폼 전체 */
    form {
        width: 100%;
        display: flex;
        flex-direction: column;
        gap: 20px; /* 레이블과 입력 필드 간격 */
    }

    label {
        font-weight: 600;
        color: #555;
    }

    input[type="text"], input[type="password"], button {
        width: 100%;
        box-sizing: border-box; /* padding 포함 너비 계산 */
    }

    input[type="text"], input[type="password"] {
        padding: 12px 16px;
        border-radius: 12px;
        border: 1px solid #DDD;
        font-size: 15px;
        outline: none;
        transition: 0.15s;
    }

    input[type="text"]:focus, input[type="password"]:focus {
        border-color: #FF7C40;
    }

    button {
        padding: 12px;
        border: none;
        border-radius: 12px;
        background: #FF7C40;
        color: white;
        font-size: 16px;
        font-weight: 700;
        cursor: pointer;
        transition: 0.15s;
    }

    button:hover {
        background: #ff6720;
    }

    /* 회원가입 링크 */
    p {
        text-align: center;
        margin-top: 20px;
        font-size: 14px;
        color: #666;
    }

    a {
        color: #5271FF;
        font-weight: 600;
        text-decoration: none;
        transition: 0.15s;
    }

    a:hover {
        text-decoration: underline;
    }
</style>
</head>
<body>

<% 
    String errorMsg = (String) request.getAttribute("errorMsg");
    if (errorMsg != null) { 
%>
    <script>
        alert("<%= errorMsg %>");
    </script>
<%
    }
%>

<div class="container">
    <h2>로그인</h2>
    <form action="<%= request.getContextPath() %>/user/login" method="post">
        <div>
            <label>아이디</label>
            <input type="text" name="userId" placeholder="아이디를 입력하세요" required>
        </div>

        <div>
            <label>비밀번호</label>
            <input type="password" name="userPassword" placeholder="비밀번호를 입력하세요" required>
        </div>

        <button type="submit">로그인</button>
    </form>

    <p>회원이 아니신가요? <a href="register.jsp">회원가입</a></p>
</div>

</body>
</html>
