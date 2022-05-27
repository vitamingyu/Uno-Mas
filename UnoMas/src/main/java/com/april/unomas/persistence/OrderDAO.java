package com.april.unomas.persistence;

import java.util.List;
import java.util.Map;

import com.april.unomas.domain.OrderAddrVO;
import com.april.unomas.domain.OrderVO;
import com.april.unomas.domain.PayVO;
import com.april.unomas.domain.UserCriteria;

public interface OrderDAO {

	// 회원의 배송지 정보 가져오기
	public List<OrderAddrVO> getOrderAddrList(int user_num) throws Exception;
	
	// 가장 마지막 주문번호 가져오기
	public int getLastOrderNum() throws Exception;
	
	// 주문정보 생성
	public void createOrder(OrderVO vo) throws Exception;
	
	// 결제정보 생성
	public void createPay(PayVO vo) throws Exception;
	
	// 총 주문 개수
	public List<Integer> myOrderCount(String num) throws Exception;
	
	public Map<Integer, List> getMyOrderList(String num, UserCriteria cri) throws Exception;
}
