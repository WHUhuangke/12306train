INSERT INTO t_train_station(train_id, station_id, station_name, sequence) VALUES
(1, 10, '北京南', 0),
(1, 20, '济南西', 1),
(1, 30, '南京南', 2),
(1, 40, '上海虹桥', 3);

INSERT INTO t_train_seat(train_id, carriage_num, row_num, col_num, seat_type) VALUES
(1, 1, 1, 'A', 1),(1, 1, 1, 'B', 1),(1, 2, 1, 'A', 2),(1, 2, 1, 'B', 2),(1, 3, 1, 'A', 3);

INSERT INTO t_train_ticket_stock(train_id, seat_type, from_station_id, from_index, to_station_id, to_index, stock) VALUES
(1,1,10,0,20,1,30),(1,1,10,0,30,2,30),(1,1,10,0,40,3,30),
(1,2,10,0,20,1,50),(1,2,10,0,30,2,50),(1,2,10,0,40,3,50),
(1,3,10,0,20,1,80),(1,3,10,0,30,2,80),(1,3,10,0,40,3,80);
