package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;



public class GetProductAction extends Action{

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		System.out.println("여기는 getProductAction 내부");
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		
		System.out.println("getProductAction :: prodNo 값은 ===== "+prodNo);
		
		ProductService service=new ProductServiceImpl();
		Product vo = service.getProduct(prodNo);
		
		System.out.println("menu값?=================");
		System.out.println(request.getParameter("menu"));		
		
		System.out.println("prodNo를 받은 vo 값은 = " + vo);
		
		request.setAttribute("vo", vo);		
		request.setAttribute("menu",request.getParameter("menu"));

		return "forward:/product/updateProduct.jsp";
		//return "forward:/updateProductView.do?prodNo"; 
		//if(request.getParameter("menu").equals("manage")) {			
		//	return "forward:/product/updateProductManage.jsp";
		//}else {
		//	return "forward:/product/updateProductSearch.jsp";
		//}		
	}
}