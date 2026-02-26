DROP DATABASE IF EXISTS bajamaja_rental;
CREATE DATABASE bajamaja_rental;
USE bajamaja_rental;

create table BajaMajas (
                           id bigint not null auto_increment,
                           handicap bit,
                           name varchar(30) not null,
                           number_of_stalls integer not null,
                           rental_rate float(53) not null,
                           type enum ('BAJAMAJA','DECORATION','PLATFORM') not null,
                           primary key (id)
);

create table customers (
                           id bigint not null auto_increment,
                           email varchar(120) not null,
                           name varchar(120) not null,
                           primary key (id)
);

create table decorations (
                             id bigint not null auto_increment,
                             color enum ('BLACK','BLUE','BROWN','GOLD','GREEN','ORANGE','PINK','PURPLE','RED','SILVER','WHITE','YELLOW'),
                             name varchar(30) not null,
                             rental_rate float(53) not null,
                             type enum ('BAJAMAJA','DECORATION','PLATFORM') not null,
                             primary key (id)
);

create table join_platforms_bajamajas (
                                          platform_id bigint not null,
                                          bajamaja_id bigint not null,
                                          primary key (platform_id, bajamaja_id)
);

create table platforms (
                           id bigint not null auto_increment,
                           name varchar(30) not null,
                           rental_rate float(53) not null,
                           type enum ('BAJAMAJA','DECORATION','PLATFORM') not null,
                           primary key (id)
);

create table rentals (
                         id bigint not null auto_increment,
                         dailyRate float(53) not null,
                         end_date datetime(6) not null,
                         extraPayment float(53),
                         originalPayment float(53),
                         refund float(53),
                         rental_object_id bigint not null,
                         rental_object_type enum ('BAJAMAJA','DECORATION','PLATFORM') not null,
                         return_date datetime(6),
                         start_date datetime(6) not null,
                         customer_id bigint not null,
                         primary key (id)
);