package example.vo;


/**
 * @author dikers
 * @date 2019-03-29
 * 用来封装查询接口的输入参数
 */

public class RequestVo {

    /**
     * 用户查询的关键字
     */
    private String searchWord;
    /**
     * 是否保存用户查询的关键字
     */
    private boolean addFlag;


    /**
     * 无参数的构造函数
     */
    public RequestVo() {
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public boolean isAddFlag() {
        return addFlag;
    }

    public void setAddFlag(boolean addFlag) {
        this.addFlag = addFlag;
    }
}
