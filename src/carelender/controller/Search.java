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
				break;
			case DUMMY:
				break;
			case EDIT:
				break;
			case ERROR:
				break;
			case HELP:
				break;
			case LIST:
				
				//TODO: Replace the null param in retrieveEvent to something that makes sense.
				if (Model.retrieveEvent(null) != null) {
					for (EventObject event : Model.retrieveEvent(null)) {
						if (this.eventMatchesParams(event, ((QueryList) query).getParamsList())) {
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
			
			if (name instanceof String) {
				match = this.isEventNameExact(eventToCheck, (String)name);
			}
		}
		
		if (this.hasNameContains(paramsList)) {
			Object name = paramsList.get(QueryList.SearchParam.NAME_CONTAINS);
			
			if (name instanceof String) {
				match = this.isEventNameMatch(eventToCheck, (String)name);
			}
		}
		
		if (this.hasDateRange(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
			
			if (startDate instanceof Date && endDate instanceof Date) {
				match = this.isEventInDateRange(eventToCheck, 
												(Date)startDate, (Date)endDate);
			}
		} else if (this.hasStartDate(paramsList)) {
			Object startDate = paramsList.get(QueryList.SearchParam.DATE_START);
			
			if (startDate instanceof Date) {
				match = this.isEventAfterDate(eventToCheck, (Date)startDate);
			}
		} else if (this.hasEndDate(paramsList)) {
			Object endDate = paramsList.get(QueryList.SearchParam.DATE_END);
			
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
			if (!earliestDate.before(startDate) && !latestDate.after(endDate)) {
				return true;
			}
		}
		return false;
	}
}
