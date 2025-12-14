구동을 위해서는 src/main/resources/config/dbConfig.properties 생성 이후 빌드패스에서 소스 탭에서 src/main/resources 폴더를 추가 
    dbConfig.properties 작성예시는 아래와 같음 
    DB_URL=jdbc:mysql://localhost:3306/dorandoran?serverTimezone=UTC
    DB_USER=root
    DB_PASSWORD=1234

DB 세팅


DROP DATABASE IF EXISTS dorandoran;
CREATE DATABASE dorandoran;
USE dorandoran;

SET FOREIGN_KEY_CHECKS = 0;

-- ================================
-- 테이블 생성
-- ================================
-- 🧍 USER
CREATE TABLE user (
    auto_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100) UNIQUE NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50) UNIQUE NOT NULL,
    trust_score INT DEFAULT 0,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 🏠 USER ADDRESS
CREATE TABLE user_address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    road_address VARCHAR(255),
    jibun_address VARCHAR(255),
    addr_detail VARCHAR(255),
    dong_name VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- 🔖 USER TAG
CREATE TABLE user_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tag_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- 📂 FILE RESOURCE
CREATE TABLE file_resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_url VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_name VARCHAR(255),
    file_type VARCHAR(30),
    size BIGINT,
    used_type VARCHAR(30) NOT NULL,
    used_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 🌙 DREAM POST (드림 게시글)
CREATE TABLE dream_post (
  dream_id BIGINT NOT NULL AUTO_INCREMENT,
  writer_id BIGINT NOT NULL,
  writer_type VARCHAR(20) NOT NULL,        -- USER / ADMIN 등
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  category_code VARCHAR(30) NOT NULL,      -- 예: BABY, PET, ETC
  condition_code VARCHAR(20) NOT NULL,     -- 예: NEW, USED
  price INT NOT NULL DEFAULT 0,
  dong VARCHAR(50) NOT NULL,               -- 동 단위 필터
  thumbnail_url TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
  view_count INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
      ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,

  PRIMARY KEY (dream_id),
  KEY idx_dream_status_dong (status, dong),
  FOREIGN KEY (writer_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ⭐ REVIEW
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reviewer_id BIGINT NOT NULL,
    reviewee_id BIGINT NOT NULL,
    item_id BIGINT,
    rating_manner INT DEFAULT 0,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reviewer_id) REFERENCES user(auto_id),
    FOREIGN KEY (reviewee_id) REFERENCES user(auto_id)
);

-- 📍 MEETING LOCATION
CREATE TABLE meeting_location(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    road_address VARCHAR(255),
    jibun_address VARCHAR(255),
    addr_detail VARCHAR(255),
    dong_name VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE
);

-- 🤝 MEETING
CREATE TABLE meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    date DATETIME,
    location_id BIGINT,
    max_members INT,
    current_members INT,
    cost INT DEFAULT 0,
    tag VARCHAR(100),
    status ENUM('OPEN','CLOSED','COMPLETED') DEFAULT 'OPEN',
    view_count INT default 0,
    creator_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    weather VARCHAR(20),
    FOREIGN KEY (location_id) REFERENCES meeting_location(id),
    FOREIGN KEY (creator_id) REFERENCES user(auto_id)
);

-- 👥 MEETING PARTICIPANT
CREATE TABLE meeting_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE,
    UNIQUE (meeting_id, user_id)
);

-- 💬 CHAT ROOM
CREATE TABLE chat_room (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT,
    meeting_id BIGINT,
    room_type ENUM('PRIVATE','GROUP') DEFAULT 'PRIVATE',
    host_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (host_id) REFERENCES user(auto_id),
    FOREIGN KEY (meeting_id) REFERENCES meeting(id)
);

-- 💭 CHAT ROOM USER
CREATE TABLE chat_room_user (
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id, user_id),
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- 💬 CHAT MESSAGE
CREATE TABLE chat_message (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    sender_id BIGINT,
    content TEXT,
    message_type VARCHAR(255),
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user(auto_id) ON DELETE SET NULL
);

-- 🌤 WEATHER DATA
CREATE TABLE weather_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    temp DOUBLE NOT NULL,
    weather VARCHAR(50) NOT NULL,
    pm10 INT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 💰 USER WALLET
CREATE TABLE user_wallet (
    user_id BIGINT PRIMARY KEY,
    balance INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- 💰 WALLET HISTORY
CREATE TABLE wallet_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type ENUM('CHARGE','MEETING_PAY','REFUND') NOT NULL,
    amount INT NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- 💳 MOCK CARD
CREATE TABLE mock_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(30) NOT NULL,
    cvc VARCHAR(10) NOT NULL,
    owner_name VARCHAR(50),
    valid_until VARCHAR(10),
    password VARCHAR(20),
    balance INT NOT NULL DEFAULT 100000
);

-- 📢 NOTICE
CREATE TABLE notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 신고 테이블
CREATE TABLE report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT NOT NULL,
    target_user_id BIGINT NOT NULL,
    target_type ENUM('USER','DREAM','MEETING'),
    reason TEXT,
    status ENUM('PENDING','RESOLVED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES user(auto_id),
    FOREIGN KEY (target_user_id) REFERENCES user(auto_id)
);

SET FOREIGN_KEY_CHECKS = 1;

-- ================================
-- 샘플 데이터 INSERT
-- ================================
INSERT INTO user (user_id, user_password, user_name, nickname, role)
VALUES
('admin01', '1234', '관리자', '관리자닉', 'ADMIN'),
('test01', '1234', '테스트', '닉테스트', 'USER');

INSERT INTO user_address (user_id, road_address, jibun_address, dong_name, addr_detail, latitude, longitude)
VALUES
(1, '서울특별시 은평구 역촌동', '서울특별시 은평구 역촌동 123', '역촌동', '101호', 37.602, 126.927),
(2, '서울특별시 강남구 삼성동', '서울특별시 강남구 삼성동 456', '삼성동', '201호', 37.514, 127.063);

INSERT INTO meeting_location (road_address, jibun_address, addr_detail, dong_name, latitude, longitude)
VALUES ('서울특별시 한강공원', '서울특별시 용산구 한강로', '1구역', '한강동', 37.526, 126.927);

INSERT INTO meeting (title, content, date, location_id, max_members, current_members, cost, tag, status, creator_id, weather, created_at)
VALUES ('조깅 모임', '매주 토요일 조깅', '2025-12-15 09:00:00', 1, 10, 2, 1000000, '운동', 'OPEN', 2, '맑음', '2025-12-10 08:00:00');

INSERT INTO meeting_participant (meeting_id, user_id, paid)
VALUES (1, 1, TRUE), (1, 2, FALSE);

INSERT INTO chat_room (item_id, meeting_id, room_type, host_id)
VALUES
(1, NULL, 'PRIVATE', 2),
(2, NULL, 'PRIVATE', 2),
(NULL, 1, 'GROUP', 1);

INSERT INTO chat_room_user (room_id, user_id)
VALUES
(1, 1),(1, 2),
(2, 1),(2, 2),
(3, 1),(3, 2);

INSERT INTO chat_message (room_id, sender_id, content)
VALUES
(1, 1, '안녕하세요, 자전거 구매하고 싶습니다.'),
(1, 2, '안녕하세요! 가격 흥정 가능해요.'),
(2, 1, '책 대여 가능할까요?'),
(2, 2, '일주일 대여 가능합니다.'),
(3, 1, '이번주 토요일 모임은 몇 시인가요?'),
(3, 2, '오전 9시 한강공원입니다.');

-- DREAM POST 샘플 데이터
INSERT INTO dream_post
(writer_id, writer_type, title, content, category_code, condition_code, price, dong, thumbnail_url, status)
VALUES
(2, 'USER',
 '유모차 나눔합니다',
 '아이 다 커서 유모차 필요하신 분께 드려요.',
 'BABY', 'USED', 0,
 '삼성동',
 'https://example.com/images/stroller.jpg',
 'OPEN'),

(2, 'USER',
 '강아지 장난감 드림',
 '소형견 장난감 무료 나눔합니다.',
 'PET', 'USED', 0,
 '삼성동',
 'https://example.com/images/dogtoy.jpg',
 'OPEN'),

(1, 'ADMIN',
 '아기 침대 드림',
 '관리자 검수 완료된 아기 침대입니다.',
 'BABY', 'USED', 0,
 '역촌동',
 NULL,
 'OPEN');

-- 유저 지갑 및 카드 데이터
INSERT INTO user_wallet (user_id, balance)
VALUES
(1, 50000),
(2, 12000);

INSERT INTO wallet_history (user_id, type, amount, description)
VALUES
(2, 'CHARGE', 20000, '카드 충전');

INSERT INTO mock_card (card_number, cvc, owner_name, valid_until, password, balance)
VALUES
('1111-2222-3333-4444', '123', '홍길동', '12/27', '12', 1000000);

-- 확인용 SELECT
SELECT * FROM user_wallet;
SELECT * FROM mock_card;
SELECT * FROM wallet_history;
SELECT * FROM meeting;
SELECT * FROM meeting_participant;
SELECT * FROM meeting_location;
SELECT * FROM chat_room;
SELECT * FROM chat_room_user;
SELECT * FROM chat_message;
SELECT * FROM user;
SELECT * FROM user_address;
SELECT * FROM file_resource;
SELECT * FROM report;
SELECT * FROM chat_room;
SELECT * FROM chat_room_user;
SELECT * FROM chat_message;
SELECT * FROM user;
SELECT * FROM user_address;
SELECT * FROM file_resource;
SELECT * FROM report;
SELECT * FROM weather_data;
