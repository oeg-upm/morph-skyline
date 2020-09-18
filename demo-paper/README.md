# A simple Morph-Skyline Demo
This demo consists of 5 queries, over the the GTFS feed of the city of Madrid metro system (https://github.com/oeg-upm/gtfs-bench) and geonames (https://www.geonames.org/export/codes.html), using Morhp-Skyline and the version the multi-threaded version of the TPF, brTPF and skyTPF (https://github.com/ilkcan/skytpf).

##How to deploy
Firstly, copy the mapping file, the queries and the properties files from the corresponding directories to the demo-paper directory

Then, start Morph-Skyline:

```bash 
$ docker-compose up -d 
```

Subsequently, download data from https://upm365-my.sharepoint.com/:f:/g/personal/marlene_gdasilva_upm_es/El1puQtLzlBFoy-BlG5GdOoBT9yhgQO5LPGaOp1Hc5_GbA?e=GSe0uK, and load the data into the database using our scripts in the data directory.

You can now execute run.sh to perform our queries.