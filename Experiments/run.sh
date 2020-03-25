#!/bin/bash

#Parameter 1 refers to db size

for i in "i" "c" "a"
do
	for q in 1 2 3 4 5 6 7 8 9 
	do
		echo "Loading mappings"
		docker exec morphskyline cp mappings/mapping-exasol-${i}${1}.ttl mapping-exasol.ttl 
		for t in 1 2 3 4 5 
		do		
			echo "size $1 db $i  query $q  run $t"
			# Run engine 
			start=$(date +%s.%N)
			docker exec morphskyline java -cp dependency/*:.:morph-rdb.jar es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner . query$q-skyline-exasol.morph.properties
			#finish time
			finish=$(date +%s.%N)
			#duration
			dur=$(echo "$finish - $start" | bc)
			echo "$1, $i, $q, S, $t, $dur">>results/results-times$1.csv

			# restart database
			echo "Restart data base..."
			docker exec morphskyline mv query$q-skyline-result-exasol.xml results/S/${i}/${1}
			docker-compose restart exasoldb
			sleep 4m

			# Run engine 
			start=$(date +%s.%N)
			timeout -s SIGKILL 30m docker exec morphskyline java -cp dependency/*:.:morph-rdb.jar es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner . query$q-exasol.morph.properties
			#finish time
			finish=$(date +%s.%N)
			#duration
			dur=$(echo "$finish - $start" | bc)
			echo "$1, $i, $q, R,  $t, $dur">>results/results-times$1.csv
			
			# restart database
			echo "Restart data base..."
			docker-compose restart exasoldb
			sleep 4m
		done
		docker exec morphskyline sh contar_skyline.sh query$q-skyline-result-exasol.xml $1 $i $q >>cardinality$1.csv
	done
done
