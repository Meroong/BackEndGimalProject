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

CREATE TABLE weather_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    temp DOUBLE NOT NULL,              -- 현재 온도
    weather VARCHAR(20) NOT NULL,      -- 맑음, 흐림, 비 등
    pm10 INT NOT NULL,                 -- 미세먼지
    latitude DOUBLE NOT NULL,          -- 위도
    longitude DOUBLE NOT NULL,         -- 경도
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_weather_location_time (latitude, longitude, created_at)
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
SET FOREIGN_KEY_CHECKS = 0;

-- ================================
-- 🌦 WEATHER_DATA
-- ================================
CREATE TABLE IF NOT EXISTS weather_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    temp DOUBLE NOT NULL,
    weather VARCHAR(20) NOT NULL,
    pm10 INT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_weather_location_time (latitude, longitude, created_at)
);



-- ================================
-- 👤 USER
-- ================================
INSERT INTO user
(user_id, user_password, user_name, nickname, trust_score, role)
VALUES
('admin01','1234','관리자','관리자닉',100,'ADMIN'),
('user01','1234','김철수','철수',30,'USER'),
('user02','1234','이영희','영희',50,'USER');

-- ================================
-- 🏠 USER_ADDRESS
-- ================================

INSERT INTO user_address
(user_id, road_address, jibun_address, addr_detail, dong_name, latitude, longitude)
VALUES
(1,'서울특별시 은평구 연서로 59','서울특별시 은평구 역촌동 14-63','101호','역촌동',37.606215,126.922028),
(2,'서울특별시 강남구 테헤란로 521','서울특별시 강남구 삼성동 159','202호','삼성동',37.5157864094542 ,127.051319226082),
(3,'서울특별시 마포구 마포대로 130','서울특별시 마포구 공덕동 456','303호','공덕동',37.545487,126.951523);
select * from user_address;
-- ================================
-- 📂 FILE_RESOURCE
-- ================================
INSERT INTO file_resource
(file_url, file_name, original_name, file_type, size, used_type, used_id)
VALUES
('/upload/profile/saram.png','saram.png','saram.png','image/png',3540,'PROFILE',1),
('/upload/profile/saram2.png','saram2.png','saram2.png','image/png',3540,'PROFILE',2),
('/upload/profile/saram3.png','saram3.png','saram3.png','image/png',3540,'PROFILE',3);

INSERT INTO file_resource
(file_url, file_name, original_name, file_type, size, used_type, used_id)
VALUES
('/upload/post/1/jutByeung.jpg','jutByeung.jpg','jutByeung.jpg','image/png',8123,'POST',1),
('/upload/post/2/bohang.png','bohang.png','bohang.png','image/png',8123,'POST',2);
-- ('/upload/post/1/jutByeung.jpg','jutByeung.jpg','jutByeung.jpg','image/png',8123,'POST',2),
-- ('/upload/post/2/bohang.png','bohang.png','bohang.png','image/png',8123,'POST',3);
UPDATE dream_post
SET thumbnail_url = '/upload/post/1/jutByeung.jpg'
WHERE dream_id = 1;

UPDATE dream_post
SET thumbnail_url = '/upload/post/2/bohang.png'
WHERE dream_id = 2;

INSERT INTO file_resource
(used_id, used_type, original_name, file_name, file_type, size, file_url)
VALUES
-- 역촌 조깅
(1, 'MEETING', 'running.jpg', 'running.jpg', 'image/png', 2048, '/upload/meeting/running.jpg'),

-- 은평 요가
(2, 'MEETING', 'yoga.jpg', 'yoga.jpg', 'image/png', 2048, '/upload/meeting/yoga.jpg'),

-- 삼성 산책
(3, 'MEETING', 'walk.jpg', 'walk.jpg', 'image/png', 2048, '/upload/meeting/walk.jpg'),

-- 코엑스 독서
(4, 'MEETING', 'reading.jpg', 'reading.jpg', 'image/png', 2048, '/upload/meeting/reading.jpg'),

-- 공덕 러닝
(5, 'MEETING', 'running2.jpg', 'running2.jpg', 'image/png', 2048, '/upload/meeting/running2.jpg'),

-- 공덕 브런치
(6, 'MEETING', 'brunch.jpg', 'brunch.jpg', 'image/png', 2048, '/upload/meeting/brunch.jpg');



-- ================================
-- 🌙 DREAM_POST
-- ================================
INSERT INTO dream_post
(writer_id, writer_type, title, content, category_code, condition_code, price, dong)
VALUES
(2,'USER','젖병 나눔합니다','선물 받아서 안쓰는 젖병 나눔해요! 삼성동까지 직접 와주세요!','ERRAND','흠집없는 중고',10000,'삼성동'),
(3,'USER','보행기 나눔','보행기 나눔합니다. 아이가 이제 잘 걸어다녀서 필요없어서 나눔합니다!','수유/이유용품','새거',5000,'공덕동');

-- ================================
-- ⭐ REVIEW
-- ================================
INSERT INTO review
(reviewer_id, reviewee_id, item_id, rating_manner, content)
VALUES
(2,3,1,5,'시간 약속 잘 지켜요'),
(3,2,2,4,'친절했어요');

-- ================================
-- 📍 MEETING_LOCATION
-- ================================
INSERT INTO meeting_location
(road_address, jibun_address, addr_detail, dong_name, latitude, longitude)
VALUES
('서울특별시 은평구 연서로 50','서울특별시 은평구 역촌동 13-45','근린공원','역촌동',37.605512,126.920931),
('서울특별시 은평구 역말로 37','서울특별시 은평구 역촌동 17-12','체육센터','역촌동',37.607114,126.923554),

('서울특별시 강남구 봉은사로 524','서울특별시 강남구 삼성동 73','봉은사 입구','삼성동',37.514575,127.057152),
('서울특별시 강남구 영동대로 513','서울특별시 강남구 삼성동 168-1','코엑스','삼성동',37.511823,127.059159),

('서울특별시 마포구 백범로 23','서울특별시 마포구 공덕동 256','오거리','공덕동',37.543971,126.950355),
('서울특별시 마포구 마포대로 109','서울특별시 마포구 공덕동 404','공덕역','공덕동',37.544706,126.951822);

-- ================================
-- 🤝 MEETING
-- ================================
INSERT INTO meeting
(title, content, date, location_id, max_members, current_members, cost, tag, creator_id, weather)
VALUES
('역촌 조깅','아침 러닝','2025-12-20 07:00:00',1,10,3,0,'운동',1,'맑음'),
('은평 요가','요가 클래스','2025-12-21 10:00:00',2,8,2,5000,'요가',1,'흐림'),

('삼성 산책','봉은사 산책','2025-12-20 08:00:00',3,10,4,0,'산책',2,'맑음'),
('코엑스 독서','카페 독서','2025-12-21 14:00:00',4,6,3,0,'독서',2,'맑음'),

('공덕 러닝','오거리 러닝','2025-12-20 06:30:00',5,12,5,0,'운동',3,'흐림'),
('공덕 브런치','역 근처 브런치','2025-12-22 11:00:00',6,6,2,10000,'식사',3,'맑음');

-- ================================
-- 👥 MEETING_PARTICIPANT
-- ================================
INSERT INTO meeting_participant
(meeting_id, user_id, paid)
VALUES
(1,1,TRUE),(1,2,FALSE),
(3,2,TRUE),(4,2,FALSE),
(5,3,TRUE),(6,3,FALSE);

-- ================================
-- 💬 CHAT_ROOM
-- ================================
INSERT INTO chat_room
(item_id, meeting_id, room_type, host_id)
VALUES
(NULL,1,'GROUP',1),
(NULL,3,'GROUP',2),
(NULL,5,'GROUP',3);

-- ================================
-- 💭 CHAT_ROOM_USER
-- ================================
INSERT INTO chat_room_user
(room_id, user_id)
VALUES
(1,1),(1,2),
(2,2),
(3,3);

-- ================================
-- 💬 CHAT_MESSAGE
-- ================================
INSERT INTO chat_message
(room_id, sender_id, message_type, content)
VALUES
(1,1,'TEXT','안녕하세요!'),
(1,2,'TEXT','반갑습니다'),
(2,2,'TEXT','모임 시간 확인해주세요');

-- ================================
-- 📊 POLL
-- ================================
INSERT INTO poll
(room_id, title, expire_at)
VALUES
(1,'모임 시간 투표','2025-12-19 23:59:59');

INSERT INTO poll_option
(poll_id, option_text)
VALUES
(1,'오전 7시'),
(1,'오전 8시'),
(1,'오전 9시');

INSERT INTO poll_vote
(poll_id, user_id, option_id)
VALUES
(1,1,1),
(1,2,2);

-- ================================
-- 💰 USER_WALLET
-- ================================
INSERT INTO user_wallet
(user_id, balance)
VALUES
(1,100000),
(2,50000),
(3,30000);

-- ================================
-- 💰 WALLET_HISTORY
-- ================================
INSERT INTO wallet_history
(user_id, type, amount, description)
VALUES
(2,'CHARGE',30000,'카드 충전'),
(3,'CHARGE',20000,'카드 충전'),
(2,'MEETING_PAY',10000,'요가 클래스 결제');

-- ================================
-- 💳 MOCK_CARD
-- ================================
INSERT INTO mock_card
(card_number, cvc, owner_name, valid_until, password, balance)
VALUES
('1111-2222-3333-4444','123','김철수','12/27','12',100000),
('2222-3333-4444-5555','234','이영희','11/26','23',80000),
('5555-6666-7777-8888','567','관리자','08/28','56',999999);

-- ================================
-- 🚨 REPORT
-- ================================
INSERT INTO report
(reporter_id, target_user_id, target_type, reason)
VALUES
(2,3,'USER','비매너 언행');

SET FOREIGN_KEY_CHECKS = 1;


select * from dream_post;

