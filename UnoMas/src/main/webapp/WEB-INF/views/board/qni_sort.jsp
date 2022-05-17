<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	pageContext.setAttribute("br", "<br/>");
	pageContext.setAttribute("cn", "\n");
%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="zxx">
<!-- Head start -->
<jsp:include page="../inc/top.jsp"></jsp:include>
<!-- Head end -->

<body>
    <!-- Header Section Begin -->
	<jsp:include page="../inc/header.jsp"></jsp:include>
    <!-- Header End -->

    <!-- Breadcrumb Section Begin -->
    <div class="breacrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Breadcrumb Section Begin -->

    <!-- Faq Section Begin -->
    <div class="faq-section spad">
    
    <jsp:include page="../inc/board_sub_menu.jsp"></jsp:include>
    
        <div class="container">
            <div class="row">
            
                <div class="col-lg-12">
                
                    <div class="faq-accordin">
                        
                        <div class="page_aticle">
    <div class="head_aticle">
                    <h2 class="tit">자주하는 질문 <span class="tit_sub">고객님들께서 가장 자주하시는 질문을 모두 모았습니다.</span></h2>
                </div>
    
    <input type="button" value="글쓰기" onclick="location.href='/board/qni_write';">
    
    <script>
  var preContent;

  function view_content(obj)
  {
    var div = obj.parentNode;

    for (var i=1, m=div.childNodes.length;i<m;i++) {
      if (div.childNodes[i].nodeType != 1) continue;    // text node.
      else if (obj == div.childNodes[ i ]) continue;

      obj = div.childNodes[ i ];
      break;
    }

    if (preContent && obj!=preContent){
      obj.style.display = "block";
      preContent.style.display = "none";
    }
    else if (preContent && obj==preContent) preContent.style.display = ( preContent.style.display == "none" ? "block" : "none" );
    else if (preContent == null ) obj.style.display = "block";

    preContent = obj;
  }

  { // 초기출력
    var no = "faq_";
    if ( document.getElementById( no ) ) view_content( document.getElementById( no ) );
  }

 
</script>



        <form name="frmList" id="form" method="get" action="?">
            <div class="page_section">
                
                <div class="search_date">
                    <select class="btn_layer" id="qni_category">
                       			<option>카테고리 선택</option>
                       			<option value="1">배송/포장</option>
                            	<option value="2">취소/교환/환불</option>
                            	<option value="3">이벤트/적립금</option>
                            	<option value="4">상품</option>
                            	<option value="5">주문/결제</option>
                            	<option value="6">회원</option>
                            	<option value="7">서비스 이용</option>
                    </select>
                </div>
                
                <div class="xans-element- xans-myshop xans-myshop-couponserial ">
                    <table width="100%" class="xans-board-listheader">
                        <tbody>
                            <tr>
                                <th width="70" class="input_txt">번호</th>
                                <th width="135" class="input_txt">카테고리</th>
                                <th style="width: 500px; text-align: center;" class="input_txt">제목</th>
                                <th style="width: 100px; text-align: center;" class="input_txt">작성자</th>
                            </tr>
                        </tbody>
                    </table>
                                <c:forEach items="${pList }" var="vo" varStatus="i">
                    <div>
                        <div>
                            <table width="100%" class="table_faq" onclick="view_content(this)" id="faq_7">
                                <tbody>
    
                                    <tr>
                                        <td width="70" align="center">${fn:length(pList)-i.index }</td>
                                        <td width="135" align="center">${vo.qnaCateVO.qnacate_name }</td>
                                        <td style="cursor:pointer">${vo.faq_title }</td>
                                        <td style="cursor:pointer">${vo.adminVO.admin_id }</td>
                                    </tr>
                                </tbody>
                            </table>
                                
                            <div style="display:none;padding:30px; border-top:1px solid #e6e6e6">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tbody>
                                        <tr valign="top">
                                            <th style="color:#0000bf;width:40px; padding-top:1px;"></th>
                                            <td>
                                            ${fn:replace(vo.faq_content,cn,br)}<br>
                                            </td>
                                        </tr>
                                    </tbody>
                                    
                                </table>
                            </div>
                        </div>

                    </div>
                                </c:forEach>
                    <div style="padding:1px; border-top:1px solid #e6e6e6"></div>
                    <div class="layout-pagination">
                        <div class="pagediv">
<div class="row justify-content-center">
              <div class="col-1 justify-content-center ">
                <ul class="pagination">
                  <li class="page-item">
                    <a class="page-link text-dark" href='<c:url value="/board/qni_sort${pagingVO.makeQuery(pagingVO.startPage-1) }&qnacate_num=${qnacate_num }"/>' aria-label="Previous">
                      <span aria-hidden="true">&lt;</span>
                    </a>
                  </li>
                  <c:forEach begin="${pagingVO.startPage }" end="${pagingVO.endPage }" var="pageNum">
                  <li class="page-item"><a class="page-link text-dark" href='<c:url value="/board/qni_sort${pagingVO.makeQuery(pageNum) }&qnacate_num=${qnacate_num }"/>'>${pageNum }</a></li>
                  </c:forEach>
                  <li class="page-item">
                    <a class="page-link text-dark" href='<c:url value="/board/qni_sort${pagingVO.makeQuery(pagingVO.endPage+1) }&qnacate_num=${qnacate_num }"/>' aria-label="Next">
                      <span aria-hidden="true">&gt;</span>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
						
                        </div>
                    </div>
                    <table class="xans-board-search xans-board-search2">
                        <tbody>
                            <tr>
                                <td class="input_txt">&nbsp;</td>
                                
                            </tr>
                        </tbody>
                    </table>
                </div>

            </div>
        </form>
    
    
    
    
</div>
                        
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Faq Section End -->

    <!-- Partner Logo Section End -->
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
    <script type="text/javascript">
		$(document).ready(function() {
			$("#qni_category").change(function() {
				var changeVal = $(this).val();
				location.href="/board/qni_sort${pagingVO.makeQuery(pageNum) }&qnacate_num="+changeVal;
			});
		});
	</script>
</body>

</html>