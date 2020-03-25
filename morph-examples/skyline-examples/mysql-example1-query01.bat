cd ..
cd ..
copy morph-rdb-dist\target\morph-rdb-dist-3.12.6.jar morph-examples\morph-rdb.jar
cd morph-examples
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples example1-query01-mysql.morph.properties
cd skyline-examples
