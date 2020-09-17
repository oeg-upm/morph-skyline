# How to load the data?

- Install docker (with user permissions), docker-compose (with user permissions), unzip, wget.
- Download the datasets from https://upm365-my.sharepoint.com/:f:/g/personal/marlene_gdasilva_upm_es/El1puQtLzlBFoy-BlG5GdOoBT9yhgQO5LPGaOp1Hc5_GbA?e=GSe0uK. The file GTFS-1 contains the original data and the other files (GTFS-X where X is 5, 10, 50, 100, 1000) correspond to the original dataset scaled by VIG.
- For each file, decompress the files with tar -xzvf file.tar.gz and then copy the decompressed files into the directory exa_volume where Morph-Skyline is installed
- Run: 
```bash 
docker exec exasoldb exaplus -c localhost:8563 -u sys -P exasol -f "exa exasol/docker-db/script.sql" 
```
for loading data from GTFS-1 or 
```bash 
docker exec exasoldb exaplus -c localhost:8563 -u sys -P exasol -f "exa exasol/docker-db/script-scale.sql" for loading data scaled by VIG.
```