package gui;

/**
 * Table view for Requierment table in bd
 * 
 */

public class RequestsTable {
	private String name, board, handler, status;
	
	public RequestsTable(String name, String board, String handler, String status) {
		super();
		this.name = name;
		this.board = board;
		this.handler = handler;
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBoard() {
		return board;
	}
	public void setBoard(String board) {
		this.board = board;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
