//@@author A0125566B
package carelender.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import carelender.model.strings.DateFormats;

public class DateFormatsTest {

	@Test
	public void testFormatKey() {
		Calendar calendar;
		Date date;
		String key;
		
		assertEquals("", DateFormats.FORMAT_KEY(null));
		
		/*
		 * Test prepending 2 0s behind the day of the year(1).
		 * 
		 * This is a boundary case for the partition where the 
		 * date provided is the smallest day of the year.
		 *
		 */
		calendar = Calendar.getInstance();
		calendar.set(2013, Calendar.JANUARY, 1, 0, 0, 0);
		date = calendar.getTime();
		
		key = "2013 001 1 Tue";
		
		assertEquals(key, DateFormats.FORMAT_KEY(date));
		
		/*
		 * Test prepending 1 0 behind the day of the year(26).
		 */
		calendar = Calendar.getInstance();
		calendar.set(2013, Calendar.JANUARY, 26, 0, 0, 0);
		date = calendar.getTime();
		
		key = "2013 026 26 Sat";
		
		assertEquals(key, DateFormats.FORMAT_KEY(date));
		
		/*
		 * Test prepending no 0s behind the day of the year(326).
		 * 
		 * This is a boundary case for the partition where the 
		 * date provided is a 3-digit day of the year.
		 *
		 */
		calendar = Calendar.getInstance();
		calendar.set(2013, Calendar.NOVEMBER, 22, 0, 0, 0);
		date = calendar.getTime();
		
		key = "2013 326 22 Fri";
		
		assertEquals(key, DateFormats.FORMAT_KEY(date));
	}
}
