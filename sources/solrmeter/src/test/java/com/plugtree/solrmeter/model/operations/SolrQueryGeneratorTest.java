package com.plugtree.solrmeter.model.operations;



import java.util.List;

import junit.framework.TestCase;

import org.apache.solr.client.solrj.SolrQuery;

import com.plugtree.solrmeter.model.operation.SolrQueryGenerator;

public class SolrQueryGeneratorTest extends TestCase{

	public void testShouldBreakTheStringInKeyValue() throws Exception {
		String queryString = "formatter=html&hl.fragsize=0&q=*:*&h1.requireFieldMatch=true&hl.simple.pre=%3Cem+class%3D%22yellow_highlight%22%3E&hl.fl=content&hl=true&rows=5";
		List<String> params = SolrQueryGenerator.getParamsFrom(queryString);
		assertEquals(8, params.size());
		
		String secondQueryString = "formatter%3Dhtml%26hl.fragsize%3D0%26q%3D*%3A*%26h1.requireFieldMatch%3Dtrue%26hl.simple.pre%3D%253Cem%2Bclass%253D%2522yellow_highlight%2522%253E%26hl.fl%3Dcontent%26hl%3Dtrue%26rows%3D5";
		List<String> params2 = SolrQueryGenerator.getParamsFrom(secondQueryString);
		assertEquals(8, params2.size());
	}
	
	
	public void testShouldRemoveEmptyStringsOrWrongKeyValueFromParameterList() throws Exception {
		String queryString = "qt=/duplicate&&shards.qt=/duplicate";
		List<String> list = SolrQueryGenerator.getParamsFrom(queryString);
		assertEquals(2, list.size());
	}
	
	public void testShouldBreakTheStringUsingASpecifiedChar() throws Exception {
		String string = "formatter=html&hl.fragsize=0&sort=harvestDate desc";
		List<String> values = SolrQueryGenerator.split(string, "&");
		assertEquals(3, values.size());
		
		String string2 = "formatter=html";
		List<String> keyVal = SolrQueryGenerator.getKeyValuePair(string2);
		assertEquals(2, keyVal.size());
	}
	
	public void testShouldCreateASolrQueryStartingFromAString() throws Exception {
		String q= "q=*:*&rows=5";
		SolrQuery query = SolrQueryGenerator.fromString(q);
		SolrQuery expected = new SolrQuery();
		expected.add("q", "*:*");
		expected.add("rows", "5");
		assertEquals(query.toString(), expected.toString());
		
	}
	
	public void testWithLocalParams() {
	  String q = "facet.field={!ex=source}source&facet.field={!ex=source}type&facet.field={!ex=source}status&facet.field={!ex=display}author&qt=/browse&fq={!tag=source}source:1&fq={!tag=display}display:true";
	  SolrQuery query = SolrQueryGenerator.fromString(q);
	  assertEquals("{!ex=source}source", query.getFacetFields()[0]);
	  assertEquals("{!ex=source}type", query.getFacetFields()[1]);
	  assertEquals("{!ex=source}status", query.getFacetFields()[2]);
	  assertEquals("{!ex=display}author", query.getFacetFields()[3]);
	  assertEquals("{!tag=source}source:1", query.getFilterQueries()[0]);
	  assertEquals("{!tag=display}display:true", query.getFilterQueries()[1]);
	 }
	
	
}