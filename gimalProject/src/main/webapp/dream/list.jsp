<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="cond" value="${requestScope.cond}" />
<c:set var="loginUser" value="${sessionScope.Authorization}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>드림 - 도란도란</title>

  <link rel="stylesheet" href="${ctx}/resources/css/dream-list.css">
</head>
<body>

  <jsp:include page="/include/header.jsp" />

  <main class="dream-page">

    <form id="dreamSearchForm" method="get" action="${ctx}/dream/list.do" class="dream-form">

            <!-- 상단 위치 + 검색 영역 -->
      <section class="dream-topbar">
        <div class="dream-topbar-main">

          <!-- 왼쪽: 타이틀 / 카피 -->
          <div class="dream-topbar-left">
            <div class="dream-topbar-badge">우리 동네 나눔</div>
            <h1 class="dream-topbar-title">도란도란 드림</h1>
            <p class="dream-topbar-subtitle">
              가까운 동네에서 안 쓰는 물건들을 나누고, 따뜻한 이웃을 만나보세요.
            </p>
          </div>

          <!-- 오른쪽: 동 선택 + 검색 + 인기 검색어 -->
          <div class="dream-topbar-right">
            <div class="dream-topbar-controls">
              <!-- 동 선택 -->
              <div class="dream-location-wrap">
                <label class="sr-only" for="dongSelect">동 선택</label>
                <c:set var="selectedDong"
                       value="${empty param.dong ? (empty cond.dong ? '마곡동' : cond.dong) : param.dong}" />
                <select id="dongSelect"
                        name="dong"
                        class="dream-location-select js-auto-submit">
                  <option value="마곡동"
                    <c:if test="${selectedDong eq '마곡동'}">selected</c:if>>마곡동</option>
                  <option value="상암동"
                    <c:if test="${selectedDong eq '상암동'}">selected</c:if>>상암동</option>
                  <option value="염창동"
                    <c:if test="${selectedDong eq '염창동'}">selected</c:if>>염창동</option>
                  <option value="가양동"
                    <c:if test="${selectedDong eq '가양동'}">selected</c:if>>가양동</option>
                </select>
              </div>

              <!-- 검색 박스 -->
              <div class="dream-search-wrap">
                <label class="sr-only" for="keywordInput">상품 검색</label>
                <input id="keywordInput"
                       type="text"
                       name="keyword"
                       value="${fn:escapeXml(cond.keyword)}"
                       placeholder="어떤 물건을 찾고 계신가요?"
                       class="dream-search-input" />
                <button type="submit" class="dream-search-button">
                  검색
                </button>
              </div>
            </div>

            <!-- 인기 검색어 -->
            <div class="dream-popular-keywords">
              <span class="dream-popular-label">인기 검색어</span>
              <div class="dream-popular-list">
                <button type="submit" name="keyword" value="보행기" class="dream-popular-item">보행기</button>
                <button type="submit" name="keyword" value="자전거" class="dream-popular-item">자전거</button>
                <button type="submit" name="keyword" value="레고" class="dream-popular-item">레고</button>
                <button type="submit" name="keyword" value="교정젓가락" class="dream-popular-item">교정젓가락</button>
                <button type="submit" name="keyword" value="티니핑" class="dream-popular-item">티니핑</button>
                <button type="submit" name="keyword" value="또봇" class="dream-popular-item">또봇</button>
                <button type="submit" name="keyword" value="분유" class="dream-popular-item">분유</button>
              </div>
            </div>
          </div>

        </div>
      </section>

      <section class="dream-layout">
        <!-- 좌측 필터 -->
        <aside class="dream-filter">
          <h2 class="dream-filter-title">필터</h2>

		  <c:if test="${not empty loginUser}">
		    <section class="dream-filter-section">
		      <h3 class="dream-filter-section-title">내 드림</h3>
		      <label class="dream-checkbox">
		        <input type="checkbox"
		               class="js-auto-submit"
		               name="mine"
		               value="Y"
		               <c:if test="${param.mine eq 'Y' || mine}">checked</c:if> />
		        <span>내가 올린 글만 보기</span>
		      </label>
		    </section>
		  </c:if>
		
          <!-- 나눔 설정 -->
          <section class="dream-filter-section">
            <h3 class="dream-filter-section-title">나눔 설정</h3>
            <label class="dream-checkbox">
              <input type="checkbox"
                     class="js-auto-submit"
                     name="excludeDone"
                     value="Y"
                     <c:if test="${cond.excludeDone}">checked</c:if> />
              <span>나눔 완료 제외</span>
            </label>
          </section>

          <!-- 상태 -->
			<section class="dream-filter-section">
			  <h3 class="dream-filter-section-title">상태</h3>
			
			  <label class="dream-checkbox">
			    <input type="checkbox"
			           class="js-auto-submit"
			           name="condition"
			           value="새거"
			           <c:if test="${conditionNew}">checked</c:if> />
			    <span>새상품</span> <!-- 화면에는 새상품으로 표기 -->
			  </label>
			
			  <label class="dream-checkbox">
			    <input type="checkbox"
			           class="js-auto-submit"
			           name="condition"
			           value="흠집없는 중고"
			           <c:if test="${conditionLikeNew}">checked</c:if> />
			    <span>흠집 없는 중고</span>
			  </label>
			
			  <label class="dream-checkbox">
			    <input type="checkbox"
			           class="js-auto-submit"
			           name="condition"
			           value="사용감 있는 중고"
			           <c:if test="${conditionUsed}">checked</c:if> />
			    <span>사용감 있는 중고</span>
			  </label>
			</section>
			
			<!-- 카테고리 (상태 아래로 배치) -->
			  <section class="dream-filter-section dream-filter-category">
			    <h3 class="dream-filter-section-title">카테고리</h3>
			
			    <c:set var="selectedCategory" value="${cond.categoryCode}" />
			
			    <div class="dream-category-list">
			      <!-- 전체 카테고리 -->
			      <button type="submit"
			              name="category"
			              value=""
			              class="dream-category-item ${empty selectedCategory ? 'is-active' : ''}">
			        <span>전체</span>
			      </button>
			
			      <!-- 장난감 -->
			      <button type="submit"
			              name="category"
			              value="장난감"
			              class="dream-category-item ${selectedCategory eq '장난감' ? 'is-active' : ''}">
			        <span>장난감</span>
			      </button>
			
			      <!-- 유모차 -->
			      <button type="submit"
			              name="category"
			              value="유모차"
			              class="dream-category-item ${selectedCategory eq '유모차' ? 'is-active' : ''}">
			        <span>유모차</span>
			      </button>
			
			      <!-- 남아의류 -->
			      <button type="submit"
			              name="category"
			              value="남아의류"
			              class="dream-category-item ${selectedCategory eq '남아의류' ? 'is-active' : ''}">
			        <span>남아의류</span>
			      </button>
			
			      <!-- 여아의류 -->
			      <button type="submit"
			              name="category"
			              value="여아의류"
			              class="dream-category-item ${selectedCategory eq '여아의류' ? 'is-active' : ''}">
			        <span>여아의류</span>
			      </button>
			
			      <!-- 유아동용품 -->
			      <button type="submit"
			              name="category"
			              value="유아용품"
			              class="dream-category-item ${selectedCategory eq '유아용품' ? 'is-active' : ''}">
			        <span>유아동용품</span>
			      </button>
			
			      <!-- 수유/이유용품 -->
			      <button type="submit"
			              name="category"
			              value="수유/이유용품"
			              class="dream-category-item ${selectedCategory eq '수유/이유용품' ? 'is-active' : ''}">
			        <span>수유/이유용품</span>
			      </button>
			
			      <!-- 애견용품 -->
			      <button type="submit"
			              name="category"
			              value="애견용품"
			              class="dream-category-item ${selectedCategory eq '애견용품' ? 'is-active' : ''}">
			        <span>애견용품</span>
			      </button>
			
			      <!-- 애견의류 -->
			      <button type="submit"
			              name="category"
			              value="애견의류"
			              class="dream-category-item ${selectedCategory eq '애견의류' ? 'is-active' : ''}">
			        <span>애견의류</span>
			      </button>
			    </div>
			  </section>
			</aside>

        <!-- 우측 리스트 -->
        <section class="dream-main">
          <div class="dream-main-header">
			  <div class="dream-main-count">
			    총 <strong>${fn:length(dreamList)}</strong>개의 드림
			  </div>
			
			  <div class="dream-main-right">
			    <div class="dream-main-sort">
			      <span class="dream-sort-label">정렬</span>
			
			      <!-- 최신순 버튼 -->
			      <button type="submit"
			              name="sort"
			              value="LATEST"
			              class="dream-sort-button<c:if test='${empty param.sort or param.sort eq "LATEST"}'> is-active</c:if>">
			        최신순
			      </button>
			
			      <!-- 과거순 버튼 -->
			      <button type="submit"
			              name="sort"
			              value="OLDEST"
			              class="dream-sort-button<c:if test='${param.sort eq "OLDEST"}'> is-active</c:if>">
			        과거순
			      </button>
			    </div>
			
			    <!-- 로그인 상태일 때만 글쓰기 버튼 -->
			    <c:set var="loginUser" value="${sessionScope.Authorization}" />
			    <c:if test="${not empty loginUser}">
			      <a href="${ctx}/dream/write.do" class="dream-write-btn">
			        + 게시글 작성하기
			      </a>
			    </c:if>
			  </div>
			</div>

          <c:choose>
            <c:when test="${empty dreamList}">
              <p class="dream-empty">
                조건에 맞는 드림이 없습니다.  
                검색어 또는 필터를 바꿔 다시 찾아보세요.
              </p>
            </c:when>
            <c:otherwise>
              <ul class="dream-grid">
                <c:forEach var="item" items="${dreamList}">
                  <li class="dream-card">
                    <a class="dream-card-link"
                       href="${ctx}/dream/detail.do?itemId=${item.dreamId}">
                      <div class="dream-card-thumb-wrap">
                        <img class="dream-card-thumb"
                             src="${item.imagesUrl[0]}"
                             alt="${fn:escapeXml(item.title)}" />
                      </div>
                      <div class="dream-card-body">
                        <p class="dream-card-title">
                          ${fn:escapeXml(item.title)}
                        </p>

                        <p class="dream-card-meta">
                          <span class="dream-card-meta-location">
                            ${fn:escapeXml(item.dong)}
                          </span>
                          <span class="dream-card-meta-dot">·</span>
                          <span class="dream-card-meta-time">
                            ${item.timeAgoLabel}
                          </span>
                        </p>

                        <div class="dream-card-tags">
                          <c:if test="${not empty item.conditionCode}">
                            <span class="dream-tag dream-tag-condition">
                              <c:choose>
								<c:when test="${item.conditionCode eq '새거'}">새상품</c:when>                                <c:when test="${item.conditionCode eq 'NEW'}">새 상품</c:when>
                                <c:when test="${item.conditionCode eq '흠집없는 중고'}">흠집 없는 중고</c:when>
                                <c:when test="${item.conditionCode eq '사용감 있는 중고'}">사용감 있는 중고</c:when>
                                <c:otherwise>${item.conditionCode}</c:otherwise>
                              </c:choose>
                            </span>
                          </c:if>

                          <c:if test="${item.status eq 'OPEN'}">
                            <span class="dream-tag dream-tag-status-open">나눔 중</span>
                          </c:if>
                          <c:if test="${item.status eq 'CLOSE' || item.status eq 'CLOSED'}">
                            <span class="dream-tag dream-tag-status-close">나눔 완료</span>
                          </c:if>
                        </div>
                      </div>
                    </a>
                  </li>
                </c:forEach>
              </ul>
            </c:otherwise>
          </c:choose>
        </section>
      </section>
    </form>
  </main>

  <script>
    document.addEventListener("DOMContentLoaded", function () {
      var form = document.getElementById("dreamSearchForm");
      if (!form) return;

      // 체크박스 / select 자동 제출
      var autos = form.querySelectorAll(".js-auto-submit");
      autos.forEach(function (el) {
        el.addEventListener("change", function () {
          form.submit();
        });
      });

      // 카테고리 아코디언은 제거했으므로 관련 스크립트도 불필요
    });
  </script>

</body>
</html>
