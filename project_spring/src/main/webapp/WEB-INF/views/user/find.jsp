<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
	crossorigin="anonymous" />
<title>SSAFY</title>
</head>
<body>
	<div class="col-lg-8 col-md-10 col-sm-12">
		<form id="form-find" method="POST" action="">
			<input type="hidden" name="action" value="join" />
			<div class="mb-3">
				<label for="username" class="form-label">이름 : </label> <input
					type="text" class="form-control" id="username" name="userName"
					placeholder="이름..." />
			</div>
			<div class="mb-3">
				<label for="userid" class="form-label">아이디 : </label> <input
					type="text" class="form-control" id="userid" name="userId"
					placeholder="아이디..." />
			</div>
			<div class="mb-3">
				<label for="emailid" class="form-label">이메일 : </label>
				<div class="input-group">
					<input type="text" class="form-control" id="emailid" name="emailid"
						placeholder="이메일아이디" /> <span class="input-group-text">@</span> <select
						class="form-select" id="emaildomain" name="emaildomain"
						aria-label="이메일 도메인 선택">
						<option selected>선택</option>
						<option value="ssafy.com">싸피</option>
						<option value="google.com">구글</option>
						<option value="naver.com">네이버</option>
						<option value="kakao.com">카카오</option>
					</select>
				</div>
			</div>
			<div class="col-auto text-center">
				<button type="button" id="btn-login"
					class="btn btn-outline-primary mb-3">로그인 창으로</button>
				<button type="button" id="btn-find"
					class="btn btn-outline-warning mb-3">비밀번호 찾기</button>
			</div>
		</form>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
		crossorigin="anonymous">
	</script>
	<script>
		document.querySelector("#btn-login").addEventListener("click", function() {
			location.href="${root}/";
		});
		
		document.querySelector("#btn-find").addEventListener("click", function() {
    		const userId = document.getElementById("userid").value;
    		const userName = document.getElementById("username").value;
    		console.log("userId:", userId, " userName:", userName);
			console.log("click me!!");
    		fetch("${root}/user/findpass", {
        		method: "POST",
        		headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
        		body: new URLSearchParams({
            		userId: userId,
            		userName: userName
        		})
    		})
    		.then(response => response.json())
    		.then(data => {
    			console.log(data);
        		if (data.passWord) {
            		alert("비밀번호: " + data.passWord);
        		} else {
            		alert("해당 사용자 정보로 비밀번호를 찾을 수 없습니다.");
        		}
    		})
    		.catch(error => {
        		console.error('Error:', error);
        		alert("오류가 발생했습니다. 다시 시도해 주세요.");
    		});
		});

	</script>
</body>
</html>