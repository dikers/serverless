package example.vo;

/**
 * @author dikers
 * @date 2019-03-29
 * 用来返回查询的对象
 */
public class ProductVo {

    /**
     * 商品id
     */
    Integer id;

    /**
     * 商品的名称
     */
    String title;


    /**
     * 无参数的构造函数
     */
    public ProductVo(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
