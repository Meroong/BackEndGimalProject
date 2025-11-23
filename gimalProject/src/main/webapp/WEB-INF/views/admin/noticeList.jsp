<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html>
<head>
    <title>공지사항 관리</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: sans-serif; padding: 20px; }
        h2 { margin-bottom: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td {
            border: 1px solid #ddd;
            padding: 8px 10px;
            text-align: left;
        }
        th {
            background-color: #f5f5f5;
        }
        tbody tr:hover {
            background-color: #fafafa;
        }
    </style>
</head>
<body>

<h2>공지사항 관리</h2>

<table>
    <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="n" items="${noticeList}">
            <tr>
                <td>${n.id}</td>
                <td>${n.title}</td>
                <td>${n.writer}</td>
                <td>${n.createdAt}</td>
                <td>${n.hit}</td>
            </tr>
        </c:forEach>

        <c:if test="${empty noticeList}">
            <tr>
                <td colspan="5" style="text-align:center;">등록된 공지사항이 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>

</body>
</html>
