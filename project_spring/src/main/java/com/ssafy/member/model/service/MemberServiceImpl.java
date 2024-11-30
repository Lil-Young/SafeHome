package com.ssafy.member.model.service;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.UserInfoDto;
import com.ssafy.member.model.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{
	
	private final MemberMapper memberMapper;
	
	public MemberServiceImpl(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

	@Override
	public int idCheck(String userId) throws Exception {
		// TODO Auto-generated method stub
		return memberMapper.idCheck(userId);
	}

	@Override
	public int joinMember(MemberDto memberDto) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("service join memberDto :::::: " + memberDto);
		return memberMapper.joinMember(memberDto);
	}

	@Override
	public MemberDto loginMember(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("service map :::::: " + map);
		return memberMapper.loginMember(map);
	}

	@Override
	public int deleteMember(String userId) throws SQLException {
	    // 먼저 참조 데이터 삭제
	    memberMapper.deleteMemberReferences(userId);
	    // 부모 데이터 삭제
	    return memberMapper.deleteMember(userId);
	}


	@Override
	public int updateMember(MemberDto memberDto) throws SQLException {
		// TODO Auto-generated method stub
		return memberMapper.updateMember(memberDto);
	}

	@Override
	public String findPass(String userId, String userName) throws SQLException {
		// TODO Auto-generated method stub
		return memberMapper.findPass(userId, userName);
	}

	@Override
	public void updateRefreshToken(String userId, String refreshToken) throws SQLException {
		// TODO Auto-generated method stub
		memberMapper.updateRefreshToken(userId, refreshToken);
	}

	@Override
	public UserInfoDto getUserInfo(String userId) throws SQLException {
		// TODO Auto-generated method stub
		return memberMapper.getUserInfo(userId);
	}
	
	
}
