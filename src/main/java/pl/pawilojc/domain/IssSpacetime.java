package pl.pawilojc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IssSpacetime {
	@JsonProperty("iss_position")
	private IssPosition issPosition;
	private long timestamp;
	private String message;

	public String getGeographicCoordinates() {
		return String.valueOf(issPosition.getLatitude()) + ", " + String.valueOf(issPosition.getLongitude());
	}
}