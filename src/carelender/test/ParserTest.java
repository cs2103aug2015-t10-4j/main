//@@author A0133269A
package carelender.test;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import carelender.model.data.DateRange;
import carelender.model.data.Event;
import carelender.model.data.EventList;
import carelender.model.data.QueryAdd;
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
            Event e = new Event(i, null, null, null);
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

        //Wrong order and boundary case
        String case4 = "1-3, 15-13";
        indices = extractIndices(case4);
        Integer[] expected4 = {0,1,2,12,13,14};
        assertArrayEquals(expected4, indices);

        //Out of bounds case
        String case5 = "1,13,5-8,20";
        indices = extractIndices(case5);
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

        String resultString =  removeQuotes(userInput);
        String expectedString = "This is string   \"=================\" \"==================";

        assertEquals(expectedString, resultString);
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

    @Test
    public void testAddParsing() {
        //User input
        String userInput = "add \"Morning event\" tomorrow 4pm - 5pm cat hehe";
        QueryBase result = parseCompleteInput(userInput);
        if ( !(result instanceof QueryAdd) ) {
            assertTrue("Not a QueryAdd instance", false);
        }
        //Formatting the user input into a string
        QueryAdd resultAdd = (QueryAdd)result;
        Event event = resultAdd.convertToEventObject();
        String eventString = event.getDetailedInfo();

        //Creating the expected result
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,		  1);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND,	  0);
        cal.set(Calendar.MINUTE,	  0);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        Date startDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 17);
        Date endDate = cal.getTime();
        DateRange [] dateRange = new DateRange[1];
        dateRange[0] = new DateRange(startDate, endDate, true);
        Event expectedEvent = new Event(0, "Morning event", dateRange, "hehe");
        String expected = expectedEvent.getDetailedInfo();
        assertEquals(expected, eventString);

        //User input
        userInput = "add \"TGI Friday's and dinner\" 10 days from now 6.30pm - 8pm , 12 days from now 6pm - 7.30pm cat dinner";
        result = parseCompleteInput(userInput);
        if ( !(result instanceof QueryAdd) ) {
            assertTrue("Not a QueryAdd instance", false);
        }
        //Formatting the user input into a string
        resultAdd = (QueryAdd)result;
        event = resultAdd.convertToEventObject();
        eventString = event.getDetailedInfo();

        //Creating the expected result
        dateRange = new DateRange[2];

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE,		  10);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND,	  0);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE,	  30);
        startDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE,	  0);
        endDate = cal.getTime();

        dateRange[0] = new DateRange(startDate, endDate, true);

        cal.add(Calendar.DATE,		  2);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE,	  0);
        startDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 19);
        cal.set(Calendar.MINUTE,	  30);
        endDate = cal.getTime();

        dateRange[1] = new DateRange(startDate, endDate, true);

        expectedEvent = new Event(0, "TGI Friday's and dinner", dateRange, "dinner");
        expected = expectedEvent.getDetailedInfo();
        assertEquals(expected, eventString);
        System.out.println(expected);
        System.out.println(eventString);

    }

}
