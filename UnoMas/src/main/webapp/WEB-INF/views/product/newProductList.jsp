<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="zxx">

<!-- Start Header -->
<jsp:include page="../inc/top.jsp"></jsp:include>
<link rel="stylesheet"
	href="${path}/resources/css/productList.css?afte">
<!-- Start Header -->

<%
// @@ 로드시 현재 카테고리의 상품 총 개수 가져와서 저장 @@
int cnt = 10;

// @@ DB 연결 후 페이지 정보 가져오기 @@
int pageCnt = 5;
int pageBlockCnt = 5;
int startBlock = 1;
int endBlock = 5;
%>

<body>
	<!-- Header Section Begin -->
	<jsp:include page="../inc/header.jsp"></jsp:include>
	<!-- Header End -->

	<!-- Product Shop Section Begin -->
	<section class="product-shop spad">
		<div class="container">
			<div class="row">
				<div class="col-lg-12 order-1 order-lg-2">
					<div class="product-show-option">
						<h3 class="title">신상품</h3>
						<div class="row">
							<div class="col-lg-12 col-md-12 text-right">
								<p>총 ${newProdCnt }개</p>
							</div>
						</div>
					</div>
					<div class="product-list">
						<div class="row">
						    <input type="hidden" id="prodListLen" value="${fn:length(newProductList) }">
							<c:forEach var="vo" items="${newProductList }" varStatus="i">
								<div class="col-lg-4 col-sm-6">
									<div class="product-item" id="productItem">
										<div class="pi-pic">
											<a href="product_detail"> <img
												src="${path}/resources/img/product-single/product_vegi01.jpeg"
												alt=""></a>
											<ul>
											<!-- 카트담기 버튼 -->
												<li class="w-icon active"><a href="#"><i
														class="icon_bag_alt"></i></a></li>
											</ul>
										</div>
										<div class="pi-text">
											<a href="#">
												<h5>${vo.prod_name }</h5>
											</a>
											<div class="product-price" id="prod${i.index }">
												${vo.prod_price }원
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
					<!-- @@ DB 연결하면 상세 작업하기 @@ -->
					<div class="row" id="pagediv">
						<div class="col-lg-12 text-center">
						<%if (startBlock > pageBlockCnt) { %>
							<a href="#" class="arrow_carrot-2left_alt pagingBtn" id="toFirst"></a> 
							<a href="#" class="arrow_carrot-left_alt pagingBtn" id="prev"></a> 
						<% }
						
						for (int i = startBlock; i <= endBlock; i++) { %>
							<span>
								<!----> <a class="pagingBtn" id="page<%=i%>" 
								onclick="changePageNum(<%=i%>, <%=endBlock%>);"><%=i %> <!----></a>
							</span> 
						<% }
						
						if (endBlock < pageBlockCnt) { %> 
							<a href="#" class="arrow_carrot-right_alt pagingBtn" id="next"></a> 
							<a href="#" class="arrow_carrot-2right_alt pagingBtn" id="toLast"></a>
						<% } %>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!-- Product Shop Section End -->

	<!-- Footer Section Begin -->
	<jsp:include page="../inc/bottom.jsp"></jsp:include>
	<!-- Footer Section End -->

	<!-- Js Plugins -->
	<script src="${path}/resources/js/jquery-3.3.1.min.js"></script>
	<script src="${path}/resources/js/bootstrap.min.js"></script>
	<script src="${path}/resources/js/jquery-ui.min.js"></script>
	<script src="${path}/resources/js/jquery.countdown.min.js"></script>
	<script src="${path}/resources/js/jquery.nice-select.min.js"></script>
	<script src="${path}/resources/js/jquery.zoom.min.js"></script>
	<script src="${path}/resources/js/jquery.dd.min.js"></script>
	<script src="${path}/resources/js/jquery.slicknav.js"></script>
	<script src="${path}/resources/js/owl.carousel.min.js"></script>
	<script src="${path}/resources/js/main.js"></script>
	<script src="${path}/resources/js/productList.js"></script>
</body>

</html>