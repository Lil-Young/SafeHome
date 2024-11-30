package com.ssafy.member.model.service;

import java.sql.SQLException;
import java.util.Map;

import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.UserInfoDto;

public interface MemberService {
	
	//중복 검사
	int idCheck(String userId) throws Exception;
	
	//회원 가입
	int joinMember(MemberDto memberDto) throws Exception;
	
	//로그인 확인
	MemberDto loginMember(Map<String, String> map) throws Exception;
	
	//회원 탈퇴
	int deleteMember(String userId) throws SQLException;

	//회원 수정
	int updateMember(MemberDto meberDto) throws SQLException;

	//비밀번호 찾기
	String findPass(String userId , String userName) throws SQLException;
	
	// refreshToken 업데이트
    void updateRefreshToken(String userId, String refreshToken) throws SQLException;
	
    // user 정보 가져오기
    UserInfoDto getUserInfo(String userId) throws SQLException;
    
}
