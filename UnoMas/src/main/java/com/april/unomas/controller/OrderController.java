package com.april.unomas.controller;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.april.unomas.domain.CartVO;
import com.april.unomas.service.CartService;
import com.april.unomas.service.UserService;

@Controller
@RequestMapping("/order/*")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Inject
	private CartService cartService;
	
	@Inject
	private UserService userService;
	
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String orderGET() {
		return "order/order";
	}
	
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public String orderPOST(HttpServletRequest request, HttpSession session, Model model) {
		String[] selectedItems = request.getParameter("selectedItems").split(" ");
		List<CartVO> orderList = new ArrayList<CartVO>();
		for (int i = 0; i < selectedItems.length; i++) {
			log.info("cartNumber: " + selectedItems[i]);
			orderList.add(cartService.getSelectedItem(Integer.parseInt(selectedItems[i])));
		}
		
		// 주문 상품 목록
		log.info("Order: "+orderList);
		model.addAttribute("orderList", orderList);
		
		// 회원 정보
		model.addAttribute("userVO", userService.getUserInfo((String)session.getAttribute("saveID")));
		
		// 회원의 주소북 목록
		
		
		return "order/order";
	}
	
	@RequestMapping(value = "/purchase", method = RequestMethod.GET)
	public String purchaseGet() {
		return "order/purchase";
	}
	@RequestMapping(value = "/complete", method = RequestMethod.GET)
	public String completeGet() {
		return "order/complete";
	}
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public String checkGet() {
		return "order/check";
	}
}
