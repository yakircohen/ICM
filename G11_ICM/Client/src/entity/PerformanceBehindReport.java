package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerformanceBehindReport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String median;
	private String standardDeviation;
	private String distributionOfDelays;
	private String durationOfDelay;
	
	public String getMedian() {
		return median;
	}
	public void setMedian(String median) {
		this.median = median;
	}
	public String getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(String standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	public String getDistributionOfDelays() {
		return distributionOfDelays;
	}
	public void setDistributionOfDelays(String distributionOfDelays) {
		this.distributionOfDelays = distributionOfDelays;
	}
	public String getDurationOfDelay() {
		return durationOfDelay;
	}
	public void setDurationOfDelay(String durationOfDelay) {
		this.durationOfDelay = durationOfDelay;
	}
	public List<String> getAll(){
		List<String> list = new ArrayList<>();
		list.add(median);
		list.add(standardDeviation);
		list.add(distributionOfDelays);
		list.add(durationOfDelay);
		return list;
	}
}
