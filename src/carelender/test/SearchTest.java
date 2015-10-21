package carelender.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;

import org.junit.Test;

import carelender.controller.Search;
import carelender.model.data.Event;
import carelender.model.data.QueryList;;

public class SearchTest{

	@Test
	public void testHasMethods() {
		HashMap<QueryList.SearchParam, Object> params = new HashMap<QueryList.SearchParam, Object>();
		
		/* 
		 * This is a boundary case for the partition where the 
		 * parameters list does not contain the desired parameter.
		 */
		params.clear();
		assertFalse(Search.hasDateRange(params));
		/* 
		 * This is a boundary case for the partition where the 
		 * parameters list does contain the desired parameter.
		 */
		params.put(QueryList.SearchParam.DATE_END, new Date());
		params.put(QueryList.SearchParam.DATE_START, new Date());
		assertTrue(Search.hasDateRange(params));
		
		params.clear();
		assertFalse(Search.hasStartDate(params));
		params.put(QueryList.SearchParam.DATE_START, new Date());
		assertTrue(Search.hasStartDate(params));
		
		params.clear();
		assertFalse(Search.hasEndDate(params));
		params.put(QueryList.SearchParam.DATE_END, new Date());
		assertTrue(Search.hasEndDate(params));
		
		params.clear();
		assertFalse(Search.hasNameContains(params));
		params.put(QueryList.SearchParam.NAME_CONTAINS, "name");
		assertTrue(Search.hasNameContains(params));
		
		params.clear();
		assertFalse(Search.hasNameExact(params));
		params.put(QueryList.SearchParam.NAME_EXACT, "match me exactly");
		assertTrue(Search.hasNameExact(params));
	}
	
	@Test
	public void testIsMethods() {
		HashMap<QueryList.SearchParam, Object> params = new HashMap<QueryList.SearchParam, Object>();
		Event event = new Event ( 0, "", null );
		
		event.setName("exact matching");
		/* 
		 * This is a boundary case for the partition where the 
		 * parameters list does contain the desired parameter.
		 */
		assertFalse(Search.isEventNameExact(event, "not exact matching"));
		/* 
		 * This is a boundary case for the partition where the 
		 * parameters list does contain the desired parameter.
		 */
		assertTrue(Search.isEventNameExact(event, "exact matching"));
		
		event.setName("jillian's birthday party");
		assertFalse(Search.isEventNameMatch(event, "birthday bash"));
		assertTrue(Search.isEventNameMatch(event, "birthday party"));
	}
}
