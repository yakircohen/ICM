package common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import entity.Request;

class TestServer {
	private Request request = new Request();
	private DBHandler dbHandler = new DBHandler();
	
	
	@Test
	void checkingRequestInsertion() {
		request.setIdUser("2");
		request.setSystem("Office");
		request.setCurState("state");
		request.setRequestedChange("change");
		request.setPurpose("purpose");
		request.setOpentDate("24/01/2020");
		
		int generatedRequestID = dbHandler.insertRequest(request);
		
		String expected = dbHandler.getUserID(generatedRequestID);
		
		Assertions.assertEquals(expected,"2");
		
		
	}
	
	

}
