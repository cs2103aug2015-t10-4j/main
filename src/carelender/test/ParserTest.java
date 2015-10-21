package carelender.test;

import org.junit.Test;

import carelender.model.data.Event;
import carelender.model.data.EventList;
import carelender.model.data.QueryBase;
import carelender.model.data.QueryGeneric;
import carelender.model.data.QueryType;
import carelender.view.parser.InputParser;
import static org.junit.Assert.*;

public class ParserTest extends InputParser {
	
	/**
	 * Tests the index parsing used for the functions used to detect ranges.
	 */
	@Test
	public void testIndexParsing() {
		//This part is required for the indices to work
		EventList events = new EventList();
		for ( int i = 0 ; i < 15; i++ ) {
			Event e = new Event(i, null, null);
			events.add(e);
		}
		setDisplayedList(events);
		
		//Simple case
		String case1 = "1,2,3";
		Integer[] indices = extractIndices(case1);
		Integer[] expected1 = {0,1,2};
		assertArrayEquals(expected1, indices);
		
		//Case with range
		String case2 = "1,2,3-10";
		indices = extractIndices(case2);
		Integer[] expected2 = {0,1,2,3,4,5,6,7,8,9};
		assertArrayEquals(expected2, indices);
		
		//Unsorted and with spaces
		String case3 = "1, 13, 5-8";
		indices = extractIndices(case3);
		Integer[] expected3 = {0,4,5,6,7,12};
		assertArrayEquals(expected3, indices);
		
		//Out of bounds case
		String case4 = "1,13,5-8,20";
		indices = extractIndices(case4);
		assertArrayEquals(null, indices);
	
	}
	
	@Test
	public void testHelperFunctions() {
		
		String [] originalEmptyEntry = { "Hello", "", "I'm not", "empty" };
		String [] resultEmptyEntry = removeEmptyEntries(originalEmptyEntry);
		String [] expectedEmptyEntry = { "Hello", "I'm not", "empty" };
		
		assertArrayEquals(expectedEmptyEntry, resultEmptyEntry);
		
		String userInput = "This is string   \"Literals and such\" \"andr andomSpa ces ";
		String [] resultSplit = splitQuery(userInput);
		String [] expectedSplit = { "This", "is", "string", "Literals and such", "andr andomSpa ces" };
		
		assertArrayEquals(expectedSplit, resultSplit);
	}
	

	@Test
	public void testCommands() {
		String userInput = "exit";
		QueryBase result = parseCompleteInput(userInput);
		QueryBase expected = new QueryGeneric(QueryType.EXIT);
		
		assertEquals(result.getQueryType(), expected.getQueryType());
		
		userInput = "switch";
		result = parseCompleteInput(userInput);
		expected = new QueryGeneric(QueryType.SWITCHUI);
		
		assertEquals(result.getQueryType(), expected.getQueryType());
	}
}
