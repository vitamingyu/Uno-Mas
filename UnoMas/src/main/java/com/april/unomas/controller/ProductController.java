package com.april.unomas.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.april.unomas.domain.CategoryVO;
import com.april.unomas.domain.Commons;
import com.april.unomas.domain.Criter;
import com.april.unomas.domain.ImgType;
import com.april.unomas.domain.PagingVO;
import com.april.unomas.domain.ProdCommentVO;
import com.april.unomas.domain.BoardReviewVO;
import com.april.unomas.domain.ProdCriteria;
import com.april.unomas.domain.ProdInquiryVO;

import com.april.unomas.domain.ProdPageMaker;
import com.april.unomas.domain.ProductVO;
import com.april.unomas.domain.UserVO;
import com.april.unomas.service.ProductService;
import com.april.unomas.service.UserService;

@Controller
@RequestMapping("/product/*")
public class ProductController {

	@Inject
	private ProductService service;
	
	@Inject
	private UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Resource(name = "reviewImgUploadPath")
	private String reviewImgUploatPath;
	
	@Resource(name = "prodDimgUploadPath")
	private String prodDimgUploadPath;
	
	@Resource(name = "prodThumbUploadPath")
	private String prodThumbUploadPath;
	
	@Resource(name = "prodTopImgUploadPath")
	private String prodTopImgUploadPath;
	
	@Resource(name = "prodSoldoutImgUploadPath")
	private String prodSoldoutImgUploadPath;
  
	// product
	@RequestMapping(value = "/check-out")
	public String checkout() {
		return "product/check-out";
	}

	@RequestMapping(value = "/product_list", method = RequestMethod.GET) // /shop -> /product_list
	public String shopGET(@RequestParam("topcate_num") int topcate_num, 
			@RequestParam("pageNum") int pageNum, @RequestParam("dcate_num") int dcate_num, 
			HttpSession session, Model model) throws Exception {
		ProdCriteria cri = new ProdCriteria();
		cri.setTopcate_num(topcate_num);
		
		// 하단 페이징 처리 //////
		// 현재 분류별 전체 상품 개수 얻기
		// dcate_num(소분류) 번호가 0이라면 전체를 불러오는 것이고
		// 1이상이라면 각각의 소분류만 불러오는 것이다.
		int postCnt = 0;
		if (dcate_num == 0) {
			postCnt = service.getProductCnt(cri);
		}
		else {
			postCnt = service.getDcateCnt(dcate_num);
		}
		
		cri.setPage(pageNum);
		cri.setProd_category(dcate_num);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ProductVO> productList = null;
		if (dcate_num == 0) {
			productList = service.getProductPage(cri);
		}
		else {
			productList = service.getDcateList(cri);
		}
		
		ProdPageMaker pm = new ProdPageMaker();
		pm.setCri(cri);
		pm.setTotalCnt(postCnt);
		
		// 글 목록 정보 저장
		map.put("productList", productList);
		map.put("topcate_num", topcate_num);
		map.put("dcateNumList", service.getTopcateCnt(topcate_num));
		map.put("topcate", service.getTopCateName(topcate_num));
		map.put("dcate_num", dcate_num);
		map.put("dcateList", service.getDcateNames(topcate_num));
		
		map.put("postCnt", postCnt);
		
		// 페이지 처리 정보 저장
		map.put("pageNum", pageNum);
		map.put("pm", pm);
		
		model.addAllAttributes(map);
		
		return "product/productList";
	}
	
	@RequestMapping(value = "/product_detail", method = RequestMethod.GET)
	public String productDetailGET(@RequestParam("prod_num") int prod_num, Model model, 
			HttpSession session) throws Exception {
		ProductVO vo = service.getProduct(prod_num);
		service.addProdReadcnt(prod_num);
		
		ProdCriteria pc = new ProdCriteria();
		pc.setProd_num(prod_num);
		pc.setPerPageNum(7);
		
		List<BoardReviewVO> reviewList = service.getReviewList(pc);
		for (int i = 0; i < reviewList.size(); i++)
			reviewList.get(i).setUser_id(service.getUserid(reviewList.get(i).getUser_num()));
		
		List<ProdInquiryVO> inquiryList = service.getInquiryList(pc);
		for (int i = 0; i < inquiryList.size(); i++) {
			inquiryList.get(i).setUser_id(service.getUserid(inquiryList.get(i).getUser_num()));
			if(service.getProcess(inquiryList.get(i).getProd_num(), inquiryList.get(i).getP_inquiry_num()) == 1) {
				
			}
		}
		// 리뷰/문의 게시판 하단 페이징 처리
		int reviewCnt = service.getReviewCnt(prod_num);
		ProdPageMaker reviewPm = new ProdPageMaker();
		reviewPm.setCri(pc);
		reviewPm.setTotalCnt(reviewCnt);
		
		ProdPageMaker inquiryPm = new ProdPageMaker();
		inquiryPm.setCri(pc);
		inquiryPm.setTotalCnt(service.getInquiryCnt(prod_num));
		
		model.addAttribute("vo", vo);
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("reviewCnt", reviewCnt);
		model.addAttribute("inquiryList", inquiryList);
		model.addAttribute("reviewPm", reviewPm);
		model.addAttribute("inquiryPm", inquiryPm);
		
		String user_id = (String) session.getAttribute("saveID");
		if (user_id != null) {
			int user_num = (int) session.getAttribute("saveNUM");
			model.addAttribute("isInWishlist", service.isInWishlist(user_num, prod_num));
		}
		else
			model.addAttribute("isInWishlist", false);
		
		// 문의글 댓글 정보 저장
		List<ProdCommentVO> inqComList = new ArrayList<ProdCommentVO>();
		for (int i = 0; i < inquiryList.size(); i++) {
			ProdCommentVO comVO = service.getInqComment(inquiryList.get(i).getP_inquiry_num());
			inqComList.add(comVO);
		}
		model.addAttribute("inqComList", inqComList);
		log.info("inqComList: "+inqComList);
		
		return "product/productDetail";
	}

	@RequestMapping(value = "/co_buying_list") // /shop -> /product_list
	public String coBuyingList() {
		return "product/coBuyingList";
	}

	@RequestMapping(value = "/product_register", method = RequestMethod.GET)
	public String productRegisterGET(Model model) throws Exception {
		log.info("post 페이지 호출");
		
		List<CategoryVO> categories = service.getTopCategory();
		List<CategoryVO> details = service.getDCategory();
		log.info(categories+"");
		log.info(details+"");
		model.addAttribute("categories",categories);
		model.addAttribute("details",details);

		return "product/productRegister";
	}
	
	@RequestMapping(value = "/product_register", method = RequestMethod.POST)
	public String productRegisterPOST(ProductVO vo, Model model) throws Exception {
		vo.setProd_num(service.getLastProdNum());
		service.insertProduct(vo);
		
		return "redirect:/UnoMas/product/product_lookup";
	}
	
	@RequestMapping(value = "/product_lookup", method = RequestMethod.GET)
	public String productLookup(HttpServletRequest request, Model model) throws Exception {
		
		String page = request.getParameter("page");
		String searchType = request.getParameter("searchType");
		String keyword = request.getParameter("keyword");
		
		if(page == null ) { page = "1";}
		if(searchType == null ) { searchType = "1";}
		if(keyword == null ) { keyword = "";}
		
		log.info("페이지 :"+page);
		// 하단 페이지 처리
		ProdPageMaker pm = new ProdPageMaker();
		ProdCriteria pc = new ProdCriteria();
		pc.setPage(Integer.parseInt(page));
		pm.setCri(pc);
		pm.setTotalCnt(service.getAllCnt(searchType, keyword));
//		pm.setSrchTypeKyw(searchType, keyword);
		pm.setSearchType(searchType);
		pm.setKeyword(keyword);
		model.addAttribute("pm", pm);
		
		// 상품 데이터 조회
		List<ProductVO> productList 
			= service.getAllProductList(pc.getPageStart(), pc.getPerPageNum(), searchType, keyword);
		model.addAttribute("productList", productList);
//		model.addAttribute("select", page);
//		model.addAttribute("searchType",searchType);
//		model.addAttribute("keyword",keyword);
		
		return "product/productLookup";
	}
	
	@RequestMapping(value ="/status", method = RequestMethod.GET)
	public String products(@RequestParam("prod_num") int prod_num, /*@RequestParam("dcate_num") int dcate_num,*/ Model model) throws Exception {
		log.info("get호출");
		log.info(prod_num+"");
		List<CategoryVO> categories = service.getTopCategory();
		List<CategoryVO> details = service.getDCategory();
//		List<CategoryVO> getcate = service.getCategory(dcate_num);
		
		model.addAttribute("vo", service.getProduct(prod_num));
		model.addAttribute("categories",categories);
		model.addAttribute("details",details);
//		model.addAttribute("getcate", getcate);
		
		return "product/productStatus";
		
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String productsModifyGET(@RequestParam("prod_num") int prod_num, Model model) throws Exception {
		log.info("수정페이지 get");
		List<CategoryVO> categories = service.getTopCategory();
		List<CategoryVO> details = service.getDCategory();
		
		
		model.addAttribute("vo", service.getProduct(prod_num));
		log.info("getProduct 호출"+service.getProduct(prod_num));
		model.addAttribute("categories",categories);
		model.addAttribute("details",details);

		return "product/productModify";
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String productsModifyPOST(ProductVO vo) throws Exception {
		log.info("수정할 정보: "+vo);
		service.updateProduct(vo);
		
		return "redirect:/UnoMas/product/product_lookup";
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<Integer> productsDelete(@RequestParam(value = "chbox[]") List<String> chArr, 
			ProductVO vo) throws Exception {
		log.info("productsDelete 호출");
		log.info(vo+"");
		int result = 0;
		int prod_num = 0;
		
			for(String i : chArr) {
				prod_num = Integer.parseInt(i);
				vo.setProd_num(prod_num);
				File f1 = new File(prodTopImgUploadPath + File.separator + service.getProdImgs(prod_num).getProd_image1());
				File f2 = new File(prodDimgUploadPath + File.separator + service.getProdImgs(prod_num).getProd_image2());
				File f3 = new File(prodThumbUploadPath + File.separator + service.getProdImgs(prod_num).getProd_image3());
				File f4 = new File(prodSoldoutImgUploadPath + File.separator + service.getProdImgs(prod_num).getProd_image4());
//				log.info("경로"+f1.getAbsolutePath());
					if(f1.exists()) f1.delete();
					if(f2.exists()) f2.delete();
					if(f3.exists()) f3.delete();
					if(f4.exists()) f4.delete();
				service.deleteProduct(vo); 
			}
			result = 1;
		
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/shopping-cart")
	public String cart(Model model,
			@RequestParam(value="pageInfo", required = false, defaultValue="") String pageInfo) {
		model.addAttribute("pageInfo", pageInfo);
		
		return "product/shopping-cart";
	}
	
	@RequestMapping(value = "/write_review", method = RequestMethod.GET)
	public String writeReviewGET(@RequestParam("prod_num") int prod_num, Model model,
			HttpSession session) throws Exception {
		model.addAttribute("vo", service.getProduct(prod_num));
		model.addAttribute("user_num", userService.getUserInfo((String)session.getAttribute("saveID")));
		
		return "product/reviewWritingForm";
	}
	
	@RequestMapping(value = "/write_review", method = RequestMethod.POST)
	public String writeReviewPOST(HttpServletRequest request, 
			@RequestParam(value = "review_image", required = false) MultipartFile file) throws Exception {
		BoardReviewVO vo = new BoardReviewVO();
		vo.setProd_num(Integer.parseInt(request.getParameter("prod_num")));
		vo.setReview_content(request.getParameter("review_content"));
		vo.setReview_rating(Float.parseFloat(request.getParameter("review_rating")));
		vo.setReview_title(request.getParameter("review_title"));
		vo.setUser_num(Integer.parseInt(request.getParameter("user_num")));
		vo.setReview_ip(request.getRemoteAddr());
		
		// 업로드 된 파일이 있을 때
		if (!file.isEmpty()) {
			// 리뷰 이미지파일명: review_리뷰글번호.확장자
			String fileName = Commons.convertImgName(file.getOriginalFilename(), service.getLastReviewNum() + 1, ImgType.REVIEW);
			
			File targetFile = new File(reviewImgUploatPath, fileName);
			FileCopyUtils.copy(file.getBytes(), targetFile);
			
			vo.setReview_image(fileName);
		}
		
		service.insertReview(vo);
		
		return "redirect:/product/product_detail?prod_num=" + vo.getProd_num();
	}
	
	@RequestMapping(value = "/list_review", method = RequestMethod.GET)
	public String getReviewListGET(@RequestParam("prod_num") int prod_num, 
			@RequestParam("page") int page, Model model) throws Exception {
		ProdCriteria pc = new ProdCriteria();
		pc.setPage(page);
		pc.setPerPageNum(7);
		pc.setProd_num(prod_num);
		
		List<BoardReviewVO> list = service.getReviewList(pc);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setUser_id(service.getUserid(list.get(i).getUser_num()));
		}
		
		ProdPageMaker reviewPm = new ProdPageMaker();
		reviewPm.setCri(pc);
		reviewPm.setTotalCnt(service.getReviewCnt(prod_num));
		
		model.addAttribute("reviewList", list);
		model.addAttribute("reviewPm", reviewPm);
		model.addAttribute("page", page);
		
		return "product/revBoardAjax";
	}
	
	@RequestMapping(value = "/modify_review", method = RequestMethod.GET)
	public String modifyReviewGET(@RequestParam("review_num") int review_num, Model model,
			HttpSession session,
			@RequestParam(value="pageInfo", required = false, defaultValue="") String pageInfo) throws Exception {

		BoardReviewVO reviewVO = service.getReview(review_num);
		
		model.addAttribute("prod_name", service.getProduct(reviewVO.getProd_num()).getProd_name());
		model.addAttribute("vo", reviewVO);
		model.addAttribute("pageInfo", pageInfo);
		
		return "product/reviewModifyForm";
	}
	
	@RequestMapping(value = "/modify_review", method = RequestMethod.POST)
	public String modifyReviewPOST(HttpServletRequest request, 
			@RequestParam(value = "review_image", required = false) MultipartFile file,
			@RequestParam(value="pageInfo", required = false) String pageInfo,
			@RequestParam(value = "pagingNum", required = false, defaultValue = "1") String pagingNum) throws Exception {
		BoardReviewVO vo = new BoardReviewVO();
		vo.setReview_num(Integer.parseInt(request.getParameter("review_num")));
		vo.setProd_num(Integer.parseInt(request.getParameter("prod_num")));
		vo.setReview_content(request.getParameter("review_content"));
		vo.setReview_rating(Float.parseFloat(request.getParameter("review_rating")));
		vo.setReview_title(request.getParameter("review_title"));
		vo.setUser_num(Integer.parseInt(request.getParameter("user_num")));
		
		String reviewImg = service.getReviewImg(vo.getReview_num());
		String uploadImgName = request.getParameter("uploadImgName");

		// 업로드 된 파일이 있을 때 
		if (!file.isEmpty()) {
			// 기존에 저장된 이미지가 없는 경우 파일이름 생성
			if (reviewImg == null) {
				reviewImg = Commons.convertImgName(file.getOriginalFilename(), vo.getReview_num(), ImgType.REVIEW);
			}
			
			File targetFile = new File(reviewImgUploatPath, reviewImg);
			FileCopyUtils.copy(file.getBytes(), targetFile);
			vo.setReview_image(reviewImg);
		}
		// 업로드 된 파일이 없을 때
		else { 
			// 기존에 업로드 된 파일이 없거나 글을 수정하며 이미지를 삭제한 경우
			if (uploadImgName.equals("이미지 선택")) {
				if (reviewImg != null) {
					// 기존에 업로드 된 파일이 있으면 서버에서 삭제
					File f = new File(reviewImgUploatPath + File.separator + reviewImg);

					if (f.exists())
						f.delete();
				}
				vo.setReview_image(null);
			}
			// 기존에 업로드 된 파일이 있으면 그대로 사용
			else {
				vo.setReview_image(uploadImgName);
			}
		}
		
		service.modifyReview(vo);
		
		if(pageInfo.equals("pReview")) {
			return "redirect:/user/my_review?pagingNum="+pagingNum;
		} else {
			return "redirect:/product/product_detail?prod_num=" + vo.getProd_num();
		}
		
	}
	
	@RequestMapping(value = "/remove_review", method = RequestMethod.GET)
	public String removeReviewGET(@RequestParam("review_num") int review_num, 
			@RequestParam(value = "prod_num", required = false) int prod_num, 
			@RequestParam(value = "pagingNum", required = false, defaultValue = "1") String pagingNum, 
			@RequestParam(value="pageInfo", required = false) String pageInfo) throws Exception {
		// 리뷰와 함께 업로드 된 이미지파일 서버에서 삭제
		File f = new File(reviewImgUploatPath + File.separator + service.getReviewImg(review_num));
		if (f.exists()) 
			f.delete();
		
		service.removeReview(review_num);
		
		if(pageInfo != null && pageInfo.equals("pReview")) {
			return "redirect:/user/my_review?pagingNum="+pagingNum;
		} else {
			return "redirect:/product/product_detail?prod_num=" + prod_num;
		}
	}
	
	// 상품 문의
	@RequestMapping(value = "/write_inquiry", method = RequestMethod.GET)
	public String writeInquiryGET(@RequestParam("prod_num") int prod_num, Model model) throws Exception {
		model.addAttribute("vo", service.getProduct(prod_num));
		
		return "product/inquiryWritingForm";
	}
	
	@RequestMapping(value = "/write_inquiry", method = RequestMethod.POST)
	public String writeInquiryPOST(ProdInquiryVO vo) throws Exception {
		service.insertInquiry(vo);
		
		return "redirect:/product/product_detail?prod_num=" + vo.getProd_num();
	}
	


	// 상품 문의 내역
	@RequestMapping(value = "/list_inquiry", method = RequestMethod.GET)
	public String getInquiryListGET(@RequestParam("prod_num") int prod_num, 
			@RequestParam("page") int page, Model model) throws Exception {
		ProdCriteria pc = new ProdCriteria();
		pc.setPage(page);
		pc.setPerPageNum(7);
		pc.setProd_num(prod_num);
		
		List<ProdInquiryVO> list = service.getInquiryList(pc);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setUser_id(service.getUserid(list.get(i).getUser_num()));
		}
		
		ProdPageMaker inquiryPm = new ProdPageMaker();
		inquiryPm.setCri(pc);
		inquiryPm.setTotalCnt(service.getInquiryCnt(prod_num));
		
		model.addAttribute("inquiryList", list);
		model.addAttribute("inquiryPm", inquiryPm);
		model.addAttribute("page", page);
		
		return "product/inqBoardAjax";
	}
	

	// 상품 문의 수정
	@RequestMapping(value = "/modify_inquiry", method = RequestMethod.GET)
	public String modifyInquiryGET(@RequestParam("inquiry_num") int inquiry_num, Model model,
			@RequestParam(value = "pagingNum", required = false, defaultValue = "1") String pagingNum, 
			@RequestParam(value="pageInfo", required = false, defaultValue="") String pageInfo) throws Exception {
		ProdInquiryVO vo = service.getInquiry(inquiry_num);
		
		model.addAttribute("prod_name", service.getProduct(vo.getProd_num()).getProd_name());
		model.addAttribute("vo", vo);
		model.addAttribute("pagingNum", pagingNum);
		model.addAttribute("pageInfo", pageInfo);
		
		return "product/inquiryModifyForm";
	}
	
	@RequestMapping(value = "/modify_inquiry", method = RequestMethod.POST)
	public String modifyInquiryPOST(ProdInquiryVO vo, Model model,
			@RequestParam(value = "pagingNum", required = false) String pagingNum, 
			@RequestParam(value="pageInfo", required = false) String pageInfo) throws Exception {
		service.modifyInquiry(vo);
		
		if(pageInfo.equals("my")) {
			return "redirect:/user/my_prod_qa?pagingNum="+pagingNum;
		} else {
			return "redirect:/product/product_detail?prod_num=" + vo.getProd_num();
		}
		
	}
	
	// 상품 문의 삭제
	@RequestMapping(value = "/remove_inquiry", method = RequestMethod.GET)
	public String removeInquiryGET(@RequestParam("inquiry_num") int inquiry_num, 
			@RequestParam(value="prod_num", required = false) int prod_num,
			@RequestParam(value = "pagingNum", required = false, defaultValue = "1") String pagingNum, 
			@RequestParam(value="pageInfo", required = false, defaultValue="") String pageInfo) throws Exception {
		
		service.removeInquiry(inquiry_num);
		
		if(pageInfo.equals("my")) {
			return "redirect:/user/my_prod_qa?pagingNum="+pagingNum;
		} else {
			return "redirect:/product/product_detail?prod_num=" + prod_num;
		}
	}
	
	@RequestMapping(value = "/write_inq_comment", method = RequestMethod.GET)
	public String writeInqCommentGET(@RequestParam int prod_num, @RequestParam int p_inquiry_num,
			Model model) throws Exception {
		model.addAttribute("prod", service.getProduct(prod_num));
		model.addAttribute("inq", service.getInquiry(p_inquiry_num));
		
		return "product/inquiryAnswerForm";
	}
	
	@RequestMapping(value = "/write_inq_comment", method = RequestMethod.POST)
	public String writeInqCommentPOST(ProdCommentVO vo, HttpServletRequest request) throws Exception {
		service.writeInqComment(vo);
		log.info("ProdCommentVO: "+vo);
		
		int prod_num = Integer.parseInt(request.getParameter("prod_num"));
		int p_inquiry_num = Integer.parseInt(request.getParameter("p_inquiry_num"));
		service.prodCommProcessUp(prod_num, p_inquiry_num);
		
		return "redirect:/product/product_detail?prod_num=" + prod_num;
	}

	@RequestMapping(value = "/new_list", method = RequestMethod.GET)
	public String newProductListGET(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
			ProdCriteria pc, Model model) throws Exception {
		pc.setPage(pageNum);
		
		int postCnt = service.getNewProdCnt();
		
		ProdPageMaker pm = new ProdPageMaker();
		pm.setCri(pc);
		pm.setTotalCnt(postCnt);

		model.addAttribute("productList", service.getNewProductList(pc));
		model.addAttribute("postCnt", postCnt);
		model.addAttribute("topcate", "신상품");
		model.addAttribute("topcate_num", 6);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pm", pm);
		
		return "product/productList";
	}
	
	@RequestMapping(value = "/sale_list", method = RequestMethod.GET)
	public String saleProductListGET(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
			ProdCriteria pc, Model model) throws Exception {
		pc.setPage(pageNum);
		
		int postCnt = service.getSaleCnt();
		
		ProdPageMaker pm = new ProdPageMaker();
		pm.setCri(pc);
		pm.setTotalCnt(postCnt);

		model.addAttribute("productList", service.getSaleProductList(pc));
		model.addAttribute("postCnt", postCnt);
		model.addAttribute("topcate", "특가");
		model.addAttribute("topcate_num", 7);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pm", pm);
		
		return "product/productList";
	}
	
	@RequestMapping(value = "/product_search", method = RequestMethod.GET) // /shop -> /product_list
	public String searchGET(Model model,Criter criter) throws Exception {
		ProdCriteria cri = new ProdCriteria();
		
		// 하단 페이징 처리 //////
		// 현재 분류별 전체 상품 개수 얻기
		// dcate_num(소분류) 번호가 0이라면 전체를 불러오는 것이고
		// 1이상이라면 각각의 소분류만 불러오는 것이다.
		int postCnt = 0;
		postCnt = service.getSearchProdCnt(criter);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ProductVO> productList = null;
		productList = service.searchProd(criter);
		
		
		PagingVO pm = new PagingVO(criter);
		pm.setTotalCount(postCnt);
		
		// 글 목록 정보 저장
		map.put("productList", productList);

		map.put("postCnt", postCnt);
		
		// 페이지 처리 정보 저장
		map.put("pm", pm);
		
		model.addAllAttributes(map);
		
		return "product/productSearch";
	}
}
