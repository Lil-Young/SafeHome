//package com.ssafy.member.controller;
//
//import java.net.http.HttpResponse;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ssafy.member.model.MemberDto;
//import com.ssafy.member.model.service.MemberService;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Controller
//@RequestMapping("/user")
//public class MemberController {
//	
//	private final MemberService memberService;
//
//	public MemberController(MemberService memberService) {
//		super();
//		this.memberService = memberService;
//	}
//
//	/*
//	 * 마이 페이지로 이동
//	 */
//	@GetMapping("/mvMyPage")
//	public String mvMyPage() throws Exception {
//		return "user/mypage";
//	}
//	/*
//	 * 회원가입 페이지 이동
//	 */
//	@GetMapping("/join")
//	public String join() {
//		return "user/join";
//	}
//	
//	/*
//	 * 회원가입 ID Check
//	 */
//	@GetMapping("/{userid}")
//	@ResponseBody
//	public String idCheck(@PathVariable("userid") String userId) throws Exception {
//		//log.debug("idCheck userid : {}", userId);
//		int cnt = memberService.idCheck(userId);
//		return cnt + "";
//	}
//	
//	/*
//	 * 회원가입
//	 */
//	@PostMapping("/join")
//	public String join(MemberDto memberDto, Model model) {
//		//log.debug("memberDto info : {}", memberDto);
//		try {
//			memberService.joinMember(memberDto);
//			return "redirect:/user/login";
//		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("msg", "회원 가입 중 문제 발생!!!");
//			return "error/error";
//		}
//	}
//	
//	/*
//	 * 로그인 페이지 이동
//	 */
//	@GetMapping("/login")
//	public String login() {
//		return "user/login";
//	}
//	
//	/*
//	 * 로그인
//	 */
//	@PostMapping("/login")
//	public String login(@RequestParam Map<String, String> map, @RequestParam(name = "saveid", required = false) String saveid, Model model, HttpSession session, HttpServletResponse response) {
//		//log.debug("login map : {}", map);
//		try {
//			System.out.println(map);
//			MemberDto memberDto = memberService.loginMember(map);
//			System.out.println("memberDto :: " + memberDto);
//			if(memberDto != null) {
//				session.setAttribute("userinfo", memberDto);
//				
//				Cookie cookie = new Cookie("ssafy_id", map.get("userid"));
//				cookie.setPath("/");
//				if("ok".equals(saveid)) {
//					cookie.setMaxAge(60*60*24*365*40);
//				} else {
//					cookie.setMaxAge(0);
//				}
//				response.addCookie(cookie);
//				System.out.println("로그인 성공");
//				return "redirect:/";
//			} else {
//				System.out.println("로그인 실패");
//				model.addAttribute("msg", "아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
//				return "user/login";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("msg", "로그인 중 문제 발생!!!");
//			return "error/error";
//		}
//	}
//	
//	/*
//	 * 로그아웃
//	 */
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "redirect:/";
//	}
//	
//	/*
//	 * 비밀번호 찾기 페이지 이동
//	 */
//	@GetMapping("/findpass")
//	public String getfind() {
//		return "user/find";
//	}
//	
//	/*
//	 * 비밀번호 찾기
//	 */
//	@PostMapping("/findpass")
//	@ResponseBody
//	public ResponseEntity<Map<String, String>> postfind(@RequestParam String userId, @RequestParam String userName) throws SQLException {
//		
//		String password = memberService.findPass(userId, userName);
//		System.out.println("pw : " + password);
//		Map<String, String> map = new HashMap<>();
//		if(password != null) {
//			map.put("passWord", password);
//			return ResponseEntity.ok(map);
//		}
//		return ResponseEntity.ok(map);
//	}
//
//	
//}