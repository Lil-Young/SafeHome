<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<div class="row justify-content-center">
    <div class="col-lg-8 col-md-10 col-sm-12">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
            <mark class="orange">마이 페이지</mark>
        </h2>
    </div>
    <div class="col-lg-8 col-md-10 col-sm-12">
        <form id="form-join">
            <div class="mb-3">
                <label for="username" class="form-label">이름 : </label>
                <input
                    type="text"
                    class="form-control"
                    id="userName"
                    name="userName"
                    placeholder="이름..."
                    value="${userinfo.userName}"
                />
            </div>
            <div class="mb-3">
                <label for="userid" class="form-label">아이디 : </label>
                <input
                    type="text"
                    class="form-control"
                    id="userId"
                    name="userId"
                    placeholder="아이디..."
                    value="${userinfo.userId}"
                    readonly
                />
            </div>
            <div class="mb-3">
                <label for="userpwd" class="form-label">비밀번호 : </label>
                <input
                    type="text"
                    class="form-control"
                    id="userPwd"
                    name="userPwd"
                    placeholder="비밀번호..."
                    value="${userinfo.userPwd}"
                />
            </div>
            <div class="mb-3">
                <label for="emailid" class="form-label">이메일 : </label>
                <div class="input-group">
                    <input
                        type="text"
                        class="form-control"
                        id="emailId"
                        name="emailId"
                        placeholder="이메일아이디"
                        value="${userinfo.emailId}"
                        readonly
                    />
                    <span class="input-group-text">@</span>
                    <input
                        type="text"
                        class="form-control"
                        id="emailDomain"
                        name="emailDomain"
                        placeholder="이메일 도메인"
                        value="${userinfo.emailDomain}"
                        readonly
                    />
                </div>
            </div>
            <div class="mb-3">
                <label for="userid" class="form-label">가입일자 : </label>
                <input
                    type="text"
                    class="form-control"
                    id="joinDate"
                    name="joinDate"
                    placeholder="가입일자"
                    value="${userinfo.joinDate}"
                    readonly
                />
            </div>
            <div class="col-auto text-center">
                <button type="button" id="btn-join" class="btn btn-outline-primary mb-3" onclick="updateMember()">
                    정보수정
                </button>
                <button type="button" class="btn btn-outline-success mb-3" onclick="goBack()">뒤로가기</button>
                <button type="button" class="btn btn-outline-success mb-3" onclick="deleteMember()">회원탈퇴</button>
            </div>
        </form>
    </div>
</div>

<script type ="text/javascript">

function deleteMember() {
    let result = window.confirm("정말 탈퇴 하시겠습니까?");
    if (result) {
        fetch("${root}/rest/user/deleteMember", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            window.location.href = "/"; // 성공 시 /home으로 이동
        })
        .catch(error => {
            console.error("서버와의 통신에 실패했습니다:", error);
            alert("서버와의 통신에 실패했습니다.");
        });
    }
}





    function goBack() {
    	window.location.href = "${root}/";
    }

    function updateMember() {
        let jsonData = {
            userId: document.getElementById("userId").value,
            userName: document.getElementById("userName").value,
            userPwd: document.getElementById("userPwd").value,
            emailId: document.getElementById("emailId").value,
            emailDomain: document.getElementById("emailDomain").value,
            joinDate: document.getElementById("joinDate").value
        };
        
        fetch("/rest/user/updateMember", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(jsonData), // 주석을 제거
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            
            window.location.href = "${root}/user/mvMyPage";
            //window.location.reload();
            //let a = "${userinfo.userPwd}";
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
