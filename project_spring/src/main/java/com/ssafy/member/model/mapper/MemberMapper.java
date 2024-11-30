package com.ssafy.member.model.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.UserInfoDto;

@Mapper
public interface MemberMapper {
	
	//중복 검사
	int idCheck(String userId) throws Exception;
	
	//회원 가입
	int joinMember(MemberDto memberDto) throws Exception;
	
	//로그인 확인
	MemberDto loginMember(Map<String, String> map) throws Exception;
	
	//회원 탈퇴
	   // 참조 데이터 삭제
    int deleteMemberReferences(String userId);

    // 회원 삭제
    int deleteMember(String userId);
	//회원 수정
	int updateMember(MemberDto memberDto) throws SQLException;

	//비밀번호 찾기
	String findPass(String userId , String userName) throws SQLException;

	//토큰 업데이트
	void updateRefreshToken(String userId, String refreshToken) throws SQLException;;
	
	// 유저정보 가져오기	
    UserInfoDto getUserInfo(String userId) throws SQLException;

}
