CREATE TABLE t_train_station (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  train_id BIGINT NOT NULL,
  station_id INT NOT NULL,
  station_name VARCHAR(50),
  sequence INT NOT NULL COMMENT '站序(0,1,2...)'
) ENGINE=InnoDB;

CREATE TABLE t_train_seat (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  train_id BIGINT NOT NULL,
  carriage_num INT NOT NULL,
  row_num INT NOT NULL,
  col_num VARCHAR(5) NOT NULL,
  seat_type TINYINT COMMENT '1:一等, 2:二等, 3:三等',
  seat_bitmap VARCHAR(64) DEFAULT '000000000' COMMENT '9个区间状态',
  UNIQUE KEY uk_seat (train_id, carriage_num, row_num, col_num)
) ENGINE=InnoDB;

CREATE TABLE t_train_ticket_stock (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  train_id BIGINT NOT NULL,
  seat_type TINYINT COMMENT '1:一等, 2:二等, 3:三等',
  from_station_id INT NOT NULL,
  from_index INT NOT NULL,
  to_station_id INT NOT NULL,
  to_index INT NOT NULL,
  stock INT DEFAULT 0,
  UNIQUE KEY uk_route_type (train_id, from_station_id, to_station_id, seat_type)
) ENGINE=InnoDB;

CREATE TABLE t_member (
  id VARCHAR(20) PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  permissions VARCHAR(200) DEFAULT 'BUY_TICKET',
  frequent_passengers TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE t_order (
  id VARCHAR(18) PRIMARY KEY,
  member_id VARCHAR(20) NOT NULL,
  train_no VARCHAR(20) NOT NULL,
  travel_date DATE NOT NULL,
  seat_info VARCHAR(200),
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_member_date(member_id, travel_date)
) ENGINE=InnoDB;
