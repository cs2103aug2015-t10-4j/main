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
	public static boolean eventMatchesParams (Event eventToCheck,
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
			if ( startDate == null && endDate == null ) {
				match = isEventFloating(eventToCheck);
			} else if (startDate instanceof Date && endDate instanceof Date) {
				match = isEventInDateRange(eventToCheck, 
												(Date)startDate, (Date)endDate);
			}
		} else if (hasStartDate(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			if (startDate instanceof Date) {
				match = isEventAfterDate(eventToCheck, (Date)startDate);
			}
		} else if (hasEndDate(paramsList)) {
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
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
	
	public static boolean isEventNameMatch (Event eventToCheck,
										String stringToCheck) {
		String eventName = eventToCheck.getName();
		if (eventName != null) {
			if(eventName.contains(stringToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEventNameExact (Event eventToCheck,
										String stringToCheck) {
		String eventName = eventToCheck.getName();
		if (eventName != null) {
			if (eventName.equals(stringToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEventBeforeDate (Event eventToCheck,
										Date startDate) {
		if ( eventToCheck != null ) {
			if ( eventToCheck.getDateRange() == null ) {
				if ( startDate == null ) {
					return true;
				}
			}
			Date latestDate = eventToCheck.getLatestDate();
			if (latestDate != null) {
				if (!latestDate.after(startDate)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isEventAfterDate (Event eventToCheck,
										Date endDate) {
		if ( eventToCheck != null ) {
			if ( eventToCheck.getDateRange() == null ) {
				if ( endDate == null ) {
					return true;
				}
			}
			Date earliestDate = eventToCheck.getEarliestDateFromNow();
			if (earliestDate != null) {
				if (!earliestDate.before(endDate)) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean isEventFloating ( Event eventToCheck ) {
		if ( eventToCheck != null ) {
			if (eventToCheck.getDateRange() == null) {
				return true;
			}
		}
		return false;
	}
	public static boolean isEventInDateRange (Event eventToCheck,
										Date startDate, Date endDate) {
		if ( eventToCheck != null ) {
			if ( eventToCheck.getDateRange() != null ) {
				for (DateRange dateRange : eventToCheck.getDateRange()) {
					if ((!dateRange.getStart().before(startDate) && !dateRange.getStart().after(endDate))
							|| (!dateRange.getEnd().before(startDate) && !dateRange.getEnd().after(endDate))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isDateInEvent (Event eventToCheck, Date date) {
		if ( eventToCheck != null ) {
			for ( DateRange dateRange : eventToCheck.getDateRange() ) {
				if ((date.before(dateRange.getEnd()) || date.equals(dateRange.getEnd()))
					&& (date.after(dateRange.getStart()) || date.equals(dateRange.getStart()))) {
					return true;
				}
			}
		}
		return false;
	}
}
