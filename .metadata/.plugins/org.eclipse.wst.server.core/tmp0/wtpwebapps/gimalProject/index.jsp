<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
    <style>
        body {
            font-family: 'Arial';
            background-color: #f9f9f9;
            text-align: center;
            margin-top: 100px;
        }
        button {
            padding: 10px 20px;
            margin: 10px;
            border: none;
            border-radius: 8px;
            background-color: #007BFF;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

    <h1>Welcome to GimalProject 👋</h1>
    <p>회원 관련 기능을 선택하세요</p>

    <!-- 로그인 페이지 이동 -->
    <form action="<%= request.getContextPath() %>/views/user/login.jsp" method="get">
        <button type="submit">로그인</button>
    </form>

    <!-- 회원가입 페이지 이동 -->
    <form action="<%= request.getContextPath() %>/views/user/register.jsp" method="get">
        <button type="submit">회원가입</button>
    </form>

</body>
</html>
