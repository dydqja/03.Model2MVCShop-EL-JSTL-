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


public class ListProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		System.out.println("1.여기는 ListProductAction 내부");
		
		System.out.println("ListProductAction :: menu값은 ==========");
		System.out.println(request.getParameter("menu"));		
		
		Search search=new Search();
		
		int currentPage=1;
		
		if(request.getParameter("currentPage") != null) {
			currentPage=Integer.parseInt(request.getParameter("currentPage"));
	}
		
		search.setCurrentPage(currentPage);
		search.setSearchCondition(request.getParameter("searchCondition"));
		search.setSearchKeyword(request.getParameter("searchKeyword"));
		
		// web.xml  meta-data 로 부터 상수 추출
		int pageSize = Integer.parseInt( getServletContext().getInitParameter("pageSize"));
		int pageUnit = Integer.parseInt( getServletContext().getInitParameter("pageUnit"));
		search.setPageSize(pageSize);
//		String pageUnit=getServletContext().getInitParameter("pageUnit");
		
		// Business logic 수행
		ProductService productService=new ProductServiceImpl();
		Map<String, Object> map=productService.getProductList(search);	
		
		System.out.println("2.여기는 ListProductAction 내부");
		
		Page resultPage	= 
				new Page( currentPage, ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);		
		
		System.out.println("ListProductAction ::"+search);		
		System.out.println("ListProductAction ::"+resultPage);
		System.out.println("ListProductAction ::"+map.get("list"));		
		
		// Model 과 View 연결
		request.setAttribute("list", map.get("list"));
		request.setAttribute("resultPage", resultPage);
		request.setAttribute("search", search);
		request.setAttribute("menu",request.getParameter("menu"));
					
		return "forward:/product/listProduct.jsp";		
	}
}