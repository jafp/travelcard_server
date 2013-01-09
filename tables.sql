drop table if exists tracking_points;
drop table if exists journeys;
drop table if exists customers;

create table customers (
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
	card_id BIGINT NOT NULL,
	
	status INTEGER DEFAULT 0,
	balance INTEGER DEFAULT 0,
	name varchar(255) not null,
	
	created_at TIMESTAMP default 0,
	updated_at TIMESTAMP default 0
);

create table journeys (
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
	customer_id INTEGER NOT NULL,
	
	price INTEGER default 0,
	status INTEGER default 0,
	
	start_zone INTEGER,
	start_time TIMESTAMP,
	
	end_zone INTEGER,
	end_time TIMESTAMP,
	
	created_at TIMESTAMP DEFAULT 0,
	updated_at TIMESTAMP DEFAULT 0
);

create table tracking_points (
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
	customer_id INTEGER,
	zone INTEGER,
	tracking_type INTEGER,
	created_at TIMESTAMP
);
