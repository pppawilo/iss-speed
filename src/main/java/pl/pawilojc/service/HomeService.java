package pl.pawilojc.service;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;

import pl.pawilojc.domain.IssSpacetime;

@Service
public class HomeService {

	private static final String ISS_API_JSON_URL = "http://api.open-notify.org/iss-now.json";
	private static final int EARTH_RADIUS_IN_METERS = 6371009;
	private static final int ISS_ORBIT_IN_METERS = 408000;
	public static final String ISS_SPACETIME_SESSION_ATTRIBUTE_NAME = "issSpacetimeInSession";

	public IssSpacetime getIssSpacetime() {
		ObjectMapper objectMapper = new ObjectMapper();
		IssSpacetime issSpacetime = new IssSpacetime();
		try {
			issSpacetime = objectMapper.readValue(new URL(ISS_API_JSON_URL), IssSpacetime.class);
		} catch (IOException e) {

		}
		return issSpacetime;
	}

	public IssSpacetime getIssSpacetimeInSession(Model model, HttpSession sess) {
		if (sess.getAttribute(ISS_SPACETIME_SESSION_ATTRIBUTE_NAME) == null) {
			IssSpacetime issSpacetime = getIssSpacetime();
			model.addAttribute(ISS_SPACETIME_SESSION_ATTRIBUTE_NAME, issSpacetime);
			return issSpacetime;
		}
		return (IssSpacetime) sess.getAttribute(ISS_SPACETIME_SESSION_ATTRIBUTE_NAME);
	}

	public int getDistance(IssSpacetime issSpacetime1, IssSpacetime issSpacetime2) {
		LatLng point1 = new LatLng(issSpacetime1.getIssPosition().getLatitude(),
				issSpacetime1.getIssPosition().getLongitude());
		LatLng point2 = new LatLng(issSpacetime2.getIssPosition().getLatitude(),
				issSpacetime2.getIssPosition().getLongitude());
		double result = (LatLngTool.distanceInRadians(point1, point2) * (EARTH_RADIUS_IN_METERS + ISS_ORBIT_IN_METERS));
		System.out.println("distance: " + result);
		return (int) result;
	}

	public int getSpeed(IssSpacetime issSpacetime1, IssSpacetime issSpacetime2) {
		int distance = getDistance(issSpacetime1, issSpacetime2);
		int timeDifference = (int) (issSpacetime2.getTimestamp() - issSpacetime1.getTimestamp());
		System.out.println("timedifference: " + timeDifference);
		System.out.println("distance: " + distance);
		int result = distance / timeDifference;
		System.out.println("speed: " + result);
		return result;
	}

	public String getDateTimeFromIssSpacetime(IssSpacetime issSpacetime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		long unixTime = issSpacetime.getTimestamp();
		return Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).format(formatter);
	}

	// https://en.wikipedia.org/wiki/Haversine_formula
	// https://www.movable-type.co.uk/scripts/latlong.html
	// (c) Chris Veness 2002-2019
//	double distanceResult(double lat1, double lat2, double lon1, double lon2) {
//		double R = EARTH_RADIUS_IN_METERS;
//		double φ1 = Math.toRadians(lat1);
//		double φ2 = Math.toRadians(lat2);
//		double Δφ = Math.toRadians(lat2 - lat1);
//		double Δλ = Math.toRadians(lon2 - lon1);
//
//		double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2)
//				+ Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
//		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//
//		return R * c;
//	}
}
