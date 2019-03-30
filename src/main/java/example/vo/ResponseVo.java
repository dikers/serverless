package example.vo;

import example.enums.ReturnMessageEnum;

import java.util.List;

/**
 * @author dikers
 * @date 2019-03-29
 * 用来封装返回的数据对象
 */

public class ResponseVo {


    /**
     * 无参数的构造函数
     */
    public ResponseVo() {
    }

    /**
     * 返回的状态
     */
    private Integer status ;

    /**
     * 返回的信息
     */
    private String  message ;

    /**
     * 返回的产品列表
     */
    private List<ProductVo> productList;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductVo> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductVo> productList) {
        this.productList = productList;
    }

    /**
     * 构造 失败实体
     */
    public static ResponseVo fail(int status, String message) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(status);
        responseVo.setMessage(message);
        return responseVo;
    }


    /**
     * 构造 成功实体
     */
    public static ResponseVo success(List<ProductVo> productList) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus( ReturnMessageEnum.SUCCESS.getCode() );
        responseVo.setMessage( ReturnMessageEnum.SUCCESS.getName() );
        responseVo.setProductList( productList );
        return responseVo;
    }
    /**
     * 构造 成功实体
     */
    public static ResponseVo success() {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus( ReturnMessageEnum.SUCCESS.getCode() );
        responseVo.setMessage( ReturnMessageEnum.SUCCESS.getName() );
        return responseVo;
    }

    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer(  );
        if(productList == null){
            stringBuffer.append( "list is null" );
        }else {
            for(ProductVo productVo: productList){
                stringBuffer.append( "\t[" ).
                        append( productVo.id ).append( " " ).
                        append( productVo.title ).append( "]" );
            }
        }

        return "ResponseVo{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", \nproductList=" + stringBuffer.toString() +
                '}';
    }
}
