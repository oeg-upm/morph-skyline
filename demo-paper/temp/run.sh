#!/bin/bash

for q in 1 2 3 4 5
do
	for t in 1 2 3 4 5 
	do		
		echo "query $q  run $t"
		# Run engine 
		start=$(date +%s.%N)
		timeout -s SIGKILL 60m docker exec morphskyline java -cp dependency/*:.:morph-rdb.jar es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner . query$q-skyline-exasol.morph.properties
		#finish time
		finish=$(date +%s.%N)
		#duration
		dur=$(echo "$finish - $start" | bc)
		echo "$q, skyQT, $t, $dur">>results/results-times$1.csv

		# restart database
		echo "Restart data base..."
		docker-compose restart exasoldb
		sleep 1m

	done
	docker exec morphskyline sh contar_skyline.sh query$q-skyline-result-exasol.xml $1 $i $q >>cardinality$1.csv
	#docker exec morphskyline mv query$q-skyline-result-exasol.xml results/S/${i}/${1}
done
