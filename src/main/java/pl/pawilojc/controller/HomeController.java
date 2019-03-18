package pl.pawilojc.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import pl.pawilojc.domain.IssSpacetime;
import pl.pawilojc.service.HomeService;

@Controller
@SessionAttributes("issSpacetimeInSession")
public class HomeController {
	private HomeService homeService;

	public HomeController(HomeService homeService) {
		this.homeService = homeService;
	}

	@GetMapping("/")
	public String index(Model model, HttpSession sess) throws InterruptedException {
		IssSpacetime issSpacetimeInSession = homeService.getIssSpacetimeInSession(model, sess);

		IssSpacetime issSpacetime1 = homeService.getIssSpacetime();
		IssSpacetime issSpacetime2 = new IssSpacetime();
		do {
			issSpacetime2 = homeService.getIssSpacetime();
		} while (issSpacetime1.getTimestamp() + 3 > issSpacetime2.getTimestamp());

		int currentIssSpeed = homeService.getSpeed(issSpacetime1, issSpacetime2);
		int distanceFromBeginning = homeService.getDistance(issSpacetimeInSession, issSpacetime2);
		int distance1to2 = homeService.getDistance(issSpacetime1, issSpacetime2);

		String dateSavedInSession = homeService.getDateTimeFromIssSpacetime(issSpacetimeInSession);
		String dateOfCurrentIssSpacetime = homeService.getDateTimeFromIssSpacetime(issSpacetime2);

		model.addAttribute("issSpacetimeInSesssionCoordinates", issSpacetimeInSession.getGeographicCoordinates());
		model.addAttribute("issSpacetimeCoordinates1", issSpacetime1.getGeographicCoordinates());
		model.addAttribute("issSpacetimeCoordinates2", issSpacetime2.getGeographicCoordinates());
		model.addAttribute("currentIssSpeed", currentIssSpeed);
		model.addAttribute("distance1to2", distance1to2);
		model.addAttribute("distanceFromBeginning", distanceFromBeginning);
		model.addAttribute("dateSavedInSession", dateSavedInSession);
		model.addAttribute("dateOfCurrentIssSpacetime", dateOfCurrentIssSpacetime);

		return "index";
	}

	@GetMapping("/clear")
	public String endSession(HttpSession session, SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}

}