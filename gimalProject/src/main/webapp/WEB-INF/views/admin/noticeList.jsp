<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.AdminNoticeDTO" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>도란도란 - 공지사항 목록</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/home.css">
</head>
<body>

<div class="container">

    <header>
        <div class="logo">
            <img src="<%=request.getContextPath()%>/resources/images/logo.png" alt="logo">
            도란도란 관리자
        </div>
        <div class="header-buttons">
            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin'">
                관리자 메인
            </button>

            <button class="log-btn"
                    onclick="location.href='<%=request.getContextPath()%>/admin/notices/write'">
                공지 작성
            </button>
        </div>
    </header>

    <section class="main-box">
        <div class="box-title">공지사항 목록</div>

        <table border="1" style="width:100%; border-collapse:collapse; text-align:center;">
            <thead>
                <tr style="background:#f3f4f6;">
                    <th>ID</th>
                    <th>제목</th>
                    <th>작성일</th>
                    <th>삭제</th>
                </tr>
            </thead>

            <tbody>
            <%
                List<AdminNoticeDTO> noticeList = (List<AdminNoticeDTO>) request.getAttribute("noticeList");

                if (noticeList == null || noticeList.isEmpty()) {
            %>
                <tr>
                    <td colspan="4">등록된 공지사항이 없습니다.</td>
                </tr>
            <%
                } else {
                    for (AdminNoticeDTO n : noticeList) {
            %>
                <tr>
                    <td><%= n.getId() %></td>
                    <td><%= n.getTitle() %></td>
                    <td><%= n.getCreatedAt() %></td>

                    <!-- 삭제 버튼 -->
                    <td>
                        <a href="<%=request.getContextPath()%>/admin/notices/delete?id=<%= n.getId() %>"
                           onclick="return confirm('정말 삭제하시겠습니까?');">
                            삭제
                        </a>
                    </td>
                </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>

    </section>

</div>

</body>
</html>
