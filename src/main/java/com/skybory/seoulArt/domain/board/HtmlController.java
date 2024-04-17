package com.skybory.seoulArt.domain.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/html")
public class HtmlController {


	@GetMapping("/admin")
	public String admin() {
		return "admin/admin";
	}

	@GetMapping("/event/create")
	public String createForm() {
		return "event/create";
	}
	
	@GetMapping("/event/main")
	public String eventMain() {
		return "event/main";
	}
	
	@GetMapping("/members")
	public String memberList() {
		return "member";
	}
	
	@GetMapping("/eventList")
	public String eventList() {
		return "event";
	}

}

