<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="post" value="${requestScope.post}" />
<c:set var="isOwner" value="${requestScope.isOwner}" />

<%
	Object loginUser = session.getAttribute("userInfo");
    boolean isLogin = (loginUser != null);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${fn:escapeXml(post.title)} - 드림 상세</title>
  <link rel="stylesheet" href="${ctx}/resources/css/dream-list.css">
</head>
<body>

  <jsp:include page="/include/header.jsp" />

  <main class="dream-detail-page">
    <div class="dream-detail-shell">

      <!-- 상단 바: 뒤로가기 / 상태 배지 -->
      <header class="dream-detail-topbar">
        <a href="${ctx}/dream/list.do" class="dream-detail-back">
          <span class="dream-detail-back-icon">&#x2190;</span>
          <span class="dream-detail-back-text">드림 목록</span>
        </a>

        <c:choose>
          <c:when test="${post.status eq 'OPEN'}">
            <span class="dream-detail-pill is-open">나눔중</span>
          </c:when>
          <c:when test="${post.status eq 'CLOSE'}">
            <span class="dream-detail-pill is-closed">나눔완료</span>
          </c:when>
          <c:otherwise>
            <span class="dream-detail-pill">${fn:escapeXml(post.status)}</span>
          </c:otherwise>
        </c:choose>
      </header>

      <!-- 메인 레이아웃: 좌(이미지) / 우(정보) -->
      <section class="dream-detail-main">

        <!-- 왼쪽: 이미지 섹션 -->
        <div class="dream-detail-left">
          <c:choose>
            <c:when test="${not empty post.imagesUrl}">
              <div class="dream-detail-image-slider-wrapper">
                <div class="dream-detail-image-slider" id="detailSlider">
                  <div class="dream-detail-image-track" id="detailTrack">
                    <c:forEach var="imgUrl" items="${post.imagesUrl}">
                      <div class="dream-detail-image-slide">
                        <img src="${imgUrl}"
                             alt="${fn:escapeXml(post.title)}"
                             class="dream-detail-img" />
                      </div>
                    </c:forEach>
                  </div>

                  <c:if test="${fn:length(post.imagesUrl) > 1}">
                    <!-- 왼쪽/오른쪽으로 넘기는 버튼 -->
                    <button type="button"
                            class="dream-detail-image-arrow is-left"
                            id="detailPrev"
                            aria-label="이전 이미지">
                      &#x2039;
                    </button>
                    <button type="button"
                            class="dream-detail-image-arrow is-right"
                            id="detailNext"
                            aria-label="다음 이미지">
                      &#x203A;
                    </button>
                  </c:if>
                </div>

                <!-- 썸네일 리스트 -->
                <c:if test="${fn:length(post.imagesUrl) > 1}">
                  <div class="dream-detail-thumbs">
                    <c:forEach var="imgUrl" items="${post.imagesUrl}" varStatus="loop">
                      <button type="button"
                              class="dream-detail-thumb-btn <c:if test='${loop.index == 0}'>is-active</c:if>"
                              data-index="${loop.index}">
                        <img src="${imgUrl}"
                             alt="${fn:escapeXml(post.title)}"
                             class="dream-detail-thumb-img" />
                      </button>
                    </c:forEach>
                  </div>
                </c:if>
              </div>
            </c:when>
            <c:otherwise>
              <div class="dream-detail-image-slider-wrapper">
                <div class="dream-detail-image-slider dream-detail-image-empty">
                  <span>등록된 이미지가 없습니다.</span>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>

        <!-- 오른쪽: 정보 섹션 -->
        <div class="dream-detail-right">

          <h1 class="dream-detail-title">
            ${fn:escapeXml(post.title)}
          </h1>

          <div class="dream-detail-meta-row">
            <span class="dream-detail-location">${fn:escapeXml(post.dong)}</span>
            <span class="dream-detail-dot">·</span>
            <span class="dream-detail-time">${post.timeAgoLabel}</span>
            <span class="dream-detail-dot">·</span>
            <span class="dream-detail-views">조회 ${post.viewCount}회</span>
          </div>

          <div class="dream-detail-chips">
            <span class="dream-detail-chip">
              카테고리 · ${fn:escapeXml(post.categoryCode)}
            </span>
            <span class="dream-detail-chip">
              상태 · ${fn:escapeXml(post.conditionCode)}
            </span>
          </div>

          <div class="dream-detail-price-row">
            <c:choose>
              <%-- 글 작성자: 가격 대신 상태 토글 --%>
              <c:when test="${isOwner}">
                <form action="${ctx}/dream/status.do"
                      method="post"
                      class="dream-detail-status-inline">
                  <input type="hidden" name="dreamId" value="${post.dreamId}" />

                  <div class="dream-detail-status-toggle">
                    <button type="submit"
                            name="status"
                            value="OPEN"
                            class="dream-detail-status-btn<c:if test='${post.status eq "OPEN"}'> is-active</c:if>">
                      나눔중
                    </button>

                    <button type="submit"
                            name="status"
                            value="CLOSE"
                            class="dream-detail-status-btn<c:if test='${post.status eq "CLOSE"}'> is-active is-closed</c:if>"
                            onclick="return confirm('이 게시글을 나눔완료 상태로 변경하시겠습니까?');">
                      나눔완료
                    </button>
                  </div>
                </form>
              </c:when>

              <%-- 다른 사용자: 무료 나눔 텍스트 --%>
              <c:otherwise>
                <span class="dream-detail-price is-free">무료 나눔</span>
              </c:otherwise>
            </c:choose>
          </div>

          <div class="dream-detail-divider"></div>

          <section class="dream-detail-section">
            <h2 class="dream-detail-section-title">상세 설명</h2>
            <pre class="dream-detail-content">
${fn:escapeXml(post.content)}
            </pre>
          </section>

          <div class="dream-detail-divider"></div>

          <!-- CTA 영역 -->
          <div class="dream-detail-cta">
            <!-- 채팅하기 버튼 (기능 X, UI만) -->
			<form id="chatForm" method="post" action="${ctx}/chat/private" target="chatPopup">
			  <input type="hidden" name="itemId" value="${post.dreamId}">
			  <input type="hidden" name="hostId" value="${post.writerId}">
			</form>
			
			<a href="javascript:void(0)"
			   class="dream-detail-chat-btn"
			   onclick="
			     window.open('', 'chatPopup',
			       'width=430,height=640,top=100,left=100,scrollbars=yes');
			     document.getElementById('chatForm').submit();
			   ">
			   채팅하기
			</a>

            <c:if test="${isOwner}">      	
              <div class="dream-detail-owner-actions">
                <a href="${ctx}/dream/edit.do?dreamId=${post.dreamId}"
                   class="dream-detail-owner-link">
                  게시글 수정
                </a>

                <form action="${ctx}/dream/delete.do"
                      method="post"
                      class="dream-detail-owner-delete"
                      onsubmit="return confirm('정말로 이 게시글을 삭제하시겠습니까?');">
                  <input type="hidden" name="dreamId" value="${post.dreamId}" />
                  <button type="submit" class="dream-detail-owner-link is-danger">
                    삭제하기
                  </button>
                </form>
              </div>
            </c:if>
          </div>
        </div>
      </section>
    </div>
  </main>

  <!-- 이미지 슬라이더 스크립트 -->
  <script>
    (function () {
      var track = document.getElementById("detailTrack");
      if (!track) return;

      var slides = track.querySelectorAll(".dream-detail-image-slide");
      var total  = slides.length;
      if (total === 0) return;

      var prevBtn = document.getElementById("detailPrev");
      var nextBtn = document.getElementById("detailNext");
      var thumbBtns = document.querySelectorAll(".dream-detail-thumb-btn");
      var current = 0;

      function update() {
        track.style.transform = "translateX(" + (-current * 100) + "%)";
        if (prevBtn) {
          prevBtn.disabled = current === 0;
          prevBtn.classList.toggle("is-disabled", current === 0);
        }
        if (nextBtn) {
          nextBtn.disabled = current === total - 1;
          nextBtn.classList.toggle("is-disabled", current === total - 1);
        }
        if (thumbBtns && thumbBtns.length > 0) {
          for (var i = 0; i < thumbBtns.length; i++) {
            if (i === current) {
              thumbBtns[i].classList.add("is-active");
            } else {
              thumbBtns[i].classList.remove("is-active");
            }
          }
        }
      }

      if (prevBtn) {
        prevBtn.addEventListener("click", function () {
          if (current > 0) {
            current--;
            update();
          }
        });
      }

      if (nextBtn) {
        nextBtn.addEventListener("click", function () {
          if (current < total - 1) {
            current++;
            update();
          }
        });
      }

      if (thumbBtns && thumbBtns.length > 0) {
        for (var i = 0; i < thumbBtns.length; i++) {
          (function (index) {
            thumbBtns[index].addEventListener("click", function () {
              current = index;
              update();
            });
          })(i);
        }
      }

      update();
    })();
  </script>

</body>
</html>
