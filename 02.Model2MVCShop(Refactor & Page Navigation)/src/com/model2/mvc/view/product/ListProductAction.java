package com.model2.mvc.view.product;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.user.UserService;

public class ListProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		
		Search search = new Search();
		Page page = new Page();
		
		System.out.println("ListProductAction시작=========");
		System.out.println("ListProductAction:"+request.getParameter("currentPage"));
		int currentPage =1;
		if(request.getParameter("currentPage") != null)
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		
		search.setCurrentPage(currentPage);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		System.out.println("ListProductAction page확인:" +currentPage);
		
		int pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
		int pageUnit = Integer.parseInt(getServletContext().getInitParameter("pageUnit"));
		search.setPageSize(pageSize);
		System.out.println("ListProductAction pageSize:"+pageSize);
		System.out.println("ListProductAction pageUnit:"+pageUnit);
		
		ProductService prodservice = new ProductServiceImpl();
		Map<String, Object> map = prodservice.getProductList(search);
		System.out.println("ListProductAction map:"+map);
		
		Page resultPage = new Page( currentPage,((Integer)map.get("totalCount")).intValue(),pageUnit, pageSize);
		System.out.println("ListProductAction resultPage확인 :"+resultPage);
		
		request.setAttribute("list", map.get("list"));
		request.setAttribute("resultPage", resultPage);
	    request.setAttribute("search", search);

		System.out.println("ListProductAction끝=========");
		return "forward:/product/listProduct.jsp";
		
	}
			
	   
	      
	      
	      

				

	}


