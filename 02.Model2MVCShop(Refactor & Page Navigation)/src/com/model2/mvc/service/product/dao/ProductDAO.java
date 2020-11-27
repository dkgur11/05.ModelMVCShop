package com.model2.mvc.service.product.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.Page;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {

	public ProductDAO() {
		// TODO Auto-generated constructor stub
	}
	public ProductVO findProduct(int prodNo) throws Exception {
		Connection con = DBUtil.getConnection();
		System.out.println("findProduct prodNo:"+prodNo);
		String sql = "select * from PRODUCT where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);
		
		System.out.println("findProduct sql:"+sql);
		ResultSet rs = stmt.executeQuery();
		
		ProductVO productVO = null;
		while(rs.next()) {
			productVO = new ProductVO();
			productVO.setFileName(rs.getString("IMAGE_FILE"));
			productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setProdDetail(rs.getString("PROD_DETAIL"));
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setProdNo(rs.getInt("PROD_NO"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
		}
		con.close();
		
		return productVO;
		
	
	}
	public Map<String, Object> getProductList(Search search) throws Exception{
		
		/* Page page = new Page(); */
		Map<String,Object> map = new HashMap<String,Object>();
		Connection con = DBUtil.getConnection();
		System.out.println("getProductList 시작====!!!" + search.getSearchKeyword());
		
		String sql = "select * from PRODUCT ";
		
		if(search.getSearchCondition() != null) {
			if(search.getSearchCondition().equals("0") && !search.getSearchKeyword().equals("")) {
				sql += "where PROD_NO LIKE "+search.getSearchKeyword();
			}else if(search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
				sql += "where PROD_NAME LIKE '%"+search.getSearchKeyword()+"%'";
			}else if(search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")){
				sql += "where PRICE LIKE "+search.getSearchKeyword();
			}
		}
		sql +=" order by PROD_NO DESC";
		System.out.println("getProductList sql:" +sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO:getProductList TotalCount:" + totalCount);
		sql = makeCurrentPageSql(sql, search);
		
		 PreparedStatement pStmt = con.prepareStatement( sql,
				 		ResultSet.TYPE_SCROLL_INSENSITIVE, 
				 		ResultSet.CONCUR_UPDATABLE);
		 
		
		ResultSet rs = pStmt.executeQuery();
		
		System.out.println("ProductDAO:search확인:"+search);
		
	
		
		/* map.put("count", new Integer(total)); */
		 
		/*
		 * rs.absolute(search.getCurrentPage() * page.getPageUnit() - page.getPageUnit()
		 * +1);
		 */
		 
		System.out.println("searchVO.getPage():" +search.getCurrentPage());
	
		
		List<ProductVO> list = new ArrayList<ProductVO>();
		/*
		 * if(totalCount > 0) { for(int i =0; i < page.getPageUnit(); i++) { ProductVO
		 * vo = new ProductVO(); vo.setProdNo(rs.getInt("PROD_NO"));
		 * vo.setProdName(rs.getString("PROD_NAME")); vo.setPrice(rs.getInt("PRICE"));
		 * vo.setRegDate(rs.getDate("REG_DATE"));
		 * 
		 * list.add(vo); if(!rs.next()) break; } }
		 */
		
	

		while (rs.next()) {
			ProductVO productVO = new ProductVO();
			productVO.setProdNo(rs.getInt("PROD_NO"));
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
			list.add(productVO);
			System.out.println("ProductDAO while확인:" +list.size());
			
		}
		
		
		map.put("totalCount", new Integer(totalCount));
		System.out.println("list.size() :" +list.size());
		map.put("list", list);
		System.out.println("map().size() : " +map.size());
		
		con.close();
			
		System.out.println("getProductList 끝====");

		
		return map;
	}
	
	public void insertProduct(ProductVO productVO)throws Exception{
		System.out.println("insertProduct 시작=======");
		Connection con= DBUtil.getConnection();
		
		String sql = "insert into PRODUCT values(seq_product_prod_no.nextval,?,?,?,?,?,sysdate)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		System.out.println("sql시작");
		stmt.setString(1, productVO.getProdName());
		System.out.println("sql이름");
		
		stmt.setString(2, productVO.getProdDetail());
		System.out.println("sql상세");
		
		stmt.setString(3, productVO.getManuDate());
		System.out.println("sql제조일자");
		
		stmt.setInt(4,productVO.getPrice());
		System.out.println("sql가격");
		
		stmt.setString(5, productVO.getFileName());
		System.out.println("sql이미지");
		
		stmt.executeUpdate();
		
		System.out.println("insertProduct sql:"+sql);
		System.out.println("insertProduct 끝=======");
		
		con.close();
	}
	
	public void updateProduct(ProductVO productVO) throws Exception{
		
		Connection con = DBUtil.getConnection();
		System.out.println("updateProduct 시작======");
		String sql = 
				"update PRODUCT set "
						+ "PRODUCT_NAME=?"	
						+ ",PRODUCT_DETAIL=?"
						+ ",MANUFACTURE_DAY=?"
						+ ",PRICE=?"
						+ ",IMAGE_FILE=?"
						+ "where PROD_NO=?";
		
		System.out.println("updateProduct sql:"+sql);
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setString(4, productVO.getFileName());
		stmt.setInt(5, productVO.getProdNo());

		System.out.println("updateProduct 끝======");
		con.close();
	}
	
private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
}
private String makeCurrentPageSql(String sql , Search search){
    sql =    "SELECT * "+ 
             "FROM (      SELECT inner_table. * ,  ROWNUM AS row_seq " +
                         "    FROM (   "+sql+" ) inner_table "+
                         "   WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
             "WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
    
    System.out.println("UserDAO :: make SQL :: "+ sql);   
    
    return sql;
 }

}
