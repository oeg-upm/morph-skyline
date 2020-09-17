package dk.aau.cs.skytpf.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.ProjectionElem;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.QueryParser;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParserFactory;
import dk.aau.cs.skytpf.main.SkylineQueryInput.SkylineMethod;
import dk.aau.cs.skytpf.main.SkylineQueryInput.SkylinePrefFunc;
import dk.aau.cs.skytpf.model.HttpRequestConfig;
import dk.aau.cs.skytpf.model.TriplePattern;

public class gtfs {

  private static List<ProjectionElem> projectionElemList;
  private static ArrayList<TriplePattern> triplePatterns;
  private static ArrayList<String> queries;
  private static ArrayList<SkylineQueryInput> inputs;

  private static String syntheticDataStartingFragment;// = "http://172.19.2.99:6855/";

  public static void main(String[] args)
      throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
    if (args.length != 6) {
      System.err.println("The following inputs are required: distribution, number of bindings, and starting fragment");
      return;
    }
    String dist = args[0];
    int numberOfBindings = Integer.parseInt(args[1]);
    syntheticDataStartingFragment = args[2];
        int query = Integer.parseInt(args[3]);
        Boolean isMultiThreaded = Boolean.parseBoolean(args[4]);
        int skylineMethod = Integer.parseInt(args[5]);
    HttpRequestConfig.MAX_NUMBER_OF_BINDINGS = numberOfBindings;
    HttpRequestConfig.MAX_NUMBER_OF_SKYLINE_BINDINGS = numberOfBindings;
    executeSyntheticDataExperiments(dist, dist + "_" + numberOfBindings + ".csv", query, isMultiThreaded, skylineMethod);
  }

  private static void executeSyntheticDataExperiments(String dist, String outputFileName, int query, Boolean isMultiThreaded, int skylineMethod)
      throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
    PrintWriter outputPW = new PrintWriter(new FileWriter(new File(outputFileName),true));
    //outputPW.println("dist,nd,ne,stmt,stmtnc,stmtnr,stst,ststnc,ststnr" +  ",brtmt,brtmtnc,brtmtnr,brtst,brtstnc,brtstnr,tpfmt,tpfmtnc,tpfmtnr,tpfst,tpfstnc,tpfstnr");
        triplePatterns = new ArrayList<TriplePattern>();
    inputs = new ArrayList<SkylineQueryInput>();
    initializeQuery(query);

    SkylineQueryInput currInput = inputs.get(0);

        System.out.println("Running experiments for " + dist + " distribution");
        String currSF = syntheticDataStartingFragment + dist;
        inputs.get(0).setStartFragment(currSF);
        SkylineQueryProcessor.NUMBER_OF_HTTP_REQUESTS.set(0);
        SkylineQueryProcessor.NUMBER_OF_BINDINGS_SENT.set(0);
        SkylineQueryProcessor.NUMBER_OF_BINDINGS_RECEIVED.set(0);
        SkylineQueryProcessor.NUMBER_OF_SKYLINE_CANDIDATES = 0;
        SkylineQueryProcessor hybridMultiThreadedQP =
            new SkylineQueryProcessor(triplePatterns, projectionElemList, currInput,
                SkylineMethod.values()[skylineMethod], isMultiThreaded, false);
        hybridMultiThreadedQP.processQuery();
        int numberOfSkylinesHMTQP = hybridMultiThreadedQP.getSkylineBindings().size();
        long hmtqpt = hybridMultiThreadedQP.getQueryProcessingTime();
        int hmtnc = SkylineQueryProcessor.NUMBER_OF_SKYLINE_CANDIDATES;
        int hmtnb = SkylineQueryProcessor.NUMBER_OF_BINDINGS_SENT.get()
            + SkylineQueryProcessor.NUMBER_OF_BINDINGS_RECEIVED.get();
        int hmtnr = SkylineQueryProcessor.NUMBER_OF_HTTP_REQUESTS.get();

        outputPW.println(dist + "," + SkylineMethod.values()[skylineMethod] + "," +  isMultiThreaded  + "," + numberOfSkylinesHMTQP + "," + hmtqpt + "," + hmtnc + "," + hmtnr);
        System.out.println(hybridMultiThreadedQP.getSkylineBindings().toString());
    outputPW.close();
  }

  private static void initializeQuery(int i)
      throws IllegalArgumentException, IOException {
        ArrayList<ArrayList<String> > skylineAttrsList = new ArrayList<ArrayList<String> >(5);
        //q1
        ArrayList<String> a1 = new ArrayList<String>();
        a1.add("?demand");
        a1.add("?use");
        skylineAttrsList.add(a1);

        //q2
        a1 = new ArrayList<String>();
        a1.add("?demand");
        a1.add("?use");
	a1.add("?dSCH");
        skylineAttrsList.add(a1);

        //q3
        a1 = new ArrayList<String>();
        a1.add("?demand");
        a1.add("?use");
        a1.add("?dSCH");
	a1.add("?dMKT");
        skylineAttrsList.add(a1);

        //q4
        a1 = new ArrayList<String>();
        a1.add("?demand");
        a1.add("?use");
        a1.add("?dSCH");
	a1.add("?dHSP");
        skylineAttrsList.add(a1);

        //q5
        a1 = new ArrayList<String>();
        a1.add("?demand");
        a1.add("?use");
        a1.add("?dSCH");
        a1.add("?dHSP");
	a1.add("?dMALL");
        skylineAttrsList.add(a1);


        ArrayList<ArrayList<SkylinePrefFunc> > skylinePrefFuncsList = new ArrayList<ArrayList<SkylinePrefFunc> >(5);
        //q1
        ArrayList<SkylinePrefFunc> f1 = new ArrayList<SkylinePrefFunc>();
        f1.add(SkylinePrefFunc.MAX);
        f1.add(SkylinePrefFunc.MIN);
        skylinePrefFuncsList.add(f1);

        //q2
        f1 = new ArrayList<SkylinePrefFunc>();
        f1.add(SkylinePrefFunc.MAX);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        skylinePrefFuncsList.add(f1);

        //q3
        f1 = new ArrayList<SkylinePrefFunc>();
        f1.add(SkylinePrefFunc.MAX);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        skylinePrefFuncsList.add(f1);

        //q4
        f1 = new ArrayList<SkylinePrefFunc>();
        f1.add(SkylinePrefFunc.MAX);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        skylinePrefFuncsList.add(f1);

        //q5
        f1 = new ArrayList<SkylinePrefFunc>();
        f1.add(SkylinePrefFunc.MAX);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        f1.add(SkylinePrefFunc.MIN);
        skylinePrefFuncsList.add(f1);

        String queryFile = "q" + i + ".sparql";
        String queryStringBase = FileUtils.readFileToString(new File(queryFile), StandardCharsets.UTF_8);
        ArrayList<String>  skylineAttrs = skylineAttrsList.get(i);
        /*ArrayList<String> skylineAttrs = new ArrayList<String>();
    skylineAttrs.add("?cost");
    skylineAttrs.add("?size");
    ArrayList<SkylinePrefFunc> skylinePrefFuncs = new ArrayList<SkylinePrefFunc>();
    skylinePrefFuncs.add(SkylinePrefFunc.MIN);
    skylinePrefFuncs.add(SkylinePrefFunc.MAX);
        */
        ArrayList<SkylinePrefFunc> skylinePrefFuncs = skylinePrefFuncsList.get(i);
    initializeQuery(queryFile, skylineAttrs, skylinePrefFuncs);
    SkylineQueryInput input = new SkylineQueryInput();
    input.setSkylineAttributes(skylineAttrs);
    input.setSkylinePreferenceFunctions(skylinePrefFuncs);
    inputs.add(input);
  }

  private static void initializeQuery(String queryFile, ArrayList<String> skylineAttrs,
      ArrayList<SkylinePrefFunc> skylinePrefFuncs) throws IOException, IllegalArgumentException {
    triplePatterns = new ArrayList<TriplePattern>();
    String queryString = FileUtils.readFileToString(new File(queryFile), StandardCharsets.UTF_8);
    for (String skylineAttr : skylineAttrs) {
      if (!queryString.contains(skylineAttr)) {
        throw new IllegalArgumentException(
            "The given query does not contain the skyline attribute " + skylineAttr + ".");
      }
    }
    SPARQLParserFactory factory = new SPARQLParserFactory();
    QueryParser parser = factory.getParser();
    ParsedQuery parsedQuery = parser.parseQuery(queryString, null);
    TupleExpr query = parsedQuery.getTupleExpr();
    if (query instanceof Projection) {
      Projection proj = (Projection) query;
      projectionElemList = proj.getProjectionElemList().getElements();
    } else {
      throw new IllegalArgumentException("The given query should be a select query.");
    }
    List<StatementPattern> statementPatterns = StatementPatternCollector.process(query);
    for (StatementPattern statementPattern : statementPatterns) {
      triplePatterns.add(new TriplePattern(statementPattern, skylineAttrs, skylinePrefFuncs));
    }
        System.out.println("query: " + queryString + " " + skylineAttrs.toString() + " " +  skylinePrefFuncs.toString());
  }
}

