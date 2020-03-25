cd ..
cd ..
copy morph-rdb-dist\target\morph-rdb-dist-3.12.6.jar morph-examples\morph-rdb.jar
cd morph-examples
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query01-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query01-skyline-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query02-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query02-skyline-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query03-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query03-skyline-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query04-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query04-skyline-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query05-exasol.morph.properties
java -cp morph-rdb.jar;dependency/*;lib/* es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner skyline-examples dataset1-query05-skyline-exasol.morph.properties
cd skyline-examples