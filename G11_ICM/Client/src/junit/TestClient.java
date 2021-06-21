package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import controller.NewRequestController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

class TestClient {

	private NewRequestController newRequest;
	
	//Test all fields empty
	@Test
	void wrongInput() {
		newRequest = new NewRequestController();		
		//empty input
		Assertions.assertTrue(!newRequest.checkFormFields("","","","").equals("Input is ok"));
	}
	//Test current state empty
	@Test
	void notFullInput() {
		newRequest= new NewRequestController();
		Assertions.assertFalse(newRequest.checkFormFields("Moodle","","asd","f").equals("Input is ok"));
	}
	//Test current state and change requested empty
	@Test
	void notFullInput2()
	{
		newRequest= new NewRequestController();
		Assertions.assertTrue(newRequest.checkFormFields("Library","","","d").equals("The following fields are empty: Current state, Change. "));
	}
	//Test correct input
	@Test
	void correctInput() {
		newRequest= new NewRequestController();
		Assertions.assertTrue(newRequest.checkFormFields("Moodle","state","change","purpose").contentEquals("Input is ok"));
	}
}
