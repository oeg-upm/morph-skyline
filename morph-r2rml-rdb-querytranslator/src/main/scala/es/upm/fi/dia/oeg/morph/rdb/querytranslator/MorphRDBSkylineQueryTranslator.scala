package es.upm.fi.dia.oeg.morph.rdb.querytranslator

import scala.collection.JavaConversions._
import java.sql.{Connection, ResultSet}
import java.util.regex.Matcher
import java.util.regex.Pattern

import Zql.ZConstant
import Zql.ZExp
import org.apache.jena.graph.{Node, NodeFactory, Triple}
import es.upm.fi.dia.oeg.morph.base._
import es.upm.fi.dia.oeg.morph.r2rml.model.R2RMLMappingDocument
import es.upm.fi.dia.oeg.morph.r2rml.model.R2RMLTriplesMap
import es.upm.fi.dia.oeg.morph.r2rml.model.R2RMLTermMap
import es.upm.fi.dia.oeg.morph.r2rml.model.R2RMLRefObjectMap
import es.upm.fi.dia.oeg.morph.base.querytranslator.NameGenerator
import es.upm.fi.dia.oeg.morph.base.querytranslator.MorphBaseBetaGenerator
import es.upm.fi.dia.oeg.morph.base.querytranslator.MorphBaseCondSQLGenerator
import es.upm.fi.dia.oeg.morph.base.querytranslator.MorphBaseQueryTranslator
import es.upm.fi.dia.oeg.morph.base.querytranslator.MorphBaseAlphaGenerator
import es.upm.fi.dia.oeg.morph.base.querytranslator.MorphBasePRSQLGenerator
import es.upm.fi.dia.oeg.morph.base.model.MorphBaseClassMapping
import es.upm.fi.dia.oeg.morph.base.model.MorphBasePropertyMapping
import es.upm.fi.dia.oeg.morph.base.engine.MorphBaseResultSet
import es.upm.fi.dia.oeg.morph.base.sql.IQuery
import es.upm.fi.dia.oeg.morph.base.engine.MorphBaseUnfolder
import es.upm.fi.dia.oeg.morph.base.querytranslator.engine.MorphMappingInferrer
import es.upm.fi.dia.oeg.morph.r2rml.rdb.engine
import es.upm.fi.dia.oeg.morph.r2rml.rdb.engine.MorphRDBNodeGenerator
import org.apache.jena.datatypes.RDFDatatype
import org.apache.jena.datatypes.xsd.XSDDatatype
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

class MorphRDBSkylineQueryTranslator (nameGenerator:NameGenerator
                              , alphaGenerator:MorphBaseAlphaGenerator, betaGenerator:MorphBaseBetaGenerator
                              , condSQLGenerator:MorphBaseCondSQLGenerator, prSQLGenerator:MorphBasePRSQLGenerator)
  extends MorphRDBQueryTranslator(nameGenerator:NameGenerator
    , alphaGenerator:MorphBaseAlphaGenerator, betaGenerator:MorphBaseBetaGenerator
    , condSQLGenerator:MorphBaseCondSQLGenerator, prSQLGenerator:MorphBasePRSQLGenerator) {

  override val logger = LoggerFactory.getLogger(this.getClass());
   
}