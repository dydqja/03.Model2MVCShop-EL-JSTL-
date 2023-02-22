package com.model2.mvc.view.product;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;



public class UpdateProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		System.out.println("����� UpdateProductAction ����");		
		System.out.println("menu��?=================");
		System.out.println(request.getParameter("menu"));
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		String manu = request.getParameter("menu");
		
		Product product=new Product();
		product.setProdNo(prodNo);
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));		
		//product.setRegDate(request.getParameter("regDate")); <== getParameter�� String���̶� Date type�� RegDate �� ���� �� ����
		//product.setRegDate(SimpleDateFormat("yyyy-mm-dd").parse(request.getParameter("price")));  // <== ����
		//product.setRegDate((valueOF(request.getParameter("regDate"))));	// <== �Ȱǰ�..? ����
						
		ProductService service=new ProductServiceImpl();
		service.updateProduct(product);
		
		System.out.println("========������� ��������========");
		
		
		
		//return "redirect:/getProduct.do?prodNo=&munu="+prodNo+manu; // �ٽ�. �޴����� ���� getProductAction���� �ѱ�����.
																	// name,value name,value
		return "forword:/getProduct.do?prodNo="+prodNo+"&menu="+manu;
		
		
	}

	
}