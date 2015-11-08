package carelender.controller;

import java.util.Date;
import java.util.HashMap;

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
			
			if ( !match ) {
				return false;
			}
		}
		
		if (hasNameContains(paramsList)) {
			Object name = paramsList.get(QueryList.SearchParam.NAME_CONTAINS);
			assert ( name != null ) : "NAME_CONTAINS paired with null object in HashMap.";
			if (name instanceof String) {
				match = isEventNameMatch(eventToCheck, (String)name);
			}

			if ( !match ) {
				return false;
			}
		}
		
		if (hasCategory(paramsList)) {
			Object category = paramsList.get(QueryList.SearchParam.CATEGORY);
			assert ( category != null ) : "CATEGORY paired with null object in HashMap.";
			if (category instanceof String) {
				match = isEventInCategory(eventToCheck, (String)category);
			}
			
			if ( !match ) {
				return false;
			}
		}
		
		if (hasComplete(paramsList)) {
			Object complete = paramsList.get(QueryList.SearchParam.COMPLETE);
			assert ( complete != null ) : "CATEGORY paired with null object in HashMap.";
			match = IsEventComplete(eventToCheck);
			
			if ( !match ) {
				return false;
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
			
			if ( !match ) {
				return false;
			}
		} else if (hasStartDate(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			if (startDate instanceof Date) {
				match = isEventAfterDate(eventToCheck, (Date)startDate);
			}
			
			if ( !match ) {
				return false;
			}
		} else if (hasEndDate(paramsList)) {
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
			if (endDate instanceof Date) {
				match = isEventBeforeDate(eventToCheck, (Date)endDate);
			}
			
			if ( !match ) {
				return false;
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
	
	public static boolean hasCategory (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.CATEGORY);
	}
	
	public static boolean hasComplete (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.COMPLETE);
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
	
	public static boolean isEventInCategory (Event eventToCheck,
												String stringToCheck) {
		String categoryName = eventToCheck.getCategory();
		if (categoryName != null) {
			if (categoryName.equals(stringToCheck)) {
				return true;
			}
		} else {
			if (stringToCheck == null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean IsEventComplete (Event eventToCheck) {
		return eventToCheck.getCompleted();
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
