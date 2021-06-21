package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerformanceReport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String durationApprovedExtensions;
	private String durationActivityTimeAdded;
	
	public PerformanceReport(String durationApprovedExtensions, String durationActivitytimeAdded) {
		this.durationApprovedExtensions=durationApprovedExtensions;
		this.durationActivityTimeAdded=durationActivitytimeAdded;
	}
	
	public String getDurationApprovedExtensions() {
		return durationApprovedExtensions;
	}
	public void setDurationApprovedExtensions(String durationApprovedExtensions) {
		this.durationApprovedExtensions = durationApprovedExtensions;
	}
	public String getDurationActivityTimeAdded() {
		return durationActivityTimeAdded;
	}
	public void setDurationActivityTimeAdded(String durationActivityTimeAdded) {
		this.durationActivityTimeAdded = durationActivityTimeAdded;
	}
	public List<String> getAll(){
		List<String> list = new ArrayList<>();
		list.add(durationApprovedExtensions);
		list.add(durationActivityTimeAdded);
		return list;
	}
}
