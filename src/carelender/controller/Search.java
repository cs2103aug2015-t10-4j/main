package carelender.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import carelender.model.Model;
import carelender.model.data.*;

/**
 * Handles all search and sorting queries. Not just by the user, but also by the program.
 */

public class Search {
	public static boolean eventMatchesParams (EventObject eventToCheck,
										HashMap<QueryList.SearchParam, Object> paramsList) {
		boolean match = false;
		
		if (hasNameExact(paramsList)) {
			Object name = paramsList.get(QueryList.SearchParam.NAME_EXACT);
			assert ( name != null ) : "NAME_EXACT paired with null object in HashMap.";
			if (name instanceof String) {
				match = isEventNameExact(eventToCheck, (String)name);
			}
		}
		
		if (hasNameContains(paramsList)) {
			Object name = paramsList.get(QueryList.SearchParam.NAME_CONTAINS);
			assert ( name != null ) : "NAME_CONTAINS paired with null object in HashMap.";
			if (name instanceof String) {
				match = isEventNameMatch(eventToCheck, (String)name);
			}
		}
		
		if (hasDateRange(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
			assert ( startDate != null && endDate != null ) : "DATE_START or DATE_END paired with null object in HashMap.";
			if (startDate instanceof Date && endDate instanceof Date) {
				match = isEventInDateRange(eventToCheck, 
												(Date)startDate, (Date)endDate);
			}
		} else if (hasStartDate(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			assert ( startDate != null ) : "DATE_START paired with null object in HashMap.";
			if (startDate instanceof Date) {
				match = isEventAfterDate(eventToCheck, (Date)startDate);
			}
		} else if (hasEndDate(paramsList)) {
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
			assert ( endDate != null ) : "DATE_END paired with null object in HashMap.";
			if (endDate instanceof Date) {
				match = isEventBeforeDate(eventToCheck, (Date)endDate);
			}
		}
		return match;
	}
	
	public static boolean hasNameExact (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.NAME_EXACT);
	}
	
	public static boolean hasNameContains (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.NAME_CONTAINS);
	}
	
	public static boolean hasStartDate (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.DATE_START);
	}
	
	public static boolean hasEndDate (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.DATE_END);
	}
	
	public static boolean hasDateRange (HashMap<QueryList.SearchParam, Object> paramsList) {
		return (hasStartDate(paramsList) && hasEndDate(paramsList));
	}
	
	public static boolean isEventNameMatch (EventObject eventToCheck,
										String stringToCheck) {
		String eventName = eventToCheck.getName();
		if (eventName != null) {
			if(eventName.contains(stringToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEventNameExact (EventObject eventToCheck,
										String stringToCheck) {
		String eventName = eventToCheck.getName();
		if (eventName != null) {
			if (eventName.equals(stringToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEventBeforeDate (EventObject eventToCheck,
										Date startDate) {
		Date latestDate = eventToCheck.getLatestDate();
		if (latestDate != null) {
			System.out.println ( latestDate.getTime() + "     " + startDate.getTime());
			if (!latestDate.after(startDate)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEventAfterDate (EventObject eventToCheck,
										Date endDate) {
		Date earliestDate = eventToCheck.getEarliestDate();
		if (earliestDate != null) {
			System.out.println ( earliestDate.getTime() + "     " + endDate.getTime());
			if (!earliestDate.before(endDate)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEventInDateRange (EventObject eventToCheck,
										Date startDate, Date endDate) {
		Date earliestDate = eventToCheck.getEarliestDate();
		Date latestDate = eventToCheck.getLatestDate();
		
		if (earliestDate != null && latestDate != null) {
			
			System.out.println ( earliestDate.getTime() + "     " + startDate.getTime());
			System.out.println ( latestDate.getTime() + "     " + endDate.getTime());
			if (!earliestDate.before(startDate) && !latestDate.after(endDate)) {
				return true;
			}
		}
		return false;
	}
}
