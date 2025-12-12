<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="post" value="${requestScope.post}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>드림 게시글 수정 - MatchIn</title>

  <link rel="stylesheet" href="${ctx}/resources/css/dream-list.css">
</head>
<body>

  <jsp:include page="/include/header.jsp" />

  <main class="dream-write-container">
    <div class="dream-write-inner">

      <h1 class="dream-write-title">드림 게시글 수정하기</h1>

      <c:if test="${not empty errorMessage}">
        <div class="dream-write-error">${errorMessage}</div>
      </c:if>
		 
      <form action="${ctx}/dream/edit.do"
          method="post"
          class="dream-write-form"
          enctype="multipart/form-data">

      <input type="hidden" name="dreamId" value="${post.dreamId}" />

      <%-- ① 기존 이미지 썸네일 + 삭제 버튼 --%>
      <c:if test="${not empty post.imagesUrl}">
        <div class="dream-edit-current-images">
          <p class="dream-edit-current-title">현재 등록된 이미지</p>
          <div class="dream-edit-current-list">
            <c:forEach var="imgUrl" items="${post.imagesUrl}">
              <div class="dream-upload-thumb dream-edit-current-thumb">
                <img src="${imgUrl}" alt="${fn:escapeXml(post.title)}" />

                <!-- 눈에 보이는 삭제 버튼 (X) -->
                <button type="button"
                        class="dream-upload-remove js-mark-delete">
                  ×
                </button>

                <!-- 실제 전송되는 삭제 플래그 (숨김) -->
                <input type="checkbox"
                       name="deleteImageUrl"
                       value="${imgUrl}"
                       class="js-delete-checkbox"
                       style="display:none;" />
              </div>
            </c:forEach>
          </div>
        </div>
      </c:if>

        <div class="form-row">
          <label for="title">제목</label>
          <input type="text"
                 id="title"
                 name="title"
                 maxlength="200"
                 value="${fn:escapeXml(post.title)}"
                 required />
        </div>

        <div class="form-row form-row-2col">
          <div class="form-col">
            <label for="category">카테고리</label>
            <select id="category" name="category_code" required>
              <option value="">선택하세요</option>
              <option value="장난감"
                <c:if test="${post.categoryCode eq '장난감'}">selected</c:if>>장난감</option>
              <option value="유모차"
                <c:if test="${post.categoryCode eq '유모차'}">selected</c:if>>유모차</option>
              <option value="남아의류"
                <c:if test="${post.categoryCode eq '남아의류'}">selected</c:if>>남아의류</option>
              <option value="여아의류"
                <c:if test="${post.categoryCode eq '여아의류'}">selected</c:if>>여아의류</option>
              <option value="유아용품"
                <c:if test="${post.categoryCode eq '유아용품'}">selected</c:if>>유아용품</option>
              <option value="수유/이유용품"
                <c:if test="${post.categoryCode eq '수유/이유용품'}">selected</c:if>>수유/이유용품</option>
              <option value="애견용품"
                <c:if test="${post.categoryCode eq '애견용품'}">selected</c:if>>애견용품</option>
              <option value="애견의류"
                <c:if test="${post.categoryCode eq '애견의류'}">selected</c:if>>애견의류</option>
            </select>
          </div>

          <div class="form-col">
            <label for="condition">상태</label>
            <select id="condition" name="condition_code" required>
              <option value="새거"
                <c:if test="${post.conditionCode eq '새거'}">selected</c:if>>새상품</option>
              <option value="흠집없는 중고"
                <c:if test="${post.conditionCode eq '흠집없는 중고'}">selected</c:if>>흠집 없는 중고</option>
              <option value="사용감 있는 중고"
                <c:if test="${post.conditionCode eq '사용감 있는 중고'}">selected</c:if>>사용감 있는 중고</option>
            </select>
          </div>
        </div>

        <div class="form-row form-row-2col">
          <div class="form-col">
            <label for="dong">동네</label>
            <input type="text"
                   id="dong"
                   name="dong"
                   value="${fn:escapeXml(post.dong)}"
                   required />
          </div>
        </div>

        <!-- 추가 이미지 업로드 -->
        <div class="form-row">
          <label>이미지 추가</label>

          <div class="dream-upload">
            <div class="dream-upload-header">
              <span class="dream-upload-title">추가로 올릴 상품 사진</span>
              <span class="dream-upload-hint">여러 장 선택 가능 · 기존 이미지는 유지됩니다</span>
            </div>

            <div class="dream-upload-dropzone" id="dream-image-dropzone">
              <input type="file"
                     id="images"
                     name="images"
                     accept="image/*"
                     multiple
                     class="dream-upload-input" />

              <div class="dream-upload-empty" id="dream-image-empty">
                <p class="dream-upload-main">여기에 이미지를 드래그 앤 드롭</p>
                <p class="dream-upload-sub">또는 영역을 클릭해서 파일을 선택하세요</p>
              </div>

              <div class="dream-upload-carousel" id="dream-image-carousel">
                <button type="button"
                        class="dream-upload-arrow dream-upload-arrow-prev"
                        id="dream-image-prev"
                        aria-label="이전 이미지">
                  ‹
                </button>

                <div class="dream-upload-viewport" id="dream-image-viewport">
                  <div class="dream-upload-track" id="dream-image-track">
                    <!-- JS가 썸네일 추가 -->
                  </div>
                </div>

                <button type="button"
                        class="dream-upload-arrow dream-upload-arrow-next"
                        id="dream-image-next"
                        aria-label="다음 이미지">
                  ›
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="form-row">
          <label for="content">설명</label>
          <textarea id="content" name="content" rows="8" required>${fn:escapeXml(post.content)}</textarea>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary">수정 완료</button>
          <a href="${ctx}/dream/detail.do?itemId=${post.dreamId}" class="btn-secondary">취소</a>
        </div>

      </form>
    </div>
  </main>

  <!-- 작성 페이지와 동일한 업로드 스크립트 재사용 -->
  <!-- 추가 이미지 업로드 + 삭제 기능 -->
  <script>
    (function () {
      var dropzone  = document.getElementById("dream-image-dropzone");
      if (!dropzone) return;

      var fileInput = document.getElementById("images");
      var empty     = document.getElementById("dream-image-empty");
      var carousel  = document.getElementById("dream-image-carousel");
      var viewport  = document.getElementById("dream-image-viewport");
      var track     = document.getElementById("dream-image-track");
      var prevBtn   = document.getElementById("dream-image-prev");
      var nextBtn   = document.getElementById("dream-image-next");

      // 새로 추가한 이미지 파일들만 관리 (기존 DB 이미지에는 영향 없음)
      var selectedFiles = [];

      // 드롭존 클릭 시 파일 선택창 열기
      dropzone.addEventListener("click", function (e) {
        // 캐러셀(화살표, 썸네일)을 클릭한 경우에는 업로드 창 열지 않음
        if (e.target.closest(".dream-upload-carousel")) {
          return;
        }
        fileInput.click();
      });

      function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
      }

      ["dragenter", "dragover", "dragleave", "drop"].forEach(function (eventName) {
        dropzone.addEventListener(eventName, preventDefaults, false);
      });

      ["dragenter", "dragover"].forEach(function (eventName) {
        dropzone.addEventListener(eventName, function () {
          dropzone.classList.add("is-dragover");
        }, false);
      });

      ["dragleave", "drop"].forEach(function (eventName) {
        dropzone.addEventListener(eventName, function () {
          dropzone.classList.remove("is-dragover");
        }, false);
      });

      // 드래그 앤 드롭으로 파일 추가
      dropzone.addEventListener("drop", function (e) {
        var files = e.dataTransfer.files;
        addFiles(files);
      });

      // 파일 선택으로 추가
      fileInput.addEventListener("change", function (e) {
        addFiles(e.target.files);
        // 필요하면 같은 파일 다시 선택용 초기화
        // fileInput.value = "";
      });

      // 파일들을 selectedFiles에 추가
      function addFiles(files) {
        if (!files || !files.length) return;

        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          // 이미지 파일만 허용
          if (!file.type || file.type.indexOf("image/") !== 0) {
            continue;
          }
          selectedFiles.push(file);
        }

        syncInputFiles();
        renderThumbnails();
      }

      // fileInput.files를 selectedFiles 기준으로 다시 구성
      function syncInputFiles() {
        var dt = new DataTransfer();
        selectedFiles.forEach(function (file) {
          dt.items.add(file);
        });
        fileInput.files = dt.files;
      }

      // 썸네일 렌더링
      function renderThumbnails() {
        // 기존 썸네일 제거
        track.innerHTML = "";

        selectedFiles.forEach(function (file, index) {
          var thumb = document.createElement("div");
          thumb.className = "dream-upload-thumb";

          var img = document.createElement("img");
          img.src = URL.createObjectURL(file);
          img.alt = file.name;

          // 삭제 버튼
          var removeBtn = document.createElement("button");
          removeBtn.type = "button";
          removeBtn.className = "dream-upload-remove";
          removeBtn.textContent = "×";
          removeBtn.addEventListener("click", function (e) {
            e.stopPropagation();
            removeFileAt(index);
          });

          thumb.appendChild(img);
          thumb.appendChild(removeBtn);
          track.appendChild(thumb);
        });

        updateState();
        viewport.scrollLeft = 0;
        updateArrows();
      }

      // 특정 index의 파일 제거
      function removeFileAt(index) {
        if (index < 0 || index >= selectedFiles.length) return;

        selectedFiles.splice(index, 1);
        syncInputFiles();
        renderThumbnails();
      }

      // 비어있을 때 / 있을 때 상태 전환
      function updateState() {
        var hasImages = selectedFiles.length > 0;

        empty.style.display    = hasImages ? "none" : "flex";
        carousel.style.display = hasImages ? "flex" : "none";
      }

      // 좌우 화살표 활성/비활성
      function updateArrows() {
        var canScrollPrev = viewport.scrollLeft > 0;
        var maxScrollLeft = viewport.scrollWidth - viewport.clientWidth - 1;
        var canScrollNext = viewport.scrollLeft < maxScrollLeft;

        prevBtn.disabled = !canScrollPrev;
        nextBtn.disabled = !canScrollNext;

        prevBtn.classList.toggle("is-disabled", !canScrollPrev);
        nextBtn.classList.toggle("is-disabled", !canScrollNext);
      }

      // 좌/우 버튼 클릭 시 스크롤
      prevBtn.addEventListener("click", function (e) {
        e.stopPropagation();
        var amount = viewport.clientWidth;
        viewport.scrollTo({
          left: viewport.scrollLeft - amount,
          behavior: "smooth"
        });
      });

      nextBtn.addEventListener("click", function (e) {
        e.stopPropagation();
        var amount = viewport.clientWidth;
        viewport.scrollTo({
          left: viewport.scrollLeft + amount,
          behavior: "smooth"
        });
      });

      viewport.addEventListener("scroll", updateArrows);

      // 초기 상태
      updateState();
    })();
  </script>

  <script>
	  (function () {
	    var thumbs = document.querySelectorAll(".dream-edit-current-thumb");
	
	    thumbs.forEach(function (thumb) {
	      var btn = thumb.querySelector(".js-mark-delete");
	      var checkbox = thumb.querySelector(".js-delete-checkbox");
	      if (!btn || !checkbox) return;
	
	      btn.addEventListener("click", function (e) {
	        e.preventDefault();
	
	        var willDelete = !checkbox.checked;
	        checkbox.checked = willDelete;
	
	        // 시각적으로 삭제 예정 표시
	        thumb.classList.toggle("is-marked-for-delete", willDelete);
	      });
	    });
	  })();
	</script>

</body>
</html>
