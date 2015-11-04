package carelender.model.data;

public class QuerySet extends QueryBase {
	
	String keyword;
	String value;
	
    public QuerySet(String keyword, String value) {
        super(QueryType.SET);
        keyword = keyword;
        value = value;
    }
    
    @Override
	public void controllerExecute() {
		System.out.println("HAHAHAHAHAHAHAHAHAHAHAHAHA");
	}

	@Override
	public EventList searchExecute() {
		return null;
	}
}

