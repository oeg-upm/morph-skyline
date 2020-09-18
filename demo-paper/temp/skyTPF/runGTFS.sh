#!/bin/bash

cd classes
for q in 0 1 2 3 4 
do
	for t in 1 2 3 4 5
    do
		echo "query $q run $t"
        # Run engine
        start=$(date +%s.%N)
		timeout -s SIGKILL 60m java -XX:-UseGCOverheadLimit -cp .:../skytpf-experiments-executor.jar dk.aau.cs.skytpf.main.gtfs GTFS 30 http://127.0.0.1:6855/ $q true 0 > log_q$q-MT-TPF.txt  2>&1
        #finish time
        finish=$(date +%s.%N)
        #duration
        dur=$(echo "$finish - $start" | bc)
        echo "$1, $((q + 1)),  TPF-MT, $t, $dur">>results-times$1.csv
	
		start=$(date +%s.%N)
		timeout -s SIGKILL 60m java -XX:-UseGCOverheadLimit -cp .:../skytpf-experiments-executor.jar dk.aau.cs.skytpf.main.gtfs GTFS 30 http://127.0.0.1:6855/ $q true 1 > log_q$q-MT-br.txt  2>&1
        #finish time
        finish=$(date +%s.%N)
        #duration
        dur=$(echo "$finish - $start" | bc)
        echo "$1, $((q + 1)), brTPF-MT, $t, $dur">>results-times$1.csv
	
        start=$(date +%s.%N)
        timeout -s SIGKILL 60m java -XX:-UseGCOverheadLimit -cp .:../skytpf-experiments-executor.jar dk.aau.cs.skytpf.main.gtfs GTFS 30 http://127.0.0.1:6855/ $q true 2 > log_q$q-MT-skyTPF.txt  2>&1
        #finish time
        finish=$(date +%s.%N)
        #duration
        dur=$(echo "$finish - $start" | bc)
        echo "$1, $((q + 1)), SkyTPF-MT, $t, $dur">>results-times$1.csv

	done
done

cd ..
