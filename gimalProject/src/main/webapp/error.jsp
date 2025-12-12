<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>오류 - 도란도란</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resources/css/common.css">
</head>
<body>

<jsp:include page="/include/header.jsp" />

<main class="page">
  <div class="container center center--page">
    <div class="panel notice" style="max-width: 720px; width: 100%;">
      <div class="stack stack--lg">
        <div>
          <div class="kicker">ERROR</div>
          <h1 class="notice__title">요청을 처리하지 못했어요</h1>
          <p class="notice__text">
            잠시 후 다시 시도해 주세요. 문제가 계속되면 관리자에게 문의하거나, 아래 버튼으로 이동해 주세요.
          </p>
        </div>

        <div class="divider"></div>

        <div style="display:flex; gap:10px; flex-wrap:wrap; justify-content:flex-end;">
          <a class="btn btn--primary" href="<%= request.getContextPath() %>/index.jsp">홈으로</a>
          <button class="btn btn--outline" type="button" onclick="history.back()">이전으로</button>
        </div>
      </div>
    </div>
  </div>
</main>

<jsp:include page="/include/footer.jsp" />

</body>
</html>
