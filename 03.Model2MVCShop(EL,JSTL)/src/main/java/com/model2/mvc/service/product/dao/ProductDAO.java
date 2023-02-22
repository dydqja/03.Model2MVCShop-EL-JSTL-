package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;


public class ProductDAO {
	
	public ProductDAO(){
	}

	public void insertProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "insert "+
				 	 "into product "+
				 	 "values (seq_product_prod_no.nextval,?,?,?,?,?,to_date('1111-11-11','YYYY-MM-DD'))";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate().replaceAll("-",""));
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
	}

	public Product findProduct(int prodNo) throws Exception {		
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from Product where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		Product product = null;
		
		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setRegDate(rs.getDate("REG_DATE"));			
//			product.setProTranCode(rs.getString("PRO_TRAN_CODE"));
		}
		
		rs.close();
		stmt.close();
		con.close();

		return product;
	}

	public Map<String, Object> getProductList(Search search) throws Exception {
		
		System.out.println("ProductDAO :: getProductList 실행");
		
		Map<String, Object> map = new HashMap<String, Object>();
				
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT PROD_NO, PROD_NAME, PRICE, MANUFACTURE_DAY " +
				 	 " FROM product" ;
		
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE PROD_NAME LIKE '%" + search.getSearchKeyword()+"%'";
			}else if(search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE PROD_NO LIKE '%" + search.getSearchKeyword()+"%'";
				
//			} else if (search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
//				sql += " WHERE prod_name='" + search.getSearchKeyword()+ "'";
				
//			} else if (search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")) {
				
			}
		}
		
		sql += " ORDER BY PROD_NO";
		
		System.out.println("ProductDAO :: Original SQL :: " + sql);
		
		//==> TotalCount GET
		int totalCount =  this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);

//		==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurruntPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println("ProductDao :: makeCurrentPageSql return 후 :: "+search);
		
		List<Product> list = new ArrayList<Product>();

		while(rs.next()) {
			Product product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setPrice(rs.getInt("PRICE"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
//			product.setProTranCode(rs.getString("PRO_TRAN_CODE"));
			list.add(product);
		}
		
//		==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
//		==> curruntPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);
		
		rs.close();
		pStmt.close();
		con.close();
		
		return map;
	}

	public void updateProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE product " +
				 	 "SET PROD_NAME = ?, PROD_DETAIL = ?, MANUFACTURE_DAY = ?, PRICE = ?, IMAGE_FILE = ?" +
				 	 "WHERE PROD_NO = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManuDate());
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.setInt(6, product.getProdNo());	
		
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	}
	
//	게시판 Page 처리를 위한 전체 Row(totalCount)  return
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*)" +
			  "FROM ("+sql+") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if(rs.next()) {
			totalCount = rs.getInt(1);			
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
//	게시판 currentPage Row 만  return
	private String makeCurruntPageSql(String sql, Search search) {
		sql = "SELECT * " +
			  "FROM ( SELECT inner_table. * , ROWNUM AS row_seq " +
					 "FROM ( "+sql+") inner_table " +
					 "WHERE ROWNUM <=" + search.getCurrentPage()*search.getPageSize()+" ) " +
			  "WHERE row_seq BETWEEN " +((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("ProductDAO :: make SQL :: "+ sql);
//		System.out.println("ProductDAO :: make search :: "+ search);
		
		return sql;
					 
	}
	
	
	
}