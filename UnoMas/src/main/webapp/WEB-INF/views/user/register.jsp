<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="zxx">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script src="../scripts/jquery-3.6.0.js"></script>
<script src="./SignUp.js"></script>
<link rel="stylesheet" href="${path}/resources/css/user_css/register.css">
<title>회원가입 페이지</title>

</head>

<body>
<%-- 	<jsp:include page="../inc/top.jsp"></jsp:include> --%>
	
    <div class="wrap">
    	<div class="form-wrap">
            <h1 class="register_title">회원가입</h1>
                
            <form id="register" action="./SignUpAction.us" onsubmit="return signUpCheckFunc()" class="input-group">
                	
                <label>아이디</label><span id="id"></span><br>
                <input type="text" class="input-field" name="id" oninput="changeIDCheck()" placeholder="3~20자" minlength=3 maxlength=20 >   
                <input type="button" class="check-button" name="idCheckBtn" value="중복 체크" onclick="idCheckFunc()"><br>
                    
                    
                <label>비밀번호</label><span id="pw"></span><br>
                <input type="password" class="input-field" name="pw" placeholder="대문자+소문자+숫자+특수문자" minlength=8 maxlength=16><br>
                    
                <label>비밀번호 확인</label><span id="pwCheck"></span><br>
                <input type="password" class="input-field" name="pwCheck" minlength=8 maxlength=16><br>
                    
                <label>이름</label><span id="name"></span><br>
                <input type="text" class="input-field" name="name" minlength=1 maxlength=15><br>
                    
                <label>생년월일</label><span id="birth"></span><br>
				<select class="input-birth" name="birth-year" oninput="birthCheck()">
					<option value="">연도</option>
					<%
					  for(int i=1950; i<=2010; i++) {
						  %><option value="<%=i%>"><%=i %></option><%
					  }
					%>
				</select>
                <select name="birth-month" class="input-birth" oninput="birthCheck()" disabled="disabled">
					<option value="">월</option>
					<%
					  for(int i=1; i<=12; i++) {
						  %><option value="<%=i%>"><%=i %></option><%
					  }
					%>
				</select>
                
				<select name="birth-day" class="input-birth" disabled="disabled">
					<option value="">일</option>
				</select><br>
				
				
				<label>핸드폰 번호</label><span id="phone"></span><br>
                <input type="text" class="input-field" name="phone" placeholder="-없이 숫자만 입력">
                <input type="button" class="check-button" name="phoneCheck" value="인증하기" onclick="phoneCheckFunc()"><br>
				<div name="phoneCheckDiv"></div>

                <label>이메일</label><span id="email"></span><br>
                <input type="text" class="input-field" name="email"><br>
                    
                <label>주소</label><span id="addr"></span><br>
                <input type="text" class="input-addr" id="postnum" name="post" placeholder="우편번호" style="width:150px;">
				<input type="button" onclick="searchPostNum()" value="주소 가져오기" class="check-button" id="bringAddr"><br>
					
				<input type="text" id="roadAddr" placeholder="도로명주소" name="roadAddr" class="input-addr"><br>
				<input type="text" id="detailAddr" placeholder="상세주소" name="detailAddr" class="input-addr"><br>
				<input type="hidden" id="jibunAdd" placeholder="지번주소"><br>
                    
                <br>
                <label onclick="infoAgreeCheck()" class="input-checkbox" id="infoAgree">
                	<input type="checkbox" name="infoAgree" value="true" id="infoAgree"> 이용약관 동의(필수)
                </label>

                <span id="infoAgree"></span><br>
                <label class="input-checkbox" id="emailAgree">
             		<input type="checkbox" name="emailAgree" value="true" id="emailAgree"> 이메일 수신 동의(선택)
             	</label><br>
             		
                <button class="submitbutton">회원가입</button>
            </form>
        </div>
    </div>


</body>
</html>