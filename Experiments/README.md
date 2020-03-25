# Morph-Skyline

If you want to run all the experiments in the paper, you should have morph-skyline.jar file and the directory dependency in your target directory. 

Firstly, you have to start the Morph-Skyline and EXASol engines: docker-compose up -d

Then, you need to download the datasets, mappings, queries and properties from https://delicias.dia.fi.upm.es/nextcloud/index.php/s/byCEe5eoNDKiDo5. Uncompress each file within the datasets directory and execute the scripts to load the data in EXASol. For example, you can load 1M dataset by means of: docker exec exasoldb exaplus -c localhost:8888 -u sys -P exasol -f "exa exasol/docker-db/script1m.sql"

In order to run the experiments, you should execute run.sh with the dataset size as parameter: 10k,100k,1m,10m or 100m.

If you want to use your own data, modify accordingly the definition of the morph-skyline container in the docker-compose.yml file. 