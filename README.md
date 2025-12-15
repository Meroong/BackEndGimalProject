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
-- 🧍 USER
-- ================================
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

-- ================================
-- 🏠 USER ADDRESS
-- ================================
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


-- ================================
-- 📂 FILE RESOURCE
-- ================================
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

-- ================================
-- 🌙 DREAM POST
-- ================================
CREATE TABLE dream_post (
  dream_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  writer_id BIGINT NOT NULL,
  writer_type VARCHAR(20) NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  category_code VARCHAR(30) NOT NULL,
  condition_code VARCHAR(20) NOT NULL,
  price INT NOT NULL DEFAULT 0,
  dong VARCHAR(50) NOT NULL,
  thumbnail_url TEXT,
  status VARCHAR(20) DEFAULT 'OPEN',
  view_count INT DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted TINYINT(1) DEFAULT 0,
  KEY idx_dream_status_dong (status, dong),
  FOREIGN KEY (writer_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ================================
-- ⭐ REVIEW
-- ================================
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

-- ================================
-- 📍 MEETING LOCATION
-- ================================
CREATE TABLE meeting_location(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    road_address VARCHAR(255),
    jibun_address VARCHAR(255),
    addr_detail VARCHAR(255),
    dong_name VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE
);

-- ================================
-- 🤝 MEETING
-- ================================
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
    view_count INT DEFAULT 0,
    creator_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    weather VARCHAR(20),
    FOREIGN KEY (location_id) REFERENCES meeting_location(id),
    FOREIGN KEY (creator_id) REFERENCES user(auto_id)
);

-- ================================
-- 👥 MEETING PARTICIPANT
-- ================================
CREATE TABLE meeting_participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (meeting_id, user_id),
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ================================
-- 💬 CHAT ROOM
-- ================================
CREATE TABLE chat_room (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT,
    meeting_id BIGINT,
    room_type ENUM('PRIVATE','GROUP') DEFAULT 'PRIVATE',
    host_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (host_id) REFERENCES user(auto_id),
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE
);

-- ================================
-- 💭 CHAT ROOM USER
-- ================================
CREATE TABLE chat_room_user (
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id, user_id),
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ================================
-- 💬 CHAT MESSAGE
-- ================================
CREATE TABLE chat_message (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    sender_id BIGINT,
    message_type ENUM('TEXT','IMAGE','FILE') DEFAULT 'TEXT',
    content TEXT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user(auto_id) ON DELETE SET NULL
);

-- ================================
-- 📊 POLL
-- ================================
CREATE TABLE poll (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    expire_at DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE
);

CREATE TABLE poll_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    poll_id BIGINT NOT NULL,
    option_text VARCHAR(255) NOT NULL,
    FOREIGN KEY (poll_id) REFERENCES poll(id) ON DELETE CASCADE
);

CREATE TABLE poll_vote (
    poll_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    option_id BIGINT NOT NULL,
    voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (poll_id, user_id),
    FOREIGN KEY (poll_id) REFERENCES poll(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES poll_option(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id)
);

-- ================================
-- 💰 USER WALLET
-- ================================
CREATE TABLE user_wallet (
    user_id BIGINT PRIMARY KEY,
    balance INT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ================================
-- 💰 WALLET HISTORY
-- ================================
CREATE TABLE wallet_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type ENUM('CHARGE','MEETING_PAY','REFUND'),
    amount INT,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id)
);

-- ================================
-- 💳 MOCK CARD
-- ================================
CREATE TABLE mock_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(30),
    cvc VARCHAR(10),
    owner_name VARCHAR(50),
    valid_until VARCHAR(10),
    password VARCHAR(20),
    balance INT DEFAULT 100000
);

-- ================================
-- 📢 NOTICE
-- ================================
CREATE TABLE notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================
-- 🚨 REPORT
-- ================================
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
-- 📌 INSERT DATA
-- ================================

INSERT INTO user (user_id, user_password, user_name, nickname, role) VALUES
('admin01','1234','관리자','관리자닉','ADMIN'),
('user01','1234','김철수','철수','USER'),
('user02','1234','이영희','영희','USER'),
('user03','1234','박민수','민수','USER'),
('user04','1234','최지은','지은','USER');

INSERT INTO user_address (user_id, road_address, jibun_address, addr_detail, dong_name, latitude, longitude) VALUES
(1,'서울 은평구','역촌동 1','101호','역촌동',37.60,126.92),
(2,'서울 강남구','삼성동 2','202호','삼성동',37.51,127.06),
(3,'서울 마포구','공덕동 3','303호','공덕동',37.55,126.95),
(4,'서울 송파구','잠실동 4','404호','잠실동',37.51,127.10),
(5,'서울 서초구','반포동 5','505호','반포동',37.50,127.01);

INSERT INTO meeting_location (road_address, jibun_address, addr_detail, dong_name, latitude, longitude) VALUES
('한강공원','이촌동','1구역','이촌동',37.52,126.97),
('서울숲','성수동','정문','성수동',37.54,127.04),
('올림픽공원','방이동','광장','방이동',37.51,127.12),
('남산공원','회현동','타워','회현동',37.55,126.98),
('보라매공원','신대방동','중앙','신대방동',37.49,126.92);

INSERT INTO meeting (title, content, date, location_id, max_members, current_members, cost, tag, creator_id, weather) VALUES
('조깅 모임','아침 조깅','2025-12-20 09:00:00',1,10,3,0,'운동',2,'맑음'),
('독서 모임','책 읽기','2025-12-21 14:00:00',2,6,2,0,'독서',3,'흐림'),
('반려견 산책','강아지와 산책','2025-12-22 10:00:00',3,8,4,0,'펫',4,'맑음'),
('사진 출사','야경 촬영','2025-12-23 18:00:00',4,5,1,0,'사진',5,'맑음'),
('요가 클래스','야외 요가','2025-12-24 08:00:00',5,7,3,0,'요가',2,'흐림');

INSERT INTO meeting_participant (meeting_id, user_id, paid) VALUES
(1,2,TRUE),(1,3,FALSE),(1,4,FALSE),
(2,3,TRUE),(2,5,FALSE),
(3,4,TRUE),(3,2,FALSE),
(4,5,TRUE),
(5,2,FALSE),(5,3,FALSE);

INSERT INTO chat_room (item_id, meeting_id, room_type, host_id) VALUES
(NULL,1,'GROUP',2),
(NULL,2,'GROUP',3),
(1,NULL,'PRIVATE',2),
(2,NULL,'PRIVATE',3),
(NULL,3,'GROUP',4);

INSERT INTO chat_room_user (room_id, user_id) VALUES
(1,2),(1,3),(1,4),
(2,3),(2,5),
(3,2),(3,4),
(4,3),(4,5),
(5,2),(5,4);

INSERT INTO chat_message (room_id, sender_id, message_type, content) VALUES
(1,2,'TEXT','조깅 몇 시에 하나요?'),
(1,3,'TEXT','아침 9시입니다'),
(2,3,'TEXT','책은 각자 가져오나요?'),
(3,2,'TEXT','드림 물품 아직 있나요?'),
(4,5,'TEXT','사진 장비 뭐 쓰세요?');

INSERT INTO poll (room_id, title, expire_at) VALUES
(1,'모임 시간 투표','2025-12-19 23:59:59');

INSERT INTO poll_option (poll_id, option_text) VALUES
(1,'오전 9시'),
(1,'오전 10시'),
(1,'오후 2시');

INSERT INTO poll_vote (poll_id, user_id, option_id) VALUES
(1,2,1),
(1,3,2),
(1,4,1);

INSERT INTO user_wallet (user_id, balance) VALUES
(1,100000),(2,50000),(3,30000),(4,20000),(5,15000);

INSERT INTO wallet_history (user_id, type, amount, description) VALUES
(2,'CHARGE',30000,'카드 충전'),
(3,'CHARGE',20000,'카드 충전'),
(4,'MEETING_PAY',5000,'모임 참가비'),
(5,'REFUND',3000,'환불'),
(2,'MEETING_PAY',10000,'요가 클래스');

INSERT INTO mock_card (card_number, cvc, owner_name, valid_until, password, balance) VALUES
('1111-2222-3333-4444','123','김철수','12/27','12',100000),
('2222-3333-4444-5555','234','이영희','11/26','34',80000),
('3333-4444-5555-6666','345','박민수','10/25','56',50000),
('4444-5555-6666-7777','456','최지은','09/24','78',120000),
('5555-6666-7777-8888','567','관리자','08/28','90',999999);

INSERT INTO report (reporter_id, target_user_id, target_type, reason) VALUES
(2,3,'USER','비매너 언행'),
(3,4,'DREAM','허위 게시물'),
(4,5,'MEETING','노쇼'),
(5,2,'USER','욕설'),
(1,3,'DREAM','부적절한 내용');

SELECT * FROM weather_data;
