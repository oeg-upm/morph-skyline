package es.upm.fi.dia.oeg.morph.base.parser;

//import es.upm.fi.dia.oeg.morph.base.parser.Parser;
//import es.upm.fi.dia.oeg.morph.base.parser.ParserSPARQL;

import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.apache.jena.sparql.syntax.Template;
import org.apache.jena.util.FileManager;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.lang.ParserARQ ;
import org.apache.jena.sparql.lang.SPARQLParser ;
import org.apache.jena.riot.system.IRIResolver ;
import org.apache.jena.query.Query;
import org.apache.jena.graph.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class QueryFactorySkyline extends QueryFactory{

	// ---- static methods for making a query
	static public QuerySkyline create(String queryString) {
		return create(queryString, Syntax.defaultSyntax);
	}

	static public QuerySkyline create(String queryString, Syntax langURI) {
		return create(queryString, null, langURI);
	}

	static public QuerySkyline create(String queryString, String baseURI) {
		QuerySkyline query = new QuerySkyline();
		parse(query, queryString, baseURI, Syntax.defaultSyntax);
		return query;

	}

	static public QuerySkyline create(String queryString, String baseURI,
			Syntax querySyntax) {
		QuerySkyline query = new QuerySkyline();
		parse(query, queryString, baseURI, querySyntax);
		return query;

	}
	
	static public QuerySkyline create(String queryString, String skylineClause, String baseURI,
			Syntax querySyntax) {
		QuerySkyline skyline = new QuerySkyline();
		Query query = new Query();
		parse(query, queryString, baseURI, querySyntax);
		skyline.setQuery(query);
		if (skylineClause != null) {
			skyline.setQuerySkylineType();
			parse(skyline, skylineClause);
		}
		
		return skyline;
	}	
	
	static public void parse(QuerySkyline skyline, String skylineClause) {
		ArrayList<String> skylineAttributes = new ArrayList<String>();
		ArrayList<QuerySkyline.SkylinePrefFunc> skylinePreferenceFunctions = new ArrayList<QuerySkyline.SkylinePrefFunc>();
		
		String att, pref, prefwocomma;
		
		Query q =  skyline.getQuery();
		// Obtiene las variables del query para verificar si la clausula del Skyline esta ok
		Set<String> vars = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		vars.addAll(q.getResultVars());
		q.getQueryPattern();
		// This will walk through all parts of the query
		ElementWalker.walk(q.getQueryPattern(),
		    // For each element...
		    new ElementVisitorBase() {
		        // ...when it's a block of triples...
		        public void visit(ElementPathBlock el) {
		            // ...go through all the triples...
		            Iterator<TriplePath> triples = el.patternElts();
		            while (triples.hasNext()) {
		                // ...and grab the subject
		            	TriplePath triple = triples.next();
		            	if (triple.isTriple()) {
		            		Node s = triple.getSubject();
		            		Node o = triple.getObject();
		            		if (!s.isURI() && !vars.contains(s.getName()))
		            			vars.add(s.getName());
		            		if (!o.isURI() && !vars.contains(o.getName()))
		            			vars.add(o.getName());
		            	}
		            }
		        }
		    }
		);

		// verificacion lexica y sintactica 
		StringTokenizer st = new StringTokenizer(skylineClause);
		if (st.countTokens() > 2) {
			if (!st.nextToken().toUpperCase().equals("SKYLINE") || !st.nextToken().toUpperCase().equals("OF"))
				throw new UnsupportedOperationException("Lexical error in SKYLINE OF clause");
			else {
				while (st.hasMoreTokens() ) {
					if (st.countTokens() > 1) {
						att = st.nextToken();
						pref = st.nextToken();
						
						// verifica atributo e lo inserta en la lista de atributos del skyline
						if (!vars.contains(att.substring(1)) || att.charAt(0) != '?')
							throw new UnsupportedOperationException("Lexical error in SKYLINE OF clause. " + att + " is an invalid attribute");
						else 
							skylineAttributes.add(att);
						//verifica directiva y la inserta en la lista de directivas del skyline
						if (!pref.toUpperCase().equals("MIN") && !pref.toUpperCase().equals("MAX") && 
							!pref.toUpperCase().equals("MIN,") && !pref.toUpperCase().equals("MAX,"))
							throw new UnsupportedOperationException("Lexical error in SKYLINE OF clause. " + att + " must be followed by MIN or MAX ");
						else {
							prefwocomma = pref;
							if (pref.indexOf(",") != -1)
								prefwocomma = pref.substring(0, pref.length()-1);
							skylinePreferenceFunctions.add(QuerySkyline.SkylinePrefFunc.valueOf(prefwocomma));
						}
						// verifica que venga una coma
						if (pref.indexOf(",") == - 1 && st.countTokens() > 1)
							if (!st.nextToken().equals(","))
								throw new UnsupportedOperationException("Lexical error in SKYLINE OF clause. A comma must follow after each criterion");
						if ( (st.countTokens() == 0 && pref.indexOf(",") != - 1) || st.countTokens() == 1)
								throw new UnsupportedOperationException("Lexical error in SKYLINE OF clause. The token after " + att + " " + pref + " is invalid");
					}
					else 
						throw new UnsupportedOperationException("Lexical error in SKYLINE OF clause. The Skyline clause must be contain <attribute MIN|MAX>[,]");
				}
				// almacena los criterios del Skyline
				skyline.setSkylineAttributes(skylineAttributes);
				skyline.setSkylineFunctions(skylinePreferenceFunctions);
			}
		}
		else 
			throw new UnsupportedOperationException("Lexical error in SKYLINE OF clause") ;
	    
	}
	
	/**
	 * Make a query - no parsing done
	 */
	static public QuerySkyline make() {
		return new QuerySkyline();
	}

	static public QuerySkyline create(QuerySkyline originalQuery) {
		return originalQuery.cloneQuery();
	}

	static public Query parse(Query query, String queryString, String baseURI,
			Syntax syntaxURI) {
	        if ( syntaxURI == null )
	            syntaxURI = query.getSyntax() ;
	        else
	            query.setSyntax(syntaxURI) ;

	        SPARQLParser parser = SPARQLParser.createParser(syntaxURI) ;
	        
	        if ( parser == null )
	            throw new UnsupportedOperationException("Unrecognized syntax for parsing: "+syntaxURI) ;
	        
	        if ( query.getResolver() == null )
	        {
	            IRIResolver resolver = null ;
	            try { 
	                if ( baseURI != null ) { 
	                    // Sort out the baseURI - if that fails, dump in a dummy one and continue.
	                    resolver = IRIResolver.create(baseURI) ; 
	                }
	                else { 
	                    resolver = IRIResolver.create() ;
	                }
	            }
	            catch (Exception ex) {}
	            if ( resolver == null )   
	                resolver = IRIResolver.create("http://localhost/query/defaultBase#") ;
	            query.setResolver(resolver) ;
	            
	        }
	        return parser.parse(query, queryString) ;
	    
	}

	static boolean knownParserSyntax(Syntax syntaxURI) {
		SPARQLParser parser = SPARQLParser.createParser(syntaxURI);
		return (parser != null);
	}

	static public QuerySkyline read(String url) {
		return read(url, null, null, null);
	}

	static public QuerySkyline read(String url, String baseURI) {
		return read(url, null, baseURI, null);
	}

	static public QuerySkyline read(String url, String baseURI, Syntax langURI) {
		return read(url, null, baseURI, langURI);
	}

	static public QuerySkyline read(String url, FileManager filemanager,
			String baseURI, Syntax langURI) {
		String new_lines, skyline_line = null;
		if (filemanager == null)
			filemanager = FileManager.get();
		String qStr = filemanager.readWholeFileAsUTF8(url);
		
		// Codigo nuevo
		int indexSkyline = qStr.toUpperCase().indexOf("skyline of".toUpperCase());
		new_lines =  qStr;
        if (indexSkyline > - 1) {
        	new_lines = qStr.substring(0, indexSkyline);
        	skyline_line = qStr.substring(indexSkyline);
        }
        
		if (baseURI == null)
			baseURI = url;
		if (langURI == null)
			langURI = Syntax.guessQueryFileSyntax(url);
		return create(new_lines, skyline_line, baseURI, langURI);
		//return create(qStr, baseURI, langURI);
	}

	static public Element createElement(String elementString) {
		return ParserARQ.parseElement(elementString);
	}

	static public Template createTemplate(String templateString) {
		return ParserARQ.parseTemplate(templateString);
	}
}