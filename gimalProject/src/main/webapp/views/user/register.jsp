<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<style>
    @charset "UTF-8";

    body {
        margin: 0;
        padding: 0;
        background: #F5F6FA;
        font-family: 'Pretendard', sans-serif;
        color: #222;
    }

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

    h2 {
        text-align: center;
        font-size: 28px;
        font-weight: 700;
        color: #FF7C40;
        margin-bottom: 30px;
    }

    form {
        width: 100%;
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    label {
        font-weight: 600;
        color: #555;
    }

    input[type="text"], input[type="password"], button {
        width: 100%;
        box-sizing: border-box;
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
    <script>alert("<%= errorMsg %>");</script>
<%
    }
%>

    <div class="container">
        <h2>회원가입</h2>

        <form action="<%= request.getContextPath() %>/user/register" method="post">
            <div>
                <label>아이디</label>
                <input type="text" name="userId"
                       value="<%= request.getAttribute("userId") != null ? request.getAttribute("userId") : "" %>"
                       placeholder="아이디를 입력하세요" required>
            </div>

            <div>
                <label>비밀번호</label>
                <input type="password" name="userPassword" placeholder="비밀번호를 입력하세요" required>
            </div>

            <div>
                <label>이름</label>
                <input type="text" name="userName"
                       value="<%= request.getAttribute("userName") != null ? request.getAttribute("userName") : "" %>"
                       placeholder="이름을 입력하세요" required>
            </div>

            <div>
                <label>닉네임</label>
                <input type="text" name="nickName"
                       value="<%= request.getAttribute("nickName") != null ? request.getAttribute("nickName") : "" %>"
                       placeholder="닉네임을 입력하세요" required>
            </div>

            <button type="submit">회원가입</button>
        </form>

        <p>이미 계정이 있으신가요? <a href="login.jsp">로그인</a></p>
    </div>
</body>
</html>
