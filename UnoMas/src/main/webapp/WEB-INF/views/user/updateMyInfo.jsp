<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>

<head>
<meta charset=UTF-8>
<title>개인정보수정 페이지</title>
<link rel="stylesheet" href="${path}/resources/css/user_css/updateMyInfo.css">

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="postalcode.js"></script>
<script type="text/javascript" src="../script/jquery-3.6.0.js"></script>
<script type="text/javascript">

function birthCheck() {
   if($("select[name=birth-year]").val()== "" || $("select[name=birth-month]").val()== ""){
      $("select[name=birth-month]").val("");
      $("select[name=birth-month]").attr('disabled', true);
      $("select[name=birth-day]").val("");
      $("select[name=birth-day]").attr('disabled', true)
   }
   
   if($("select[name=birth-year]").val() != "") {
      $("select[name=birth-month]").attr('disabled', false);
   } 
   
   if($("select[name=birth-month]").val() != "") {
      $("select[name=birth-day]").attr('disabled', false);
      
      switch ($("select[name=birth-month]").val()) {
      case "2":
         for(var i=1; i<=28; i++) {
            $("select[name=birth-day]").append("<option value='"+i+"'>"+i+"일"+"</option>")
         }
         break;
      case "4":
      case "6":
      case "9":
      case "11":
         for(var i=1; i<=30; i++) {
            $("select[name=birth-day]").append("<option value='"+i+"'>"+i+"일"+"</option>")
         }
         break;
      case "1":
      case "3":
      case "5":
      case "7":
      case "8":
      case "10":
      case "12":
         for(var i=1; i<=31; i++) {
            $("select[name=birth-day]").append("<option value='"+i+"'>"+i+"일"+"</option>")
         }
         break;
      default:
         break;
      }
      
   } 
}
</script>
</head>
<body>
	<!-- 헤더 -->
	<div style="height: 80px; border-bottom: 1px solid black; background-color: green">헤더</div>
	<!-- 헤더 -->


	<div class="myinfo_container">

		<!-- 마이페이지 카테고리 -->
		<jsp:include page="myPageLeftBar.jsp"></jsp:include>


		<div class="myinfo_content">
			<h2>개인 정보 확인</h2>
			<hr>

			<h3>기본 정보</h3>
			<div class="table_div">
				<table class="table_info"
					style="margin-bottom: 60px; height: 330px;">
					<tr>
						<th>아이디</th>
						<td><input type="text" class="input_field" name="id" disabled></td>
					</tr>

					<tr>
						<th>이름</th>
						<td><input type="text" class="input_field" name="name"></td>
					</tr>

					<tr>
						<th>이메일</th>
						<td><input type="email" class="input_field" name="email"></td>
					</tr>
					
					<tr>
						<th>생년월일</th>
						<td>
						<span id="birth"></span><br>
	          			<select class="input-birth" name="birth-year" oninput="birthCheck()">
	              			<option value="">연도</option>
			               <%
			                 for(int i=1950; i<=2022; i++) {
			                    %><option value="<%=i%>"><%=i %>년</option><%
			                 }
			               %>
			            </select>
			            <select name="birth-month" class="input-birth" oninput="birthCheck()" disabled="disabled">
			               <option value="">월</option>
			               <%
			                 for(int i=1; i<=12; i++) {
			                    %><option value="<%=i%>"><%=i %>월</option><%
			                 }
			               %>
			            </select>
	               
			            <select name="birth-day" class="input-birth" disabled="disabled">
			               <option value="">일</option>
			            </select><br>
				        </td>   
					</tr>

					<tr>
						<th>휴대폰 번호</th>
						<td><input type="text" class="input_field" name="phone" placeholder=" -없이 숫자만 입력"></td>
					</tr>
				</table>




				<div style="text-align: right;">
				<h3 style="float: left;">배송지 정보</h3>
					<input type="button" name="postalcode" value="우편번호 찾기" id="postal_btn" onclick="execDaumPostcode('0')">
				</div>
				<table class="table_info">
					<tr>
						<th>기본 배송지</th>
						<td style="line-height: 25px;">
							<input type="text" id="postalcode" name="postalcode" placeholder="우편번호">
							<input type="text" id="roadaddr" name="roadaddr" placeholder="도로명주소">
							<span id="guide" style="color:#999;display:none"></span>
							<input type="text" id="detailaddr" name="detailaddr" placeholder="상세주소">
						</td>
					</tr>
				</table>
				<table class="table_info">
					<tr>
						<th>추가 배송지</th>
						<td style="line-height: 25px;">
							<label>배송지1</label><br>
							<input type="text" id="postalcode1" name="postalcode1" placeholder="우편번호">
							<input type="text" id="roadaddr1" name="roadaddr" placeholder="도로명주소">
							<span id="guide" style="color:#999;display:none"></span>
							<input type="text" id="detailaddr" name="detailaddr" placeholder="상세주소">
							<input type="button" name="postalcode" value="우편번호 찾기" id="postal_btn2" onclick="execDaumPostcode('1')">
							<br><br>
							<label>배송지2</label><br>
							<input type="text" id="postalcode2" name="postalcode2" placeholder="우편번호">
							<input type="text" id="roadaddr2" name="roadaddr" placeholder="도로명주소">
							<span id="guide" style="color:#999;display:none"></span>
							<input type="text" id="detailaddr" name="detailaddr" placeholder="상세주소">
							<input type="button" name="postalcode" value="우편번호 찾기" id="postal_btn2" onclick="execDaumPostcode('2')">
						</td>
					</tr>
				</table>
				




				<h3>환불 정보</h3>
				<table class="table_info">
					<colgroup>
						<col style="width: 165px;">
						<col style="width: 140px;">
					</colgroup>
					<tr>
						<th id="refund_top" rowspan="4" style="width:25%;">환불계좌</th>
						<td id="refund_account">
							<div class="my_tbl_con">
								<div class="rebate_account">
									<dl>
										<dt>은행명</dt>
										<dd>
											<select class="form-select form-select-sm" aria-label=".form-select-sm example">
											  <option value="" >은행을 선택해주세요.</option>
											  <option value="기업은행">기업은행</option>
											  <option value="국민은행">국민은행</option>
											  <option value="농협은행">농협은행</option>
											  <option value="우리은행">우리은행</option>
											  <option value="신한은행">신한은행</option>
											  <option value="부산은행">부산은행</option>
											  <option value="하나은행">하나은행</option>
											</select>
										</dd>
										<dt>계좌번호</dt>
										<dd>
											<span class="input_area"> 
											<input type="text" id="account_no" name="acoount_no" title="계좌번호 입력"  style="width: 228px">
											</span>
										</dd>
										<dt>예금주</dt>
										<dd>
											<span class="input_area">
											<input type="text" id="account_p" name="account_p" title="예금주 입력" style="width: 228px">
											</span> 
											<a href="" class="refund_btn" >
											<span>계좌인증</span></a>
											<a href="" class="reset_btn" >
											<span>초기화</span></a>
										</dd>
									</dl>
									<ul class="list_type">
										<li>* 관계법령(전자상거래법 및 정보통신망법)에 따라 요금정산을 위해 계좌정보를 수집하며 [인증해제&gt; 초기화] 시 삭제됩니다.</li>
									</ul>
								</div>
							</div>
						</td>
					</tr>
				</table>

				<h3>(선택) 개인정보 수집 및 이용안내</h3>
				<hr>
				<input type="checkbox" id="checkbox_text">(선택) 이메일 이벤트등 마케팅 수신 동의
				<div id="agree_listbox">
				<ul class="agree-list">
					<li>개인정보 수집 및 이용동의 미동의 시 성별정보는 저장되지 않습니다.</li>
					<li>이메일, SMS 동의 여부 변경시 24시간 이후부터 적용됩니다.</li>
					<li>이메일, SMS 수신 동의 시 우노마스의 특가상품, 할인쿠폰, 이벤트 소식을 받을 수 있습니다.</li>
					<li>회원정보, 구매, 배송 정보 및 중요 공지사항은 수신 동의 여부에 관계없이 발송됩니다.</li>
				</ul>
				</div>
			</div>

			<div style="text-align: center;">
				<a href="" class="updateBtn">수정</a>
			</div>

		</div>
	</div>


	<!-- 푸터 -->
	<div style="border-top: 1px solid black; height: 200px; background-color: green; clear: left;">푸터</div>


</body>
</html>