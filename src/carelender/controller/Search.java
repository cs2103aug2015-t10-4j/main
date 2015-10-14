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
	
	public Search () {
	}
	
	public EventList parseQuery (QueryBase query) {
		EventList returnList = new EventList();
		switch (query.getQueryType()) {
			case ADD:
				break;
			case CLEAR:
				break;
			case DELETE:
				
				QueryDelete queryDelete = (QueryDelete)query;
				
				if (Model.getInstance().retrieveEvent() != null) {
					for (EventObject event : Model.getInstance().retrieveEvent()) {
						if (this.isEventNameExact(event, queryDelete.getName())) {
							returnList.add(event);
						}
					}
				}
				
				break;
			case UPDATE:
				
				QueryList queryUpdate = (QueryList)query;
				
				//TODO: Replace the null parameter in retrieveEvent to something that makes sense.
				if (Model.getInstance().retrieveEvent() != null) {
					for (EventObject event : Model.getInstance().retrieveEvent()) {
						if (this.eventMatchesParams(event, queryUpdate.getSearchParamsList())) {
							returnList.add(event);
						}
					}
				}
				
				break;
			case EDIT:
				break;
			case ERROR:
				break;
			case HELP:
				break;
			case LIST:
				
				QueryList queryList = (QueryList)query;
				
				//TODO: Replace the null parameter in retrieveEvent to something that makes sense.
				if (Model.getInstance().retrieveEvent() != null) {
					for (EventObject event : Model.getInstance().retrieveEvent()) {
						if (this.eventMatchesParams(event, queryList.getSearchParamsList())) {
							returnList.add(event);
						}
					}
				}
				
				break;
			default:
				break;
		}
		return returnList;
	}
	
	private boolean eventMatchesParams (EventObject eventToCheck,
										HashMap<QueryList.SearchParam, Object> paramsList) {
		boolean match = false;
		
		if (this.hasNameExact(paramsList)) {
			Object name = paramsList.get(QueryList.SearchParam.NAME_EXACT);
			assert ( name != null ) : "NAME_EXACT paired with null object in HashMap.";
			if (name instanceof String) {
				match = this.isEventNameExact(eventToCheck, (String)name);
			}
		}
		
		if (this.hasNameContains(paramsList)) {
			Object name = paramsList.get(QueryList.SearchParam.NAME_CONTAINS);
			assert ( name != null ) : "NAME_CONTAINS paired with null object in HashMap.";
			if (name instanceof String) {
				match = this.isEventNameMatch(eventToCheck, (String)name);
			}
		}
		
		if (this.hasDateRange(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
			assert ( startDate != null && endDate != null ) : "DATE_START or DATE_END paired with null object in HashMap.";
			if (startDate instanceof Date && endDate instanceof Date) {
				match = this.isEventInDateRange(eventToCheck, 
												(Date)startDate, (Date)endDate);
			}
		} else if (this.hasStartDate(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			assert ( startDate != null ) : "DATE_START paired with null object in HashMap.";
			if (startDate instanceof Date) {
				match = this.isEventAfterDate(eventToCheck, (Date)startDate);
			}
		} else if (this.hasEndDate(paramsList)) {
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
			assert ( endDate != null ) : "DATE_END paired with null object in HashMap.";
			if (endDate instanceof Date) {
				match = this.isEventBeforeDate(eventToCheck, (Date)endDate);
			}
		}
		return match;
	}
	
	private boolean hasNameExact (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.NAME_EXACT);
	}
	
	private boolean hasNameContains (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.NAME_CONTAINS);
	}
	
	private boolean hasStartDate (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.DATE_START);
	}
	
	private boolean hasEndDate (HashMap<QueryList.SearchParam, Object> paramsList) {
		return paramsList.containsKey(QueryList.SearchParam.DATE_END);
	}
	
	private boolean hasDateRange (HashMap<QueryList.SearchParam, Object> paramsList) {
		return (this.hasStartDate(paramsList) && this.hasEndDate(paramsList));
	}
	
	private boolean isEventNameMatch (EventObject eventToCheck,
										String stringToCheck) {
		String eventName = eventToCheck.getName();
		if (eventName != null) {
			if(eventName.contains(stringToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isEventNameExact (EventObject eventToCheck,
										String stringToCheck) {
		String eventName = eventToCheck.getName();
		if (eventName != null) {
			if (eventName.equals(stringToCheck)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isEventBeforeDate (EventObject eventToCheck,
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
	
	private boolean isEventAfterDate (EventObject eventToCheck,
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
	
	private boolean isEventInDateRange (EventObject eventToCheck,
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
