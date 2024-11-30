package com.ssafy.home.model.service;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.home.model.GugunDto;
import com.ssafy.home.model.SidoDto;
import com.ssafy.home.model.buyDto.*;
import com.ssafy.home.model.rentDto.*;
import com.ssafy.home.model.mapper.HomeMapper;

@Service
public class HomeServiceImpl implements HomeService {

    private final HomeMapper homeMapper;

    public HomeServiceImpl(HomeMapper homeMapper) {
        super();
        this.homeMapper = homeMapper;
    }
    
    @Override
    public List<Map<String, Object>> findSido() throws SQLException {
        return homeMapper.findSido();
    }

    @Override
    public List<Map<String, Object>> findGugun(String sido) throws SQLException {
        if (sido == null || sido.isEmpty()) {
            throw new IllegalArgumentException("시/도가 유효하지 않습니다.");
        }
        return homeMapper.findGugun(sido);
    }

    @Override
    public List<Map<String, Object>> findDong(String sido, String gugun) throws SQLException {
        if (sido == null || sido.isEmpty() || gugun == null || gugun.isEmpty()) {
            throw new IllegalArgumentException("시/도와 구/군이 유효하지 않습니다.");
        }
        return homeMapper.findDong(sido, gugun);
    }
    
    @Override
    public Map<String, String> findLocationByCodeAndDong(String code, String dongName) throws SQLException {
        // TODO Auto-generated method stub
        Map<String, String> response = homeMapper.findLocationByCodeAndDong(code, dongName);
        System.out.println("Service findLocationByDong::: " + response);
        return response;
    }
    
    @Override
    public List<?> findAptsBuy(Map<String, Object> filters) throws SQLException {
        System.out.println("Service: 아파트 매매, Filters: " + filters);
        return homeMapper.findAptsBuy(filters);
    }

    @Override
    public List<?> findAptsRent(Map<String, Object> filters) throws SQLException {
        System.out.println("Service: 아파트 전/월세, Filters: " + filters);
        return homeMapper.findAptsRent(filters);
    }

    @Override
    public List<?> findOfficetelsBuy(Map<String, Object> filters) throws SQLException {
        System.out.println("Service: 오피스텔 매매, Filters: " + filters);
        return homeMapper.findOfficetelsBuy(filters);
    }

    @Override
    public List<?> findOfficetelsRent(Map<String, Object> filters) throws SQLException {
        System.out.println("Service: 오피스텔 전/월세, Filters: " + filters);
        return homeMapper.findOfficetelsRent(filters);
    }

    @Override
    public List<?> findVillaBuy(Map<String, Object> filters) throws SQLException {
        System.out.println("Service: 주택/빌라 매매, Filters: " + filters);
        return homeMapper.findVillaBuy(filters);
    }

    @Override
    public List<?> findVillaRent(Map<String, Object> filters) throws SQLException {
        System.out.println("Service: 주택/빌라 전/월세, Filters: " + filters);
        return homeMapper.findVillaRent(filters);
    }

    @Override
    public String recommendation(int[] ratings) throws SQLException {
        if (ratings == null || ratings.length != 5) {
            throw new IllegalArgumentException("Invalid ratings array. Expected length: 5.");
        }

        Map<String, Integer> scores = new HashMap<>();
        String[] regions = { "대덕구", "동구", "서구", "유성구", "중구" };

        for (String region : regions) {
            int score = calculateScore(region, ratings);
            scores.put(region, score);
        }

        String recommendedRegion = scores.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse("추천 결과 없음");

        // 지역 이름에 시/도를 추가
        return "대전광역시 " + recommendedRegion;
    }

    private int calculateScore(String region, int[] ratings) {
        int totalHospitals = safeGetCount("hospital", region);
        int totalRestaurants = safeGetCount("restaurants", region);
        int totalPlayAreas = safeGetCount("play", region);
        int totalGyms = safeGetCount("exercise", region);
        int totalPharmacies = safeGetCount("pharmacies", region);

        // 전체 데이터 합산
        int total = totalHospitals + totalRestaurants + totalPlayAreas + totalGyms + totalPharmacies;

        // 데이터가 없는 경우 점수 0 반환
        if (total == 0) {
            return 0;
        }

        // 가중치 설정
        double hospitalWeight = ratings[0] >= 4 ? 2.0 : 1.0;
        double restaurantWeight = ratings[1] >= 4 ? 1.5 : 1.0;
        double playWeight = ratings[2] >= 4 ? 1.7 : 1.0;
        double gymWeight = ratings[3] >= 4 ? 1.3 : 1.0;
        double pharmacyWeight = ratings[4] >= 4 ? 2.5 : 1.0;

        // 정규화된 점수 계산
        double hospitals = (double) totalHospitals / total * ratings[0] * hospitalWeight;
        double restaurants = (double) totalRestaurants / total * ratings[1] * restaurantWeight;
        double playAreas = (double) totalPlayAreas / total * ratings[2] * playWeight;
        double gyms = (double) totalGyms / total * ratings[3] * gymWeight;
        double pharmacies = (double) totalPharmacies / total * ratings[4] * pharmacyWeight;

        return (int) (hospitals + restaurants + playAreas + gyms + pharmacies);
    }


    private int safeGetCount(String table, String region) {
        try {
            Integer count = homeMapper.getCountByRegion(table, region); // dao -> homeMapper 수정
            return count != null ? count : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}