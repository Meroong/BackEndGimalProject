구동을 위해서는 src/main/resources/config/dbConfig.properties 생성 이후 빌드패스에서 소스 탭에서 src/main/resources 폴더를 추가 
    dbConfig.properties 작성예시는 아래와 같음 
    DB_URL=jdbc:mysql://localhost:3306/dorandoran?serverTimezone=UTC
    DB_USER=root
    DB_PASSWORD=1234

DB 세팅

create database dorandoran;
use dorandoran;
-- 💬 채팅메시지
DROP TABLE IF EXISTS chat_message;

DROP TABLE IF EXISTS chat_room_user;
-- 💬 채팅방
DROP TABLE IF EXISTS chat_room;

-- 🚨 신고
DROP TABLE IF EXISTS report;

-- ⭐ 리뷰
DROP TABLE IF EXISTS review;

-- 💳 거래기록
DROP TABLE IF EXISTS transaction;

-- ❤️ 찜 목록
DROP TABLE IF EXISTS wishlist;

-- 🔁 대여 상세정보
DROP TABLE IF EXISTS rental_info;

-- 💬 중고/대여 상품 게시판
DROP TABLE IF EXISTS item;

-- 💾 이미지 테이블
DROP TABLE IF EXISTS image;

-- 🏷️ 유저 태그
DROP TABLE IF EXISTS user_tag;

-- 🤝 모임참여자 관리
DROP TABLE IF EXISTS meeting_participant;

-- 🤝 모임 게시판
DROP TABLE IF EXISTS meeting;

-- 📢 공지게시판
DROP TABLE IF EXISTS notice;

-- 🧍 USER 관련
DROP TABLE IF EXISTS user;

-- 📍 지역정보 테이블
DROP TABLE IF EXISTS address;


SET FOREIGN_KEY_CHECKS = 1;


-- 📍 지역정보 테이블
CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sido_code INT NOT NULL,
    sigungu_code INT NOT NULL,
    dong_code INT NOT NULL,
    sido_name VARCHAR(50) NOT NULL,
    sigungu_name VARCHAR(50),
    dong_name VARCHAR(50),
    full_name VARCHAR(150) GENERATED ALWAYS AS (
        CONCAT(sido_name, ' ', sigungu_name, ' ', dong_name)
    ) STORED
) COMMENT='지역 코드 및 행정구역 정보';


-- 🧍 USER 관련
CREATE TABLE user (
    auto_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저 고유 식별자',
    user_id VARCHAR(100) UNIQUE NOT NULL COMMENT '로그인용 아이디',
    user_password VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
    user_name VARCHAR(50) NOT NULL COMMENT '실명',
    nickname VARCHAR(50) UNIQUE NOT NULL COMMENT '닉네임',
    trust_score INT DEFAULT 0 COMMENT '신뢰도 점수',
    role ENUM('USER', 'ADMIN') DEFAULT 'USER' COMMENT '권한',
    address_id BIGINT COMMENT '지역 정보 참조키',
    address_detail VARCHAR(255) COMMENT '상세 주소',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '정보 수정일시',
    CONSTRAINT fk_user_address FOREIGN KEY (address_id) REFERENCES address(id)
) COMMENT='회원 정보 테이블';


-- 🏷️ 유저 태그
CREATE TABLE user_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저태그 식별자',
    user_id BIGINT NOT NULL COMMENT '유저 식별자',
    tag_name VARCHAR(100) NOT NULL COMMENT '관심 태그',
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
) COMMENT='유저 관심 태그';


-- 💾 이미지 테이블
CREATE TABLE image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '이미지 식별자',
    src VARCHAR(255) NOT NULL COMMENT '저장 경로',
    type ENUM('USER', 'ITEM', 'MEETING') COMMENT '이미지 종류',
    target_id BIGINT COMMENT '대상 ID',
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '업로드 일시'
) COMMENT='이미지 테이블';


-- 💬 중고/대여 상품 게시판
CREATE TABLE item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 ID',
    seller_id BIGINT NOT NULL COMMENT '판매자 ID',
    category_id BIGINT COMMENT '카테고리 ID',
    title VARCHAR(255) NOT NULL COMMENT '상품 제목',
    content TEXT COMMENT '상품 설명',
    price INT NOT NULL COMMENT '판매 가격',
    trade_type ENUM('SALE', 'RENTAL') DEFAULT 'SALE' COMMENT '거래 유형',
    status ENUM('AVAILABLE', 'RESERVED', 'COMPLETED') DEFAULT 'AVAILABLE' COMMENT '상품 상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (seller_id) REFERENCES user(auto_id)
) COMMENT='중고/대여 게시판';


-- 🔁 대여 상세정보
CREATE TABLE rental_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '대여 정보 ID',
    item_id BIGINT NOT NULL COMMENT '상품 ID',
    deposit INT DEFAULT 0 COMMENT '보증금',
    daily_rate INT COMMENT '일일 요금',
    rental_period INT COMMENT '대여 기간',
    return_date DATE COMMENT '반납 예정일',
    FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
) COMMENT='대여 상세 정보';


-- ❤️ 찜 목록
CREATE TABLE wishlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '찜목록 ID',
    user_id BIGINT NOT NULL COMMENT '유저 ID',
    item_id BIGINT NOT NULL COMMENT '상품 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
) COMMENT='찜 목록';


-- 💳 거래기록
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 기록 ID',
    item_id BIGINT NOT NULL COMMENT '상품 ID',
    buyer_id BIGINT NOT NULL COMMENT '구매자 ID',
    seller_id BIGINT NOT NULL COMMENT '판매자 ID',
    status ENUM('IN_PROGRESS','COMPLETED','CANCELLED') DEFAULT 'IN_PROGRESS' COMMENT '거래 상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '거래 시작일',
    completed_at TIMESTAMP NULL COMMENT '완료일',
    FOREIGN KEY (item_id) REFERENCES item(item_id),
    FOREIGN KEY (buyer_id) REFERENCES user(auto_id),
    FOREIGN KEY (seller_id) REFERENCES user(auto_id)
) COMMENT='거래 기록';


-- ⭐ 리뷰
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '리뷰 ID',
    reviewer_id BIGINT NOT NULL COMMENT '작성자 ID',
    reviewee_id BIGINT NOT NULL COMMENT '대상자 ID',
    item_id BIGINT COMMENT '상품 ID',
    rating_manner INT DEFAULT 0 COMMENT '매너 점수',
    content TEXT COMMENT '리뷰 내용',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    FOREIGN KEY (reviewer_id) REFERENCES user(auto_id),
    FOREIGN KEY (reviewee_id) REFERENCES user(auto_id)
) COMMENT='리뷰';


-- 🤝 모임 게시판
CREATE TABLE meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '모임 ID',
    title VARCHAR(255) NOT NULL COMMENT '모임 제목',
    content TEXT COMMENT '설명',
    date DATETIME COMMENT '모임 날짜',
    location VARCHAR(255) COMMENT '장소',
    max_members INT COMMENT '최대 인원',
    current_members INT COMMENT '현재 인원',
    cost INT DEFAULT 0 COMMENT '참가비',
    tag VARCHAR(100) COMMENT '모임 태그',
    status ENUM('OPEN','CLOSED','COMPLETED') DEFAULT 'OPEN' COMMENT '상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT='모임 게시판';


-- 👥 모임참여자 관리
CREATE TABLE meeting_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '참가 ID',
    meeting_id BIGINT NOT NULL COMMENT '모임 ID',
    user_id BIGINT NOT NULL COMMENT '유저 ID',
    paid BOOLEAN DEFAULT FALSE COMMENT '참가비 지불 여부',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '참여일시',
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
) COMMENT='모임 참여자';


-- 📢 공지게시판
CREATE TABLE notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '공지 ID',
    title VARCHAR(255) NOT NULL COMMENT '제목',
    content TEXT COMMENT '내용',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT='공지 게시판';


-- 💬 채팅방

CREATE TABLE chat_room (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT COMMENT '상품 ID (거래 채팅일 경우)',
    room_type ENUM('PRIVATE', 'GROUP') DEFAULT 'PRIVATE',
    host_id BIGINT NOT NULL COMMENT '방장 user_id',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (host_id) REFERENCES user(auto_id)
) COMMENT='거래 및 모임용 채팅방';


-- 💭 채팅방참여자
CREATE TABLE chat_room_user (
    room_id BIGINT NOT NULL COMMENT '채팅방 ID',
    user_id BIGINT NOT NULL COMMENT '참여 유저 ID',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '참여 일시',
    PRIMARY KEY (room_id, user_id),
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
) COMMENT='채팅방 참여자 관리';



-- 💭 채팅메시지
CREATE TABLE chat_message (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '메시지 ID',
    room_id BIGINT NOT NULL COMMENT '채팅방 ID',
    sender_id BIGINT NOT NULL COMMENT '보낸 사람 ID',
    content TEXT COMMENT '메시지 내용',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '전송일시',
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user(auto_id) ON DELETE CASCADE
) COMMENT='채팅 메시지';



-- 🚨 신고
CREATE TABLE report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '신고 ID',
    reporter_id BIGINT NOT NULL COMMENT '신고자 ID',
    target_user_id BIGINT NOT NULL COMMENT '대상자 ID',
    target_type ENUM('USER','ITEM','MEETING') COMMENT '대상 유형',
    reason TEXT COMMENT '신고 사유',
    status ENUM('PENDING','RESOLVED') DEFAULT 'PENDING' COMMENT '상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    FOREIGN KEY (reporter_id) REFERENCES user(auto_id),
    FOREIGN KEY (target_user_id) REFERENCES user(auto_id)
) COMMENT='유저/게시글/모임 신고 관리 테이블';
