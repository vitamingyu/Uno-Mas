package com.april.unomas;

import javax.inject.Inject;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.april.unomas.service.ProductService;


@Controller
public class ProductController {

	@Inject
	private ProductService service;
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	// product
	@RequestMapping(value = "/check-out")
	public String checkout() {
		return "product/check-out";
	}
	@RequestMapping(value = "product/product_list", method = RequestMethod.GET) // /shop -> /product_list
	public String shopGET(@RequestParam("topcate_num") int topcate_num, @RequestParam("pageNum") String pageNum, 
			Model model) throws Exception {
		// 페이징 처리 작업
		int pageSize = 9;
		if (pageNum == null) {
			// 만약 페이지 정보가 없으면 1번 페이지로 설정
			pageNum = "1";
		}
		
		int curPage = Integer.parseInt(pageNum);
		
		// 현재 페이지정보를 이용해서 시작 행 정보 계산
		int startRow = (curPage - 1) * pageSize + 1;
		
		// 하단 페이징 처리 //////
		// 현재 대분류의 전체 상품 개수 얻기
		int postCnt = service.getProductCnt(topcate_num);
		
		// 페이지 전체 블록 개수 계산
		int pageCnt = postCnt / pageSize + ((postCnt % pageSize == 0) ? 0 : 1);
		
		// 한 번에 보여줄 페이지 블록 개수
		int pageBlockCnt = 10;
		
		// 시작 페이지 번호 구하기
		int startBlock = ((curPage - 1) / pageBlockCnt) * pageBlockCnt + 1;
		
		// 끝 페이지 블록 번호 구하기
		int endBlock = startBlock + pageBlockCnt - 1;
		if (endBlock > pageCnt)
			endBlock = pageCnt;
		
		// 글 목록 정보 저장
		model.addAttribute("productList", service.getProductList(topcate_num, startRow, pageSize));
		model.addAttribute("topcate_num", topcate_num);
		model.addAttribute("topcate", service.getTopCateName(topcate_num));
		model.addAttribute("dcateList", service.getDcateNames(topcate_num));
		
		// 페이지 처리 정보 저장
		model.addAttribute("postCnt", postCnt);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageCnt", pageCnt);
		model.addAttribute("pageBlockCnt", pageBlockCnt);
		model.addAttribute("startBlock", startBlock);
		model.addAttribute("endBlock", endBlock);
		
		return "product/productList";
	}
	
	@RequestMapping(value = "/product_detail") // /product -> /product_detail
	public String product() {
		return "product/productDetail";
	}

	@RequestMapping(value = "/product_register")
	public String productRegister() {
		return "product/productRegister";
	}

	@RequestMapping(value = "/shopping-cart")
	public String cart() {
		return "product/shopping-cart";
	}
	@RequestMapping(value = "/review_writing_form")
	public String reviewWritingForm() {
		return "product/reviewWritingForm";
	}
	@RequestMapping(value = "/product_qna_writing_form")
	public String qnaWritingForm() {
		return "product/qnaWritingForm";
	}

	@RequestMapping(value = "/new_product_list", method = RequestMethod.GET)
	public String newProductList() {
		return "product/newProductList";
	}
	@RequestMapping(value = "/sale_product_list", method = RequestMethod.GET)
	public String saleProductList() {
		return "product/saleProductList";
	}

}
