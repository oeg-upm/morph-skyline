DROP SCHEMA IF EXISTS skyline CASCADE;
CREATE SCHEMA IF NOT EXISTS skyline;

CREATE TABLE skyline.STOPS (stop_id VARCHAR(200),stop_code VARCHAR(200),stop_name VARCHAR(200),stop_desc VARCHAR(200),stop_lat DECIMAL(18,15),stop_lon DECIMAL(18,15),zone_id VARCHAR(200),stop_url VARCHAR(200),location_type INT,parent_station VARCHAR(200),stop_timezone VARCHAR(200) DEFAULT NULL,wheelchair_boarding INT,PRIMARY KEY (stop_id));
CREATE TABLE skyline.ROUTES (route_id VARCHAR(200),agency_id VARCHAR(200),route_short_name VARCHAR(200),route_long_name VARCHAR(200),route_desc VARCHAR(200) DEFAULT NULL,route_type INT,route_url VARCHAR(200),route_color VARCHAR(200),route_text_color VARCHAR(200),PRIMARY KEY (route_id));
CREATE TABLE skyline.STOP_TIMES (trip_id VARCHAR(200),arrival_time VARCHAR(200),departure_time VARCHAR(200),stop_id VARCHAR(200),stop_sequence INT,stop_headsign VARCHAR(200),pickup_type INT DEFAULT 0,drop_off_type INT DEFAULT 0,shape_dist_traveled DECIMAL(18,15),PRIMARY KEY (trip_id,stop_id,arrival_time));
CREATE TABLE skyline.TRIPS (route_id VARCHAR(200),service_id VARCHAR(200),trip_id VARCHAR(200),trip_headsign VARCHAR(200),trip_short_name VARCHAR(200),direction_id INT,block_id VARCHAR(200) DEFAULT NULL,shape_id VARCHAR(200),wheelchair_accessible INT,PRIMARY KEY (trip_id));
CREATE TABLE skyline.DEMAND (route_id VARCHAR(200),demand DECIMAL(18,2));
CREATE TABLE skyline.USES (stop_id VARCHAR(200),use DECIMAL(18,2));

IMPORT INTO skyline.STOPS FROM LOCAL CSV FILE 'exa exasol/docker-db/STOPS.csv' SKIP = 1 ROW SEPARATOR='CRLF';
IMPORT INTO skyline.ROUTES FROM LOCAL CSV FILE 'exa exasol/docker-db/ROUTES.csv' SKIP = 1;
IMPORT INTO skyline.TRIPS FROM LOCAL CSV FILE 'exa exasol/docker-db/TRIPS.csv' SKIP = 1 ROW SEPARATOR='CRLF';
IMPORT INTO skyline.STOP_TIMES FROM LOCAL CSV FILE 'exa exasol/docker-db/STOP_TIMES.csv' SKIP = 1 ROW SEPARATOR='CRLF';
IMPORT INTO skyline.DEMAND FROM LOCAL CSV FILE 'exa exasol/docker-db/demand1.csv'  ;
IMPORT INTO skyline.USES FROM LOCAL CSV FILE 'exa exasol/docker-db/uses1.csv' ;

create table geonames (
geonameid int,
name varchar(200),
asciiname varchar(200),
alternatenames varchar(10000),
latitude DECIMAL(9,6),
longitude DECIMAL(9,6),
feature_class char(1),
feature_code varchar(10),
country_code char(2),
cc2 varchar(200),
admin1_code varchar(20),
admin2_code varchar(80),
admin3_code varchar(20),
admin4_code varchar(20),
population bigint,
elevation int,
dem int,
timezone varchar(40),
modification_date date
);

IMPORT INTO geonames
    FROM LOCAL CSV FILE 'exa exasol/docker-db/ES.txt'
    ENCODING = 'UTF8'
    COLUMN SEPARATOR = 'TAB'
    COLUMN DELIMITER = 'ESC'
    ERRORS INTO error_table (CURRENT_TIMESTAMP)
    REJECT LIMIT UNLIMITED;

ALTER TABLE geonames ADD COLUMN geo GEOMETRY(4326);
UPDATE geonames SET geo = 'POINT ('||longitude||' '||latitude||')';

CREATE TABLE escuelas AS
       SELECT geo FROM geonames
       WHERE feature_code like 'SCH%';

CREATE TABLE mercados AS
       SELECT geo FROM geonames
       WHERE feature_code like 'MKT';

CREATE TABLE cc AS
       SELECT geo FROM geonames
       WHERE feature_code like 'MALL';

CREATE TABLE hospitales AS
       SELECT geo FROM geonames
       WHERE feature_code like 'HSP%';

create table dSCH as 
select p.stop_id,MIN(ST_DISTANCE('POINT ('||stop_lon||' '||stop_lat||')',s.geo)) dSCH 
FROM trips
  INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id
  INNER JOIN stops p ON p.stop_id = stop_times.stop_id
  INNER JOIN routes on trips.route_id = routes.route_id, escuelas s
group by p.stop_id;

create table dMKT as
select s.stop_id,MIN(ST_DISTANCE('POINT ('||stop_lon||' '||stop_lat||')',m.geo)) dMKT
FROM trips
  INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id
  INNER JOIN stops s ON s.stop_id = stop_times.stop_id
  INNER JOIN routes on trips.route_id = routes.route_id, mercados m
group by s.stop_id;

create table dMALL as
select s.stop_id,MIN(ST_DISTANCE('POINT ('||stop_lon||' '||stop_lat||')',cc.geo)) dMALL
FROM trips
  INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id
  INNER JOIN stops s ON s.stop_id = stop_times.stop_id
  INNER JOIN routes on trips.route_id = routes.route_id, cc
group by s.stop_id;

create table dHSP as
select s.stop_id,MIN(ST_DISTANCE('POINT ('||stop_lon||' '||stop_lat||')',h.geo)) dHSP
FROM trips
  INNER JOIN stop_times ON stop_times.trip_id = trips.trip_id
  INNER JOIN stops s ON s.stop_id = stop_times.stop_id
  INNER JOIN routes on trips.route_id = routes.route_id, hospitales h
group by s.stop_id;

create table skyline.DISTANCES as
select s.stop_id,dSCH, dMKT,dMALL,dHSP
from dSCH s, dMKT m, dHSP h, dMALL cc
where s.stop_id=m.stop_id and m.Stop_id=h.stop_id and h.stop_id=cc.stop_id;

