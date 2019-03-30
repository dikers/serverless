package example.db;

import example.vo.ProductVo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author dikers
 * @date 2019-03-29
 * 封装数据库操作
 */
public class ProductDao {


    /**
     *
     * @param searchWord
     * @param amount
     * @return
     * @throws SQLException
     */
    public List<ProductVo> getList(String searchWord , int amount) throws SQLException {


        String sql = "select id , title from product where title like  concat( ?,'%') ORDER BY id desc limit  ?  " ;
        PreparedStatement preparedStatement = DbHelper.getInstance().getConnection().prepareStatement( sql );

        preparedStatement.setString( 1, searchWord );
        preparedStatement.setInt( 2, amount );
        ResultSet resultSet = preparedStatement.executeQuery();

        List<ProductVo>  productVoList = new ArrayList<>(  );
        while (resultSet.next()){
            ProductVo productVo = new ProductVo();

            productVo.setId(resultSet.getInt( 1 ));
            productVo.setTitle( resultSet.getString( 2 ) );

            productVoList.add( productVo );
        }


        return productVoList;
    }


    /**
     * 用来保存新的商品信息
     * @param searchWord
     * @return
     * @throws SQLException
     */
    public boolean save(String searchWord) throws SQLException {


        //如果有重复名称的商品，更新内容。
        String sql =  "insert into product (title)  values(?)  on  DUPLICATE key update content=? ";


        PreparedStatement preparedStatement = DbHelper.getInstance().getConnection().prepareStatement( sql );

        //用来模拟更新内容
        String content = "update "+new Date( ).toString() ;
        preparedStatement.setString( 1, searchWord );
        preparedStatement.setString( 2, content );

        return preparedStatement.execute();

    }


}
