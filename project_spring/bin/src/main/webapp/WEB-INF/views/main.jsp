<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />

<div>
	<div id="map" style="position: absolute; height: 100%; width: 100%"></div>
	<div class="search-window">
		<div class="search-top">
			<div class="select">
				<div class="select-top">
					<div id="search-local">
						<select name="sido" id="sido" class="search-contents" onchange="updateGugun()">
							<option value="none" style="text-align: center">
								---선택---
							</option>
							<option value="서울특별시">서울특별시</option>
							<option value="부산광역시">부산광역시</option>
							<option value="대구광역시">대구광역시</option>
							<option value="인천광역시">인천광역시</option>
							<option value="광주광역시">광주광역시</option>
							<option value="대전광역시">대전광역시</option>
							<option value="울산광역시">울산광역시</option>
							<option value="세종특별자치시">세종특별자치시</option>
							<option value="경기도">경기도</option>
							<option value="강원도">강원도</option>
							<option value="충청북도">충청북도</option>
							<option value="충청남도">충청남도</option>
							<option value="전라북도">전라북도</option>
							<option value="전라남도">전라남도</option>
							<option value="경상남도">경상남도</option>
							<option value="제주특별자치도">제주특별자치도</option>
						</select>
					</div>
					<div id="search-tourist">
						<select name="gugun" id="gugun" class="search-contents">
							<option value="0" style="text-align: center" selected>
								---선택---
							</option>
						</select>
					</div>
				</div>
				<div class="select-bottom">
					<input id="search-keyword" type="search" class="search-content-typing" placeholder="원하는 아파트을 검색하세요" />
				</div>
			</div>
			<input type="button" class="btn-11" id="btn-search" value="검색" onclick="searchApartments()"/>
		</div>
		<hr class="divider" />
		<div class="sort-select" id="select-sort"> </div>
		<div class="search-middle" id="home-list">
			
		</div>

		<hr class="divider" />
	</div>
</div>
<script>
	async function updateGugun() {
	    const sidoSelect = document.getElementById('sido');
	    const gugunSelect = document.getElementById('gugun');
	    
	    const selectedSido = sidoSelect.value;
	
	    if (selectedSido === "none") {
	        gugunSelect.innerHTML = '<option value="0" style="text-align: center" selected>---선택---</option>';
	        return;
	    }
	    
        const response = await fetch(`${root}/home/findGugun?sido=` + selectedSido);        
        const gugunOptions = await response.json();
        console.log(gugunOptions);
	    gugunSelect.innerHTML = '<option value="0" style="text-align: center" selected>---선택---</option>';	
	    gugunOptions.forEach(gugun => {
	        const option = document.createElement('option');
	        option.value = gugun.gugun;
	        option.textContent = gugun.gugun;
	        gugunSelect.appendChild(option);	
	    });
	}

	var mapContainer = document.getElementById("map"), // 지도를 표시할 div
	  mapOption = {
	    center: new kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
	    level: 5, // 지도의 확대 레벨
	  };

	var map = new kakao.maps.Map(mapContainer, mapOption);
	var positions;
	async function searchApartments() {
	    const sidoSelect = document.getElementById('sido');
	    const gugunSelect = document.getElementById('gugun');
	    const searchKeyword = document.getElementById('search-keyword');
		
        let response;
        if (searchKeyword.value) {
            response = await fetch(`${root}/home/searchName?name=` + searchKeyword.value);
        } else if (gugunSelect.value != 0) {
            response = await fetch(`${root}/home/searchAll?sido=` + sidoSelect.value + "&gugun=" + gugunSelect.value);
        } else {
            response = await fetch(`${root}/home/searchSido?sido=` + sidoSelect.value);
        }

	    const ascData = await response.json();
	    let descData = [...ascData].reverse();
	    
	    displayList(descData);
	    
	    sortDiv.addEventListener('change', (event) => {
	        const sortValue = event.target.value;
	        if (sortValue === 'asc') {
	            displayList(ascData); // 오름차순
	        } else {
	        	displayList(descData); // 내림차순
	        }
	    });
	    
	}
	
	function displayList(datas) {
		const innerDiv = document.getElementById('home-list');
	    positions = []; // 마커 초기화
	    innerDiv.innerHTML = ''; // 초기화
	    
	    

	    datas.forEach(home => {
	        const itemDiv = document.createElement("div");
	        itemDiv.classList.add("content-items");
	        itemDiv.onclick = () => moveCenter(home.latitude, home.longitude);
	        itemDiv.innerHTML = 
	            "<div class='content-item-info'>" +
	                "<div class='content-item-name-area'>" +
	                    "<strong class='content-item-name'>" + home.aptName + "</strong>" +
	                "</div>" +
	                "<div class='content-item-address'>" + home.newAddress + "</div>" +
	                "<div class='content-item-review'>가격: " + home.dealAmount + "</div>" +
	            "</div>";
	        innerDiv.appendChild(itemDiv);
	        
	        let markerInfo = {
	            title: home.aptName,
	            latlng: new kakao.maps.LatLng(home.latitude, home.longitude),
	        };
	        positions.push(markerInfo);
	    });
	    displayMarker(); 
	}

	function displayMarker() {
		// 마커 이미지의 이미지 주소입니다
		var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

		for (var i = 0; i < positions.length; i++) {
			// 마커 이미지의 이미지 크기 입니다
			var imageSize = new kakao.maps.Size(24, 35);

			// 마커 이미지를 생성합니다
			var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

			// 마커를 생성합니다
			var marker = new kakao.maps.Marker({
				map: map, // 마커를 표시할 지도
				position: positions[i].latlng, // 마커를 표시할 위치
				title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
				image: markerImage, // 마커 이미지
			});
			
			marker.setMap(map);
		}

		// 첫번째 검색 정보를 이용하여 지도 중심을 이동 시킵니다
		map.setCenter(positions[0].latlng);
	}
	
	function moveCenter(lat, lng) {
		map.setCenter(new kakao.maps.LatLng(lat, lng));
	}
</script>