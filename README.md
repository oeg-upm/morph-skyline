# Morph-Skyline

Morph-Skyline is an RDB2RDF engine for skyline queries developed by the Ontology Engineering Group, that follows the R2RML specification (http://www.w3.org/TR/r2rml/). 

Skyline queries are preference-based queries, which were proposed by Börzsönyi et al. [1] extending SQL by means of a SKYLINE OF clause as follows: SKYLINE OF d1 [MIN | MAX],..., dn [MIN | MAX] where where di denote the dimensions of the Skyline, and MIN and MAX specify whether the value in that dimension should be minimised or maximised.

Morph-Skyline extends Morph-RDB and it supports two operational modes: data upgrade (generating RDF instances from data in a relational database) and skyline query translation (SPARQL to SQL). Morph-Skyline has been tested on synthetic datasets using the data generator provided by Börzsönyi et al.[1]. At the moment, Morph-Skyline works with EXASol.

This repository contains code and experiments for the ISWC'20 paper "Morph-Skyline: A Virtual Ontology-Based Data Access Approach for Skyline Queries". Experiments can be found in the Experiments directory of this repository.

User guides:
As Morph-Skyline extends Morph-RDB, for those who want to use this project on an user level, you can find a little guide for Morph-RDB to on the main branch wiki : https://github.com/oeg-upm/morph-rdb/wiki.

If, on the other hand, you want to edit the project or at least work from an IDE such as Eclipse, we suggest you to follow this steps:
 - Download the source code.
 - Once unziped, you may notice that the imports doesn´t match the actual directories. In order to avoid changing all the imports or all the directories, import this way: Import -> Maven -> Existing Maven Project, and select as root the folder where you unziped the project (it may take a few minutes).
 - Now that it´s finally imported, you can run the file es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBRunner.scala (in other to pass the arguments in Eclipse, right click, Run As -> Run Configuration -> Arguments, and remember to imput both the path to the .properties file and it´s full name).
* In case the program doesn´t find the file log4j.properties, move it from "morph-examples" to "morph-r2rml-rdb", thought this file isn´t essential.

<!--- Acknowledgement: Since January 2020, the development of morph-Skyline has been supported by the SPRINT project (http://sprint-transport.eu/).--->

References:
[1] Borzsonyi, Stephan; Kossmann, Donald; Stocker, Konrad (2001). "The Skyline Operator". Proceedings 17th International Conference on Data Engineering: 421–430. doi:10.1109/ICDE.2001.914855.
