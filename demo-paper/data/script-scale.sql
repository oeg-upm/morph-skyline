DROP SCHEMA IF EXISTS skyline CASCADE;
CREATE SCHEMA IF NOT EXISTS skyline;

CREATE TABLE skyline.STOPS (stop_id VARCHAR(200),stop_code VARCHAR(200),stop_name VARCHAR(200),stop_desc VARCHAR(200),stop_lat DECIMAL(18,5),stop_lon DECIMAL(18,5),zone_id VARCHAR(200),stop_url VARCHAR(200),location_type INT,parent_station VARCHAR(200),stop_timezone VARCHAR(200) DEFAULT NULL,wheelchair_boarding INT,PRIMARY KEY (stop_id));
CREATE TABLE skyline.ROUTES (route_id VARCHAR(200),agency_id VARCHAR(200),route_short_name VARCHAR(200),route_long_name VARCHAR(200),route_desc VARCHAR(200) DEFAULT NULL,route_type INT,route_url VARCHAR(200),route_color VARCHAR(200),route_text_color VARCHAR(200),PRIMARY KEY (route_id));
CREATE TABLE skyline.STOP_TIMES (trip_id VARCHAR(200),arrival_time VARCHAR(200),departure_time VARCHAR(200),stop_id VARCHAR(200),stop_sequence INT,stop_headsign VARCHAR(200),pickup_type INT DEFAULT 0,drop_off_type INT DEFAULT 0,shape_dist_traveled DECIMAL(18,5),PRIMARY KEY (trip_id,stop_id,arrival_time));
CREATE TABLE skyline.TRIPS (route_id VARCHAR(200),service_id VARCHAR(200),trip_id VARCHAR(200),trip_headsign VARCHAR(200),trip_short_name VARCHAR(200),direction_id INT,block_id VARCHAR(200) DEFAULT NULL,shape_id VARCHAR(200),wheelchair_accessible INT,PRIMARY KEY (trip_id));
CREATE TABLE skyline.DEMAND (route_id VARCHAR(200),demand DECIMAL(18,2));
CREATE TABLE skyline.USES (stop_id VARCHAR(200),use DECIMAL(18,2));
CREATE TABLE skyline.DISTANCES (stop_id VARCHAR(200),dSCH DECIMAL(18,2),dMKT DECIMAL(18,2),dMALL DECIMAL(18,2),dHSP DECIMAL(18,2));

IMPORT INTO skyline.STOPS FROM LOCAL CSV FILE 'exa exasol/docker-db/STOPS.csv' SKIP = 1;
IMPORT INTO skyline.ROUTES FROM LOCAL CSV FILE 'exa exasol/docker-db/ROUTES.csv' SKIP = 1;
IMPORT INTO skyline.TRIPS FROM LOCAL CSV FILE 'exa exasol/docker-db/TRIPS.csv' SKIP = 1;
IMPORT INTO skyline.STOP_TIMES FROM LOCAL CSV FILE 'exa exasol/docker-db/STOP_TIMES.csv' SKIP = 1;
IMPORT INTO skyline.DEMAND FROM LOCAL CSV FILE 'exa exasol/docker-db/DEMAND.csv'  SKIP = 1;
IMPORT INTO skyline.USES FROM LOCAL CSV FILE 'exa exasol/docker-db/USES.csv' SKIP = 1;
IMPORT INTO skyline.DISTANCES FROM LOCAL CSV FILE 'exa exasol/docker-db/DISTANCES.csv' SKIP = 1;