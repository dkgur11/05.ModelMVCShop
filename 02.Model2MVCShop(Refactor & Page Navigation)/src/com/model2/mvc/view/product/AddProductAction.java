package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class AddProductAction extends Action {


	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		ProductVO productVO = new ProductVO();
		System.out.println("AddProductAction 시작====");
		productVO.setProdName(request.getParameter("prodName"));
		System.out.println(request.getParameter("prodName"));
		productVO.setProdDetail(request.getParameter("prodDetail"));
		productVO.setManuDate(request.getParameter("manuDate"));
		
		int price = Integer.parseInt(request.getParameter("price"));
		productVO.setPrice(price);
		
		productVO.setFileName(request.getParameter("fileName"));
		
		System.out.println("AddProductAction productVO값확인:"+productVO);
		
		
		ProductService service = new ProductServiceImpl();
		service.addProduct(productVO);
		request.setAttribute("vo", productVO);
		System.out.println("AddProductAction 끝====");
		
		return "forward:/product/redProduct.jsp";
	}

}
