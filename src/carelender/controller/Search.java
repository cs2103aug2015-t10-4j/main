package carelender.controller;

import java.util.Date;
import java.util.HashMap;

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
				
				//TODO: This should instead draw from the model.
				EventList eventList = new EventList();
				
				DateRange[] dateRange = new DateRange[]{new DateRange(new Date(), new Date())};
				EventObject temp = new EventObject(0, "birthday party", dateRange);
				eventList.add(temp);
				temp = new EventObject(1, "important meeting", dateRange);
				eventList.add(temp);
				
				for (EventObject event : eventList) {
					if (this.eventMatchesParams(event, ((QueryList) query).getParamsList())) {
						returnList.add(event);
					}
				}
				
				break;
			default:
				break;
		}
		return returnList;
	}
	
	private boolean eventMatchesParams (EventObject eventToCheck,
										HashMap<QueryList.PARAM, Object> paramsList) {
		boolean match = false;
		
		if (this.hasNameExact(paramsList)) {
			Object name = paramsList.get(QueryList.PARAM.NAME_EXACT);
			
			if (name instanceof String) {
				match = this.isEventNameExact(eventToCheck, (String)name);
			}
		}
		
		if (this.hasNameContains(paramsList)) {
			Object name = paramsList.get(QueryList.PARAM.NAME_CONTAINS);
			
			if (name instanceof String) {
				match = this.isEventNameMatch(eventToCheck, (String)name);
			}
		}
		
		if (this.hasDateRange(paramsList)) {
			Object startDate = paramsList.get(QueryList.PARAM.DATE_START);
			Object endDate = paramsList.get(QueryList.PARAM.DATE_END);
			
			if (startDate instanceof Date && endDate instanceof Date) {
				match = this.isEventInDateRange(eventToCheck, 
												(Date)startDate, (Date)endDate);
			}
		} else if (this.hasStartDate(paramsList)) {
			Object startDate = paramsList.get(QueryList.PARAM.DATE_START);
			
			if (startDate instanceof Date) {
				match = this.isEventBeforeDate(eventToCheck, (Date)startDate);
			}
		} else if (this.hasEndDate(paramsList)) {
			Object endDate = paramsList.get(QueryList.PARAM.DATE_END);
			
			if (endDate instanceof Date) {
				match = this.isEventAfterDate(eventToCheck, (Date)endDate);
			}
		}
		return match;
	}
	
	private boolean hasNameExact (HashMap<QueryList.PARAM, Object> paramsList) {
		return paramsList.containsKey(QueryList.PARAM.NAME_EXACT);
	}
	
	private boolean hasNameContains (HashMap<QueryList.PARAM, Object> paramsList) {
		return paramsList.containsKey(QueryList.PARAM.NAME_CONTAINS);
	}
	
	private boolean hasStartDate (HashMap<QueryList.PARAM, Object> paramsList) {
		return paramsList.containsKey(QueryList.PARAM.DATE_START);
	}
	
	private boolean hasEndDate (HashMap<QueryList.PARAM, Object> paramsList) {
		return paramsList.containsKey(QueryList.PARAM.DATE_END);
	}
	
	private boolean hasDateRange (HashMap<QueryList.PARAM, Object> paramsList) {
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
