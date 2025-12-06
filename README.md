구동을 위해서는 src/main/resources/config/dbConfig.properties 생성 이후 빌드패스에서 소스 탭에서 src/main/resources 폴더를 추가 
    dbConfig.properties 작성예시는 아래와 같음 
    DB_URL=jdbc:mysql://localhost:3306/dorandoran?serverTimezone=UTC
    DB_USER=root
    DB_PASSWORD=1234

DB 세팅

DROP database IF exists dorandoran;
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


-- 💬 드림/교환 게시판
CREATE TABLE item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 ID',
    seller_id BIGINT NOT NULL COMMENT '판매자 ID',
    category_id BIGINT COMMENT '카테고리 ID',
    title VARCHAR(255) NOT NULL COMMENT '상품 제목',
    content TEXT COMMENT '상품 설명',
    price INT NOT NULL COMMENT '판매 가격',
    trade_type ENUM('SALE', 'RENTAL', 'DREAM') DEFAULT 'SALE' COMMENT '거래 유형',
    status ENUM('AVAILABLE', 'RESERVED', 'COMPLETED') DEFAULT 'AVAILABLE' COMMENT '상품 상태',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (seller_id) REFERENCES user(auto_id)
) COMMENT='드림/교환 게시판';


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
    meeting_id BIGINT COMMENT '모임 ID',
    room_type ENUM('PRIVATE', 'GROUP') DEFAULT 'PRIVATE',
    host_id BIGINT NOT NULL COMMENT '방장 user_id',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (host_id) REFERENCES user(auto_id),
    FOREIGN KEY (meeting_id) REFERENCES meeting(id)
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
) COMMENT='신고';

-- 간이 데이터 
-- 📍 지역정보
INSERT INTO address (sido_code, sigungu_code, dong_code, sido_name, sigungu_name, dong_name)
VALUES
(11, 101, 1, '서울특별시', '은평구', '역촌동'),
(11, 102, 2, '서울특별시', '강남구', '삼성동');

-- 🧍 USER
INSERT INTO user (user_id, user_password, user_name, nickname, role, address_id, address_detail)
VALUES
('admin01', '1234', '관리자', '관리자닉', 'ADMIN', 1, '성산동 123-45'),
('test01', '1234', '테스트', '닉테스트', 'USER', 2, '삼성동 456-78');

-- 🏷️ 유저 태그
INSERT INTO user_tag (user_id, tag_name)
VALUES
(2, '운동 좋아함'),
(2, '강아지 사랑');

-- 💾 이미지
INSERT INTO image (src, type, target_id)
VALUES
('user1.png','USER',1),
('item1.png','ITEM',1);

-- 💬 중고/대여 상품 게시판
INSERT INTO item (seller_id, category_id, title, content, price, trade_type, status)
VALUES
(2, 1, '자전거 판매', '좋은 자전거 팝니다', 100000, 'SALE', 'AVAILABLE'),
(2, 1, '책 대여', '프로그래밍 책 대여합니다', 5000, 'RENTAL', 'AVAILABLE');

-- 🔁 대여 상세정보
INSERT INTO rental_info (item_id, deposit, daily_rate, rental_period, return_date)
VALUES
(2, 1000, 500, 7, '2025-11-24');

-- ❤️ 찜 목록
INSERT INTO wishlist (user_id, item_id)
VALUES
(1, 1);

-- 💳 거래기록
INSERT INTO transaction (item_id, buyer_id, seller_id, status)
VALUES
(1, 1, 2, 'IN_PROGRESS');

-- ⭐ 리뷰
INSERT INTO review (reviewer_id, reviewee_id, item_id, rating_manner, content)
VALUES
(1, 2, 1, 5, '좋은 판매자입니다!');

-- 🤝 모임 게시판
INSERT INTO meeting (title, content, date, location, max_members, current_members, cost, tag, status)
VALUES
('조깅 모임', '매주 토요일 조깅', '2025-11-22 09:00:00', '한강공원', 10, 2, 0, '운동', 'OPEN');

-- 👥 모임참여자 관리
INSERT INTO meeting_participant (meeting_id, user_id, paid)
VALUES
(1, 1, TRUE),
(1, 2, FALSE);

-- 📢 공지게시판
INSERT INTO notice (title, content)
VALUES
('서버 점검 안내', '2025-11-20 00:00 ~ 02:00 서버 점검 예정');



-- 🚨 신고
INSERT INTO report (reporter_id, target_user_id, target_type, reason)
VALUES
(1, 2, 'USER', '스팸 메시지 발송');

-- -------------------------------
-- 💬 채팅방 (chat_room)
-- -------------------------------

INSERT INTO chat_room (item_id, meeting_id, room_type, host_id)
VALUES
-- 거래용 채팅방: item_id=1, host=2 (판매자)
(1, NULL, 'PRIVATE', 2),
-- 거래용 채팅방: item_id=2, host=2 (판매자)
(2, NULL, 'PRIVATE', 2),
-- 모임용 채팅방: meeting_id=1, host=1
(NULL, 1, 'GROUP', 1);

-- -------------------------------
-- 💭 채팅방 참여자 (chat_room_user)
-- -------------------------------
INSERT INTO chat_room_user (room_id, user_id)
VALUES
-- 거래방 1 참여자: 구매자 1, 판매자 2
(1, 1),
(1, 2),
-- 거래방 2 참여자: 구매자 1, 판매자 2
(2, 1),
(2, 2),
-- 모임방 참여자: 유저 1, 2
(3, 1),
(3, 2);

-- -------------------------------
-- 💭 채팅 메시지 (chat_message)
-- -------------------------------
INSERT INTO chat_message (room_id, sender_id, content)
VALUES
-- 거래방 1
(1, 1, '안녕하세요, 자전거 구매하고 싶습니다.'),
(1, 2, '안녕하세요! 가격 흥정 가능해요.'),
(1, 1, '좋습니다. 그럼 언제 만날까요?'),
-- 거래방 2
(2, 1, '책 대여 가능할까요?'),
(2, 2, '네, 일주일 대여 가능합니다.'),
(2, 1, '좋아요, 내일 수령할게요.'),
-- 모임방
(3, 1, '이번 주 토요일 모임 몇 시에 시작하나요?'),
(3, 2, '오전 9시에 한강공원에서 시작합니다.'),
(3, 1, '좋아요, 그때 봬요!');

-- 🚨 신고
INSERT INTO report (reporter_id, target_user_id, target_type, reason)
VALUES
(1, 2, 'USER', '스팸 메시지 발송');


