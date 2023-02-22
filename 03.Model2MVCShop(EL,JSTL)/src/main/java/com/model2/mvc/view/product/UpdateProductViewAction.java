package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;



public class UpdateProductViewAction extends Action{

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		System.out.println("여기는 UpdateProductViewAction 내부");		
		System.out.println("menu값은?? = ");
		System.out.println(request.getParameter("menu"));
		
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		
		ProductService service=new ProductServiceImpl();
		Product product = service.getProduct(prodNo);
		
		System.out.println("prodNo를 받은 vo 값은 = " + product);
		
		request.setAttribute("product", product);		

		//return "forward:/product/readProduct.jsp";
		//return "forward:/listProductManage.do?prodNo";  updateproductview로 가야되는거 아님?
		return "forward:/product/updateProductView.jsp";		
	}
}
