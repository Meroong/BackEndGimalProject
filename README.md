구동을 위해서는 src/main/resources/config/dbConfig.properties 생성 이후 빌드패스에서 소스 탭에서 src/main/resources 폴더를 추가 
    dbConfig.properties 작성예시는 아래와 같음 
    DB_URL=jdbc:mysql://localhost:3306/dorandoran?serverTimezone=UTC
    DB_USER=root
    DB_PASSWORD=1234

DB 세팅


create database dorandoran;
use dorandoran;


SET FOREIGN_KEY_CHECKS = 0;

-- 📍 지역정보 테이블
DROP TABLE IF EXISTS user_address;
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
DROP TABLE IF EXISTS file_resource;
-- 🏷️ 유저 태그
DROP TABLE IF EXISTS user_tag;
-- 🤝 모임참여자 관리
DROP TABLE IF EXISTS meeting_location;
-- 🤝 모임참여자 관리
DROP TABLE IF EXISTS meeting_participant;
-- 🤝 모임 게시판
DROP TABLE IF EXISTS meeting;
-- 📢 공지게시판
DROP TABLE IF EXISTS notice;
-- 🧍 USER 관련
DROP TABLE IF EXISTS user;

SET FOREIGN_KEY_CHECKS = 1;

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
    role ENUM('USER', 'ADMIN', 'BLOCK') DEFAULT 'USER',
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
    latitude DOUBLE,
    longitude DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ================================
-- 🔖 USER TAG
-- ================================
CREATE TABLE user_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tag_name VARCHAR(100) NOT NULL,
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
-- 🛒 ITEM (드림/교환/중고)
-- ================================
CREATE TABLE item (
    item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    price INT NOT NULL,
    trade_type ENUM('SALE', 'RENTAL', 'DREAM') DEFAULT 'SALE',
    status ENUM('AVAILABLE', 'RESERVED', 'COMPLETED') DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES user(auto_id)
);

-- ================================
-- 🔁 RENTAL INFO
-- ================================
CREATE TABLE rental_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    deposit INT DEFAULT 0,
    daily_rate INT,
    rental_period INT,
    return_date DATE,
    FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
);

-- ================================
-- ❤️ WISHLIST
-- ================================
CREATE TABLE wishlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
);

-- ================================
-- 💳 TRANSACTION
-- ================================
CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    status ENUM('IN_PROGRESS','COMPLETED','CANCELLED') DEFAULT 'IN_PROGRESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (item_id) REFERENCES item(item_id),
    FOREIGN KEY (buyer_id) REFERENCES user(auto_id),
    FOREIGN KEY (seller_id) REFERENCES user(auto_id)
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
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE,
    UNIQUE (meeting_id, user_id)
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
    content TEXT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES chat_room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES user(auto_id) ON DELETE SET NULL
);

-- ================================
-- 🌤 WEATHER DATA
-- ================================
CREATE TABLE weather_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    temp DOUBLE NOT NULL,
    weather VARCHAR(50) NOT NULL,
    pm10 INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================
-- 💰 USER WALLET
-- ================================
CREATE TABLE user_wallet (
    user_id BIGINT PRIMARY KEY,
    balance INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ================================
-- 💰 WALLET HISTORY
-- ================================
CREATE TABLE wallet_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type ENUM('CHARGE','MEETING_PAY','REFUND') NOT NULL,
    amount INT NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(auto_id) ON DELETE CASCADE
);

-- ================================
-- 💳 MOCK CARD
-- ================================
CREATE TABLE mock_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(30) NOT NULL,
    cvc VARCHAR(10) NOT NULL,
    owner_name VARCHAR(50),
    valid_until VARCHAR(10),
    password VARCHAR(20),
    balance INT NOT NULL DEFAULT 100000
);

-- ================================
-- 📢 NOTICE
-- ================================
CREATE TABLE notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
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
    PRIMARY KEY (poll_id, user_id),  -- 사용자당 1회 제한
    FOREIGN KEY (poll_id) REFERENCES poll(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES poll_option(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(auto_id)
);


-- ================================
-- 🚨 REPORT
-- ================================
CREATE TABLE report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reporter_id BIGINT  NULL,
    target_user_id BIGINT  NULL,
    target_type ENUM('USER','ITEM','MEETING'),
    reason TEXT,
    status ENUM('PENDING','RESOLVED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES user(auto_id) ON DELETE SET NULL,
    FOREIGN KEY (target_user_id) REFERENCES user(auto_id) ON DELETE SET NULL
);
ALTER TABLE report
ADD COLUMN target_id BIGINT NULL AFTER target_type;

CREATE INDEX idx_report_meeting ON report(target_type, target_id, status);
-- ================================
-- 📌 샘플 데이터 INSERT
-- ================================

INSERT INTO user (user_id, user_password, user_name, nickname, role)
VALUES
('admin01', '1234', '관리자', '관리자닉', 'ADMIN'),
('test01', '1234', '테스트', '닉테스트', 'USER');
INSERT INTO user_address (user_id, road_address, jibun_address, addr_detail, latitude, longitude)
VALUES
(1, '서울특별시 은평구 역촌동', '서울특별시 은평구 역촌동 123', '101호', 37.602, 126.927),
(2, '서울특별시 강남구 삼성동', '서울특별시 강남구 삼성동 456', '201호', 37.514, 127.063);

INSERT INTO meeting_location (road_address, jibun_address, addr_detail, latitude, longitude)
VALUES ('서울특별시 한강공원', '서울특별시 용산구 한강로', '1구역', 37.526, 126.927);

INSERT INTO meeting (title, content, date, location_id, max_members, current_members, cost, tag, status, creator_id, weather)
VALUES ('조깅 모임', '매주 토요일 조깅', '2025-11-22 09:00:00', 1, 10, 2, 0, '운동', 'OPEN', 2, '맑음');

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

-- 💭 채팅 메시지
INSERT INTO chat_message (room_id, sender_id, content)
VALUES
(1, 1, '안녕하세요, 자전거 구매하고 싶습니다.'),
(1, 2, '안녕하세요! 가격 흥정 가능해요.'),
(1, 1, '좋습니다. 그럼 언제 만날까요?'),
(2, 1, '책 대여 가능할까요?'),
(2, 2, '네, 일주일 대여 가능합니다.'),
(2, 1, '좋아요, 내일 수령할게요.'),
(3, 1, '이번 주 토요일 모임 몇 시에 시작하나요?'),
(3, 2, '오전 9시에 한강공원에서 시작합니다.'),
(3, 1, '좋아요, 그때 봬요!');

-- 💾 file_resource 예시 데이터



select * from meeting;
select * from meeting_participant;
select * from meeting_location;
select * from chat_room;
select * from chat_room_user;
select * from user;
select * from user_address;
select * from file_resource;



-- 신고 테스트 데이터 5개 (report)
-- 전제: user 테이블에 auto_id 1(관리자), 2(테스트유저)가 존재

INSERT INTO report (reporter_id, target_user_id, target_type, reason, status, created_at)
VALUES
(2, 1, 'USER',    '채팅에서 비매너/욕설이 있었습니다.',                 'PENDING',  NOW()),
(2, 1, 'MEETING', '모임 공지 내용이 실제와 다르고 허위 안내로 보입니다.', 'PENDING',  NOW()),
(2, 1, 'ITEM',    '중고 상품 설명과 실제 상태가 다르다고 생각됩니다.',    'PENDING',  NOW()),
(1, 2, 'USER',    '스팸성 메시지/도배 행위가 반복됩니다.',               'RESOLVED', NOW()),
(1, 2, 'MEETING', '모임에서 규칙 위반 신고가 접수되었습니다.',           'PENDING',  NOW());

-- 확인
SELECT * FROM report ORDER BY id DESC;

SELECT auto_id, user_id FROM user ORDER BY auto_id;
DELETE FROM user WHERE user_id = 'test01';


-- ================================
-- 📍 meeting_location 추가 4개 (총 5개 장소)
-- 기존에 id=1이 있다고 가정
-- ================================
INSERT INTO meeting_location (road_address, jibun_address, addr_detail, latitude, longitude) VALUES
('서울특별시 올림픽공원', '서울특별시 송파구 방이동', '평화의문 앞', 37.5163, 127.1210),
('서울특별시 서울숲',     '서울특별시 성동구 성수동1가', '중앙광장',   37.5445, 127.0374),
('서울특별시 여의도공원', '서울특별시 영등포구 여의도동', '분수대 앞',  37.5260, 126.9237),
('서울특별시 북서울꿈의숲','서울특별시 강북구 번동',      '정문 앞',    37.6249, 127.0417);

-- ================================
-- 🤝 meeting 5개 생성
-- location_id: 1~5 사용
-- creator_id: 1(관리자), 2(테스트유저) 섞어서 사용
-- current_members는 max_members 이하로 설정
-- ================================
INSERT INTO meeting
(title, content, date, location_id, max_members, current_members, cost, tag, status, creator_id, weather)
VALUES
('조깅 모임(주말 아침)', '가볍게 5km 조깅하고 스트레칭까지 합니다.', '2025-12-20 09:00:00', 1, 10, 2, 0, '운동', 'OPEN', 2, '맑음'),
('플로깅(줍깅) 같이해요', '공원 한 바퀴 돌면서 쓰레기 줍는 모임입니다. 장갑/봉투 준비해요.', '2025-12-21 10:30:00', 2, 15, 3, 0, '봉사', 'OPEN', 1, '구름'),
('강아지 산책 번개', '반려견 있으면 같이 산책하고 사진도 찍어요. (반려견 없어도 OK)', '2025-12-22 19:30:00', 3, 8, 1, 0, '산책', 'OPEN', 2, '맑음'),
('중고 나눔 장터 모임', '아이 물건/장난감 위주로 서로 나눔/교환하는 자리입니다.', '2025-12-23 14:00:00', 4, 20, 5, 0, '나눔', 'OPEN', 1, '맑음'),
('엄마아빠 육아 수다회', '육아템 추천/정보 공유하고, 아이들 교육 얘기도 편하게 나눠요.', '2025-12-24 11:00:00', 5, 12, 4, 1000, '육아', 'OPEN', 1, '눈');

-- 확인
SELECT id, title, date, location_id, max_members, current_members, tag, status, creator_id
FROM meeting
ORDER BY id DESC
LIMIT 10;


SELECT COUNT(*) AS user_count FROM `user`;
