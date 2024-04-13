package com.skybory.seoulArt.domain.board;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller	// 해당 컨트롤러는 개발단계에서만 사용
public class BoardController {

//    @GetMapping("/login")
//    public String loginPage() {
//
//        return "login";
//    }
    
    @GetMapping("/")
    public String mainPage() {
       return "main";
    }
    
    @GetMapping("/my")
    public String myPage() {

        return "my";
    }

    @GetMapping("/ticket")
    public String ticket() {
    	
    	return "ticket";
    }
}