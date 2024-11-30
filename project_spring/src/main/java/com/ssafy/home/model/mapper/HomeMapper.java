package com.ssafy.home.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.home.model.GugunDto;
import com.ssafy.home.model.SidoDto;
import com.ssafy.home.model.buyDto.AptsBuyDto;
import com.ssafy.home.model.buyDto.OfficetelsBuyDto;
import com.ssafy.home.model.buyDto.VillaBuyDto;
import com.ssafy.home.model.rentDto.AptsRentDto;
import com.ssafy.home.model.rentDto.OfficetelsRentDto;
import com.ssafy.home.model.rentDto.VillaRentDto;

@Mapper
public interface HomeMapper {

    // 시도, 구군, 동 관련 메서드
    List<Map<String, Object>> findSido() throws SQLException;
    List<Map<String, Object>> findGugun(String sido) throws SQLException;
    List<Map<String, Object>> findDong(@Param("sido") String sido, @Param("gugun") String gugun) throws SQLException;
    Map<String, String> findLocationByCodeAndDong(@Param("code") String code, @Param("dongName") String dongName);
    int getCountByRegion(String table, String region) throws SQLException;
    
    // 매물 관련 메서드
    List<?> findAptsBuy(Map<String, Object> filters) throws SQLException;
    List<?> findAptsRent(Map<String, Object> filters) throws SQLException;
    List<?> findOfficetelsBuy(Map<String, Object> filters) throws SQLException;
    List<?> findOfficetelsRent(Map<String, Object> filters) throws SQLException;
    List<?> findVillaBuy(Map<String, Object> filters) throws SQLException;
    List<?> findVillaRent(Map<String, Object> filters) throws SQLException;
}