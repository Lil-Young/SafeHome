package com.ssafy.member.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.authenticator.BasicAuthenticator.BasicCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.UserInfoDto;
import com.ssafy.member.model.service.MemberService;
import com.ssafy.util.JwtProvider;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class RestMemberController {
	private final MemberService memberService;
	private final JwtProvider jwtProvider;
	
    @Autowired
    public RestMemberController(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

	
//	/*
//	 * 마이 페이지 이동
//	 */
//	@GetMapping("/mvMyPage")
//	public ResponseEntity<String> mvMyPage(){
//		return ResponseEntity.ok("마이 페이지 이동");
//	}
//	
//	/*
//	 * 회원 가입 페이지 이동
//	 */
//	@GetMapping("/join")
//	public ResponseEntity<String> joinPage(){
//		return ResponseEntity.ok("회원가입 페이지 이동");
//	}
//	
//	/*
//	 * 로그인 페이지 이동
//	 */
//	@GetMapping("/login")
//	public ResponseEntity<String> loginPage() {
//		return ResponseEntity.ok("로그인 페이지 이동");
//	}
//	
//	/*
//	 * 비밀번호 찾기 페이지 이동
//	 */
//	@GetMapping("/findpass")
//	public ResponseEntity<String> getfind(){
//		return ResponseEntity.ok("비밀번호 찾기 페이지 이동");
//	}
	
	/*
	 * id check
	 */
	@GetMapping("/{userid}")
	public ResponseEntity<String> idCheck(@PathVariable("userid") String userId) throws Exception{
		int cnt = memberService.idCheck(userId);
		return ResponseEntity.ok(cnt+"");
	}
	
	/*
	 * 회원가입
	 */
	@PostMapping("/join")
	public ResponseEntity<String> join(
			@RequestBody MemberDto memberDto){
		try {
			memberService.joinMember(memberDto);
			return ResponseEntity.ok("회원가입 성공");
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");			
		}
	}
	
	/*
	 * 사용자 정보 가져오기
	 */
	@GetMapping("/info")
	public ResponseEntity<UserInfoDto> getUserInfo(@RequestHeader("Authorization") String token) {
	    try {
	        // Bearer 토큰에서 실제 토큰만 추출
	        String accessToken = token.replace("Bearer ", "").trim();
	        
	        // JwtProvider를 사용해 userId 추출
	        String userId = jwtProvider.getUserIdFromToken(accessToken);
	        System.out.println("info :::::: " + userId);
	        
	        // 사용자 정보 가져오기
	        UserInfoDto userInfo = memberService.getUserInfo(userId);
	        System.out.println(userInfo);
	        if (userInfo != null) {
	            return ResponseEntity.ok(userInfo);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
	}
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> map) {
	    String userId = map.get("userId");
	    String userPwd = map.get("userPwd");
	    System.out.println("로그인 시도: " + userId + " " + userPwd);

	    try {
	        MemberDto memberDto = memberService.loginMember(map);
	        if (memberDto != null) {
	            // DB에서 사용자 확인 후 토큰 생성
	            String accessToken = jwtProvider.createAccessToken(userId);
	            String refreshToken = jwtProvider.createRefreshToken(userId);

	            memberService.updateRefreshToken(userId, refreshToken);

	            Map<String, String> tokens = new HashMap<>();
	            tokens.put("accessToken", accessToken);
	            tokens.put("refreshToken", refreshToken);

	            return ResponseEntity.ok(tokens);
	        } else {
	            // 로그인 실패: 사용자 정보 없음
	            System.out.println("로그인 실패: 사용자 정보 없음");
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("message", "아이디 또는 비밀번호가 잘못되었습니다.");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	        }
	    } catch (Exception e) {
	        // 로그인 실패: 서버 오류
	        e.printStackTrace();
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("message", "서버 오류로 인해 로그인을 처리할 수 없습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}

	
	/*
	 * 로그아웃
	 */
	@GetMapping("/logout")
	public ResponseEntity<String> logout(
			HttpSession session){
		session.invalidate();
		return ResponseEntity.ok("로그아웃 성공");
	}
	
	/*
	 * 비밀번호 찾기
	 */
	@PostMapping("/findpass")
	public ResponseEntity<Map<String, String>> postfind(
			@RequestBody Map<String, String> map) throws SQLException {
	    String userId = map.get("userId");
	    String userName = map.get("userName");
		System.out.println(userId + " " + userName);
		String password = memberService.findPass(userId, userName);
		System.out.println("pw : " + password);
		Map<String, String> response = new HashMap<>();
		if(password != null) {
			response.put("passWord", password);
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.ok(response);
	}
	
	/*
	 * 회원 정보 수정
	 */
	@PutMapping("/updateMember")
	public ResponseEntity<String> updateMember(
			@RequestHeader("Authorization") String token,
			@RequestBody MemberDto memberDto) throws SQLException {
		System.out.println("updateMember: :::::::::::::" + memberDto);
		
        String accessToken = token.replace("Bearer ", "").trim();
	    String userId = jwtProvider.getUserIdFromToken(accessToken);
		if(userId.equals(memberDto.getUserId())) {
			int cnt = memberService.updateMember(memberDto);
			if(cnt>0) {
				return ResponseEntity.ok("회원 정보 수정 완료");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보 수정 실패");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자가 다릅니다.");
	}
	
	/*
	 * 회원 정보 삭제
	 */
	@DeleteMapping("/deleteMember")
	public ResponseEntity<Map<String, Object>> deleteMember(
			@RequestHeader("Authorization") String token) throws SQLException {
	    System.out.println("call deleteMember");
        String accessToken = token.replace("Bearer ", "").trim();

	    String userId = jwtProvider.getUserIdFromToken(accessToken);
	    System.out.println("delete userid ::::::::::" + userId);
	    
	    int cnt = memberService.deleteMember(userId);
	    Map<String, Object> response = new HashMap<>();
	    
	    if (cnt > 0) {
	        response.put("status", "success");
	        response.put("message", "회원 정보 삭제 완료");
	        response.put("deletedCount", cnt);
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("status", "failure");
	        response.put("message", "회원 정보 삭제 실패");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}
	
	/*
	 * refresh으로 accesstoken 재발급
	 */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        System.out.println("server : : : : :" + refreshToken);
        try {
            // Refresh Token 검증 및 Access Token 재발급
            String userId = jwtProvider.getUserIdFromToken(refreshToken);
            String newAccessToken = jwtProvider.createAccessToken(userId);
            return ResponseEntity.ok(Collections.singletonMap("accessToken", newAccessToken));
        } catch (JwtException  e) {
            // Refresh Token이 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "서버 오류가 발생했습니다."));
        }
    }
}
