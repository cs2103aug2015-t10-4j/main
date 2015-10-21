package carelender.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import carelender.view.UserInterfaceController;
import carelender.view.UserInterfaceController.UIType;

public class UserInterfaceTest {

	@Test
	public void testSetMessageList() {
		//TODO: change the visibility of messageList to private
		UserInterfaceController test = new UserInterfaceController();
		ArrayList<String> testMessage = new ArrayList<String>();
		
		testMessage.add("Test input");
		test.setMessageList(testMessage);
		assertEquals(test.messageList.isEmpty(), false);
		
		/* 
		 * This is a boundary case for the partition where the 
		 * messageList is updated to be an empty list.
		 */
		testMessage.clear();
		assertEquals(test.messageList.isEmpty(), true);
	}
	
	public void testSetUIType(){
		UserInterfaceController test = new UserInterfaceController();
		
		assertEquals(test.setUIType(UIType.MONTH), "Month");
		
		assertEquals(test.setUIType(UIType.WEEK), "Week");
	}
}
