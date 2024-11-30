package com.ssafy.home.controller;

import com.ssafy.home.model.GugunDto;
import com.ssafy.home.model.SidoDto;
import com.ssafy.home.model.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;
    
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    // 시/도 목록 가져오기
    @GetMapping("/sido")
    public List<Map<String, Object>> getSidoList() throws SQLException {
        System.out.println("시/도 목록 요청 수신");
        return homeService.findSido();
    }

    // 구/군 목록 가져오기
    @GetMapping("/gugun")
    public List<Map<String, Object>> getGugunList(@RequestParam String sidoName) throws SQLException {
        if (sidoName == null || sidoName.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 시/도 이름입니다.");
        }
        System.out.println("구/군 목록 요청 수신: 시/도 = " + sidoName);
        return homeService.findGugun(sidoName);
    }

    // 동 목록 가져오기
    @GetMapping("/dong")
    public List<Map<String, Object>> getDongList(
            @RequestParam String sidoName,
            @RequestParam String gugunName) throws SQLException {
        if (sidoName == null || sidoName.isEmpty() || gugunName == null || gugunName.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 시/도 또는 구/군 이름입니다.");
        }
        System.out.println("동 목록 요청 수신: 시/도 = " + sidoName + ", 구/군 = " + gugunName);
        return homeService.findDong(sidoName, gugunName);
    }
    
    @GetMapping("/getPropertyDetails")
    public Map<String, String> getPropertyDetails(@RequestParam  String code, @RequestParam  String dongName) throws SQLException {
        System.out.println("code, dongName::: " + code + " " + dongName);
        Map<String, String> location = homeService.findLocationByCodeAndDong(code, dongName);
        System.out.println("getPropertyDetails: " + location);
        // 동 정보를 기반으로 시/도와 구/군 정보 반환
        return location;
    }
    @PostMapping("/getRecommendation")
    public String getRecommendation(@RequestBody int[] ratings) throws SQLException {
        return homeService.recommendation(ratings); // 동네 추천 결과 반환
    }
    
    // 지도 범위 내 데이터 찾기
    @PostMapping("/findProperties")
    public List<?> findProperties(@RequestBody Map<String, Object> filters) throws SQLException {
        System.out.println("Received filters: " + filters);

        String type = (String) filters.get("type");
        String dealType = (String) filters.get("dealType");
        
//        if(filters.get("monthlyRent") == null) {
//            filters.put("monthlyRent", new int[]{0, 0});
//        }
        
        // 매물 유형과 거래 형태에 따라 Service 호출
        switch (type) {
            case "APT":
                return "SALE".equals(dealType)
                    ? homeService.findAptsBuy(filters)
                    : homeService.findAptsRent(filters);
            case "OFFICETEL":
                return "SALE".equals(dealType)
                    ? homeService.findOfficetelsBuy(filters)
                    : homeService.findOfficetelsRent(filters);
            case "VILLA":
                return "SALE".equals(dealType)
                    ? homeService.findVillaBuy(filters)
                    : homeService.findVillaRent(filters);
            default:
                throw new IllegalArgumentException("Invalid type or dealType");
        }
    }

    
}