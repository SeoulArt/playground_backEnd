package com.skybory.seoulArt.domain.board;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 하단에 작품소개 페이지는 -> event, 예매하기 페이지는 ->ticket, 커뮤니티 페이지는 -> reply, 증강현실 페이지는 ?
@Controller	// 해당 컨트롤러는 개발단계에서만 사용
public class BoardController {

//    @GetMapping("/login")
//    public String loginPage() {
//
//        return "login";
//    }
    // 메인페이지에 뭐 담아야하는지..?
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
    	System.out.println("=================");
    	return "ticket";
    }
}