<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.AdminNoticeDTO" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <title>도란도란 - 공지사항 목록</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/admin.css">
</head>
<body>

<div class="container">

    <header>
        <h1>관리자 공지사항 관리</h1>
		
        <div class="header-buttons">
        <button class="log-btn" onclick="location.href='<%=request.getContextPath()%>/'">홈으로</button>
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

        <table class="admin-table">
            <thead>
                <tr style="background:#f3f4f6;">
                    <th>ID</th>
                    <th>제목</th>
                    <th>작성일</th>
                    <th>수정</th>   <!-- ✅ 수정 컬럼 추가 -->
                    <th>삭제</th>
                </tr>
            </thead>

            <tbody>
            <%
                List<AdminNoticeDTO> noticeList = (List<AdminNoticeDTO>) request.getAttribute("noticeList");

                if (noticeList == null || noticeList.isEmpty()) {
            %>
                <tr>
                    <!-- ✅ 컬럼 5개라 colspan도 5로 변경 -->
                    <td colspan="5">등록된 공지사항이 없습니다.</td>
                </tr>
            <%
                } else {
                    for (AdminNoticeDTO n : noticeList) {
            %>
                <tr>
                    <td><%= n.getId() %></td>
                    <td><%= n.getTitle() %></td>
                    <td><%= n.getCreatedAt() %></td>

                    <!-- ✅ 수정 버튼 -->
                    <td>
                        <a href="<%=request.getContextPath()%>/admin/notices/edit?id=<%= n.getId() %>">
                            수정
                        </a>
                    </td>

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
