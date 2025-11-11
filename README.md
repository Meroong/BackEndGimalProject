DB 세팅

create database dorandoran;
use dorandoran;
-- 🧍 USER 관련


DROP TABLE IF EXISTS user;
CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저 고유 식별자',
    user_password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    user_name VARCHAR(50) NOT NULL COMMENT '실명',
    nickname VARCHAR(50) UNIQUE NOT NULL COMMENT '닉네임 (중복 불가)',
    trust_score INT DEFAULT 0 COMMENT '신뢰도 점수 (거래/리뷰 기반)',
    role ENUM('USER', 'ADMIN') DEFAULT 'USER' COMMENT '권한 (일반 / 관리자)',
    address_id BIGINT COMMENT '지역 정보 참조키',
    address_detail VARCHAR(255) COMMENT '상세 주소',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '정보 수정일시'
) COMMENT='회원 정보 테이블';



-- 📍 지역정보 테이블


DROP TABLE IF EXISTS address;
CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '지역 고유 식별자',
    sido VARCHAR(50) COMMENT '시/도',
    sigungu VARCHAR(50) COMMENT '시/군/구',
    dong VARCHAR(50) COMMENT '동/읍/면'
) COMMENT='지역정보 (3단계 행정구역)' ;



-- 🏷️ 유저 태그


DROP TABLE IF EXISTS user_tag;
CREATE TABLE user_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저태그 식별자',
    user_id BIGINT NOT NULL COMMENT '유저 식별자',
    tag_name VARCHAR(100) NOT NULL COMMENT '관심 연령대, 견종, 성향 등 태그명',
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) COMMENT='유저 관심 태그 (연령대/견종/성향 등)';



-- 💾 이미지 테이블


DROP TABLE IF EXISTS image;
CREATE TABLE image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '이미지 식별자',
    src VARCHAR(255) NOT NULL COMMENT '로컬 또는 S3 저장 경로(UUID 파일명)',
    type ENUM('USER', 'ITEM', 'MEETING') COMMENT '이미지 종류',
    target_id BIGINT COMMENT '연결된 대상 ID',
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '업로드 일시'
) COMMENT='공통 이미지 테이블 (유저, 게시판 등)';



-- 💬 중고/대여 상품 게시판


DROP TABLE IF EXISTS item;
CREATE TABLE item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 고유 ID',
    seller_id BIGINT NOT NULL COMMENT '판매자 유저 ID',
    category_id BIGINT COMMENT '카테고리 ID',
    title VARCHAR(255) NOT NULL COMMENT '상품 제목',
    content TEXT COMMENT '상품 설명',
    price INT NOT NULL COMMENT '판매 가격',
    trade_type ENUM('SALE', 'RENTAL') DEFAULT 'SALE' COMMENT '거래 유형 (판매 / 대여)',
    status ENUM('AVAILABLE', 'RESERVED', 'COMPLETED') DEFAULT 'AVAILABLE' COMMENT '상품 상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (seller_id) REFERENCES user(user_id)
) COMMENT='중고거래 및 대여 게시판 기본 정보';



-- 🔁 대여 상세정보


DROP TABLE IF EXISTS rental_info;
CREATE TABLE rental_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '대여 정보 식별자',
    item_id BIGINT NOT NULL COMMENT '상품 ID (item 테이블 FK)',
    deposit INT DEFAULT 0 COMMENT '보증금',
    daily_rate INT COMMENT '일일 대여 요금',
    rental_period INT COMMENT '대여 가능 기간 (일 단위)',
    return_date DATE COMMENT '반납 예정일',
    FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
) COMMENT='대여형 상품의 상세 대여 정보';



-- ❤️ 찜 목록


DROP TABLE IF EXISTS wishlist;
CREATE TABLE wishlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '찜목록 식별자',
    user_id BIGINT NOT NULL COMMENT '유저 ID',
    item_id BIGINT NOT NULL COMMENT '상품 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
) COMMENT='유저별 찜 목록';



-- 💳 거래기록


DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '거래기록 식별자',
    item_id BIGINT NOT NULL COMMENT '거래 상품 ID',
    buyer_id BIGINT NOT NULL COMMENT '구매자 ID',
    seller_id BIGINT NOT NULL COMMENT '판매자 ID',
    status ENUM('IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'IN_PROGRESS' COMMENT '거래 상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '거래 시작일',
    completed_at TIMESTAMP NULL COMMENT '거래 완료일',
    FOREIGN KEY (item_id) REFERENCES item(item_id),
    FOREIGN KEY (buyer_id) REFERENCES user(user_id),
    FOREIGN KEY (seller_id) REFERENCES user(user_id)
) COMMENT='거래 이력 테이블 (판매/대여 공용)';



-- ⭐ 리뷰


DROP TABLE IF EXISTS review;
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '리뷰 식별자',
    reviewer_id BIGINT NOT NULL COMMENT '리뷰 작성자',
    reviewee_id BIGINT NOT NULL COMMENT '리뷰 대상자',
    item_id BIGINT COMMENT '거래 상품 ID',
    rating_manner INT DEFAULT 0 COMMENT '매너 점수',
    content TEXT COMMENT '리뷰 내용',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    FOREIGN KEY (reviewer_id) REFERENCES user(user_id),
    FOREIGN KEY (reviewee_id) REFERENCES user(user_id)
) COMMENT='거래/모임 종료 후 작성되는 리뷰';



-- 🤝 모임 게시판

DROP TABLE IF EXISTS meeting;
CREATE TABLE meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '모임 고유 식별자',
    title VARCHAR(255) NOT NULL COMMENT '모임 제목',
    text TEXT COMMENT '모임 설명',
    date DATETIME COMMENT '모임 날짜/시간',
    location VARCHAR(255) COMMENT '모임 장소',
    max_members INT COMMENT '최대 인원 수',
    current_members INT COMMENT '현재 인원 수',
    cost INT DEFAULT 0 COMMENT '참가비(1/n 분담)',
    tag VARCHAR(100) COMMENT '모임 관련 태그 (텍스트)',
    status ENUM('OPEN', 'CLOSED', 'COMPLETED') DEFAULT 'OPEN' COMMENT '모임 상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT='소규모 모임 게시판 (카페/놀이터 등 공유)';



-- 👥 모임참여자 관리


DROP TABLE IF EXISTS meeting_participant;
CREATE TABLE meeting_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '모임참가 식별자',
    meeting_id BIGINT NOT NULL COMMENT '모임 ID',
    user_id BIGINT NOT NULL COMMENT '참여 유저 ID',
    paid BOOLEAN DEFAULT FALSE COMMENT '참가비 지불 여부',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '참여일시',
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
) COMMENT='모임 참가자 관리 (참가비 결제 완료 시 포함)';



-- 📢 공지게시판


DROP TABLE IF EXISTS notice;
CREATE TABLE notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '공지 식별자',
    title VARCHAR(255) NOT NULL COMMENT '공지 제목',
    content TEXT COMMENT '공지 내용',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT='운영자 공지사항 게시판';



-- 💬 채팅방


DROP TABLE IF EXISTS chat_room;
CREATE TABLE chat_room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '채팅방 식별자',
    item_id BIGINT COMMENT '상품 ID (거래 채팅일 경우)',
    room_type ENUM('PRIVATE', 'GROUP') DEFAULT 'PRIVATE' COMMENT '채팅방 유형',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시'
) COMMENT='거래 및 모임용 채팅방 관리';



-- 💭 채팅메시지

DROP TABLE IF EXISTS chat_message;
CREATE TABLE chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '메시지 식별자',
    room_id BIGINT NOT NULL COMMENT '채팅방 ID',
    sender_id BIGINT NOT NULL COMMENT '보낸 사람 ID',
    content TEXT COMMENT '메시지 내용',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '전송시각',
    FOREIGN KEY (room_id) REFERENCES chat_room(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user(user_id) ON DELETE CASCADE
) COMMENT='채팅 메시지 내역';

-- 🚨 신고

DROP TABLE IF EXISTS report;
CREATE TABLE report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '신고 식별자',
    reporter_id BIGINT NOT NULL COMMENT '신고자 ID',
    target_user_id BIGINT NOT NULL COMMENT '신고 대상자 ID',
    target_type ENUM('USER', 'ITEM', 'MEETING') COMMENT '신고 대상 유형',
    reason TEXT COMMENT '신고 사유',
    status ENUM('PENDING', 'RESOLVED') DEFAULT 'PENDING' COMMENT '신고 처리 상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '신고 등록일',
    FOREIGN KEY (reporter_id) REFERENCES user(user_id),
    FOREIGN KEY (target_user_id) REFERENCES user(user_id)
) COMMENT='유저/게시글/모임 신고 관리 테이블';
