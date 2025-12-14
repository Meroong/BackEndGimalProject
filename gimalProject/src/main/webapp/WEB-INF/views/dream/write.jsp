<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>드림 게시글 작성 - MatchIn</title>

  <link rel="stylesheet" href="/gimalProject/resources/css/dream-list.css">
</head>
<body>

  <%-- 공통 헤더 --%>
  <jsp:include page="/WEB-INF/views/include/header.jsp" />

  <main class="dream-write-container">
    <div class="dream-write-inner">

      <h1 class="dream-write-title">드림 게시글 작성하기</h1>

      <c:if test="${not empty errorMessage}">
        <div class="dream-write-error">${errorMessage}</div>
      </c:if>

      <%-- 멀티파트 전송을 위해 enctype 추가 --%>
      <form action="write.do"
            method="post"
            class="dream-write-form"
            enctype="multipart/form-data">

        <div class="form-row">
          <label for="title">제목</label>
          <input type="text" id="title" name="title" maxlength="200" required />
        </div>

        <div class="form-row form-row-2col">
          <div class="form-col">
            <label for="category">카테고리</label>
            <select id="category" name="category_code" required>
              <option value="">선택하세요</option>
              <option value="장난감">장난감</option>
              <option value="유모차">유모차</option>
              <option value="남아의류">남아의류</option>
              <option value="여아의류">여아의류</option>
              <option value="유아용품">유아용품</option>
              <option value="수유/이유용품">수유/이유용품</option>
              <option value="애견용품">애견용품</option>
              <option value="애견의류">애견의류</option>
            </select>
          </div>

          <div class="form-col">
            <label for="condition">상태</label>
            <select id="condition" name="condition_code" required>
              <option value="새거">새상품</option>
              <option value="흠집없는 중고">흠집 없는 중고</option>
              <option value="사용감 있는 중고">사용감 있는 중고</option>
            </select>
          </div>
        </div>

        <div class="form-row form-row-2col">
          <div class="form-col">
            <label for="dong">동네</label>
            <input type="text" id="dong" name="dong" value="마곡동" required />
          </div>
        </div>

        <!-- 멀티파트 이미지 -->
        <div class="form-row">
          <label>이미지</label>

          <div class="dream-upload">
            <div class="dream-upload-header">
              <span class="dream-upload-title">상품 사진을 올려주세요</span>
              <span class="dream-upload-hint">여러 장 선택 가능 · JPG/PNG 등 이미지 파일</span>
            </div>

            <div class="dream-upload-dropzone" id="dream-image-dropzone">
              <%-- 실제 업로드 input (클릭 영역 전체에 걸림) --%>
              <input type="file"
                     id="images"
                     name="images"
                     accept="image/*"
                     multiple
                     class="dream-upload-input" />

              <%-- 아무 이미지가 없을 때 표시 --%>
              <div class="dream-upload-empty" id="dream-image-empty">
                <p class="dream-upload-main">여기에 이미지를 드래그 앤 드롭</p>
                <p class="dream-upload-sub">또는 영역을 클릭해서 파일을 선택하세요</p>
              </div>

              <%-- 이미지가 하나 이상일 때 보여줄 캐러셀 영역 --%>
              <div class="dream-upload-carousel" id="dream-image-carousel">
                <button type="button"
                        class="dream-upload-arrow dream-upload-arrow-prev"
                        id="dream-image-prev"
                        aria-label="이전 이미지">
                  ‹
                </button>

                <div class="dream-upload-viewport" id="dream-image-viewport">
                  <div class="dream-upload-track" id="dream-image-track">
                    <%-- JS가 썸네일 div를 append --%>
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
        <!-- /멀티파트 이미지 -->

        <div class="form-row">
          <label for="content">설명</label>
          <textarea id="content" name="content" rows="8" required></textarea>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary">등록하기</button>
          <a href="list.do" class="btn-secondary">취소</a>
        </div>

      </form>
    </div>
  </main>

    <!-- 드림 이미지 업로드 스크립트 (삭제 기능 포함 버전) -->
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

      // 현재 선택된 이미지 파일들을 관리하는 배열
      var selectedFiles = [];

      // 드롭존 클릭 시 파일 선택창 열기
      dropzone.addEventListener("click", function (e) {
        // 캐러셀(화살표, 썸네일 등)을 클릭한 경우에는 업로드 창 열지 않음
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

      // 드래그 앤 드롭
      dropzone.addEventListener("drop", function (e) {
        var files = e.dataTransfer.files;
        addFiles(files);
      });

      // 파일 선택 (클릭해서 선택하는 경우)
      fileInput.addEventListener("change", function (e) {
        addFiles(e.target.files);
        // 같은 파일 다시 선택해도 이벤트 잘 동작하게 하려면 필요시 초기화
        // fileInput.value = "";
      });

      // 파일 추가
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
            e.stopPropagation(); // 드롭존 클릭 이벤트로 올라가지 않도록
            removeFileAt(index);
          });

          thumb.appendChild(img);
          thumb.appendChild(removeBtn);
          track.appendChild(thumb);
        });

        // 상태 및 화살표 갱신
        updateState();
        viewport.scrollLeft = 0;
        updateArrows();
      }

      // 특정 index의 파일 삭제
      function removeFileAt(index) {
        if (index < 0 || index >= selectedFiles.length) return;

        selectedFiles.splice(index, 1);
        syncInputFiles();
        renderThumbnails();
      }

      // 비어있을 때 / 있을 때 UI 전환
      function updateState() {
        var hasImages = selectedFiles.length > 0;

        empty.style.display    = hasImages ? "none" : "flex";
        carousel.style.display = hasImages ? "flex" : "none";
      }

      // 좌우 화살표 활성화 여부
      function updateArrows() {
        var canScrollPrev = viewport.scrollLeft > 0;
        var maxScrollLeft = viewport.scrollWidth - viewport.clientWidth - 1;
        var canScrollNext = viewport.scrollLeft < maxScrollLeft;

        prevBtn.disabled = !canScrollPrev;
        nextBtn.disabled = !canScrollNext;

        if (canScrollPrev) {
          prevBtn.classList.remove("is-disabled");
        } else {
          prevBtn.classList.add("is-disabled");
        }

        if (canScrollNext) {
          nextBtn.classList.remove("is-disabled");
        } else {
          nextBtn.classList.add("is-disabled");
        }
      }

      // 화살표 클릭 시 부드럽게 스크롤
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

      viewport.addEventListener("scroll", function () {
        updateArrows();
      });

      // 초기 상태
      updateState();
    })();
  </script>


</body>
</html>
