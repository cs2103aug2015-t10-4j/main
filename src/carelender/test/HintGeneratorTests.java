//@@author A0133907E
package carelender.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import carelender.controller.HintGenerator;
import carelender.model.Model;

public class HintGeneratorTests {
	//String.format(MESSAGE_DELETED, OUTPUT_FILENAME, text);
	private static final int NUMBER_OF_SQUARES = 42;
	private static final int NUMBER_OF_RANGES_PER_DAY = 3;
	private static final int TEST_ROUNDS = 200;
	private int[][] monthEventNumbers = new int[NUMBER_OF_SQUARES][NUMBER_OF_RANGES_PER_DAY];
	private ArrayList<String> expectedOutput;
	
	private void resetExpectedOutput() {
		expectedOutput = Model.getInstance().loadStringArrayList("hints.dat");	
	}
	
	String BUSY_TOMORROW_HINT = "You have may tasks tomorrow. Rest well today :)";
	String REMIND_DEADLINE_HINT = "Don't forget your %1$s deadlines tomorrow!";
	String SLACK_DAY_HINT = "It seems that you are having a slack day. Spend some time with your family :)";
	String BUSY_NEXT_WEEK_HINT = "You have %1$s tasks next week! Be prepared.";
	String EXERCISE_HINT = "You don't have many tasks next week. Try exercise more :)";
	String FREE_THEN_FREE_HINT = "Next week is so free! How nice :)";
	String BUSY_THEN_FREE_HINT = "Next week is so free! Hang on there :)";
	String VACATION_PLAN_HINT = "This month looks clear, why not plan a vacation?";
	String NICE_DAY_HINT = "Have a nice day :)";
	
	@Test
	public void isBasicHintsLoaded() {
		resetExpectedOutput();
		String hint = HintGenerator.getInstance().getHints();
		assertTrue(expectedOutput.contains(hint));
	}
	
	@Test
	public void isTomorrowFreeHintLoaded() {
		resetExpectedOutput();
		for (int i=0; i<NUMBER_OF_SQUARES; i++) {
			for(int j=0; j<NUMBER_OF_RANGES_PER_DAY; j++){
				monthEventNumbers[i][j] = 0;
			}
		}
		HintGenerator.getInstance().setEventNumbers(monthEventNumbers);
		for (int k=0; k<TEST_ROUNDS; k++) {
			String hint = HintGenerator.getInstance().getHints();
			if (!expectedOutput.contains(hint)) {
				expectedOutput.add(hint);
			}
		}
		assertTrue(expectedOutput.contains(VACATION_PLAN_HINT));
		assertTrue(expectedOutput.contains(SLACK_DAY_HINT));
	}

}
