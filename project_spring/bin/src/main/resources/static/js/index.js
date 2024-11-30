window.onload = function() {
	// index page 로딩 후 전국의 시도 설정.
	let areaUrl =
		"https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" +
		serviceKey +
		"&numOfRows=20&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";

	fetch(areaUrl, { method: "GET" })
		.then((response) => response.json())
		.then((data) => makeOption(data));

	function makeOption(data) {
		let areas = data.response.body.items.item;
		// console.log(areas);
		let sel = document.getElementById("search-area");
		areas.forEach((area) => {
			let opt = document.createElement("option");
			opt.setAttribute("value", area.code);
			opt.appendChild(document.createTextNode(area.name));

			sel.appendChild(opt);
		});
	}
};
