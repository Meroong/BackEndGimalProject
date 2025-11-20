<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메시지함</title>
    <style>
        body {
            background: #F5F6FA;
            font-family: 'Pretendard', sans-serif;
        }
        .container {
            width: 900px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 18px;
            box-shadow: 0 4px 18px rgba(0,0,0,0.08);
        }
        h2 {
            margin-bottom: 25px;
            font-weight: 700;
        }
        .msg-item {
            padding: 15px;
            border-bottom: 1px solid #eee;
        }
        .msg-item:last-child {
            border-bottom: none;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>받은 메시지함</h2>

    <div class="msg-item">메시지 1 - 나중에 DB 연결 예정</div>
    <div class="msg-item">메시지 2 - 나중에 동적 데이터 변환</div>
    <div class="msg-item">메시지 3 - 테스트용</div>
</div>
</body>
</html>
