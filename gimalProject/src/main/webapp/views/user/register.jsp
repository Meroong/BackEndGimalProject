<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<style>
    body { font-family: Arial; margin: 50px; }
    .container { width: 300px; margin: auto; }
    input { width: 100%; margin: 5px 0; padding: 8px; }
    button { width: 100%; padding: 10px; background-color: #2196F3; color: white; border: none; }
</style>
</head>
<body>
    <div class="container">
        <h2>회원가입</h2>
		<form action="<%= request.getContextPath() %>/user/register" method="post">
		    <label>아이디</label>
		    <input type="text" name="userId" required>
		
		    <label>비밀번호</label>
		    <input type="password" name="userPassword" required>
		
		    <label>이름</label>
		    <input type="text" name="userName" required>
		
		    <label>닉네임</label>
		    <input type="text" name="nickname" required>
		
		    <button type="submit">회원가입</button>
		</form>


        <p>이미 계정이 있으신가요? <a href="login.jsp">로그인</a></p>
    </div>
</body>
</html>
