package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import example.constant.AppConstant;
import example.db.DbHelper;
import example.db.ProductDao;
import example.enums.ReturnMessageEnum;
import example.vo.RequestVo;
import example.vo.ResponseVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author dikers
 * @date 2019-03-29
 * 实现AWS lambda服务的接口
 */

public class Application implements RequestHandler<RequestVo, ResponseVo> {


    /**
     * Lambda 需要实现的接口
     * @param request 请求参数
     * @param context 上下文环境
     * @return
     */
    @Override
    public ResponseVo handleRequest(RequestVo request, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log( "SearchWord: '"+request.getSearchWord()+"'\taddFlag:"+request.isAddFlag()+"\t");
//        return doWork( request.getSearchWord(), request.isAddFlag());
        return ResponseVo.success();
    }


    private ResponseVo doWork(String searchWord, boolean addFlag){


        if(searchWord == null || searchWord.length()==0 ){
            return ResponseVo.fail( ReturnMessageEnum.PARAM_NULL.getCode(), ReturnMessageEnum.PARAM_NULL.getName() );
        }else if(searchWord.length() > AppConstant.MAX_LENGTH_PARAM_TITLE){
            return ResponseVo.fail( ReturnMessageEnum.PARAM_ERROR.getCode(),
                    "参数[searchWord]超过最大长度"+ AppConstant.MAX_LENGTH_PARAM_TITLE );
        }

        ProductDao productDao = new ProductDao();
        try {
            if(addFlag){
                //新加商品
                productDao.save( searchWord );

                return ResponseVo.success();
            }else {
                //查询商品列表

                return ResponseVo.success(productDao.getList( searchWord, AppConstant.MAX_COUNT_PRODUCT_SIZE ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseVo.fail( ReturnMessageEnum.SYSTEM_EXCEPTION.getCode(), ReturnMessageEnum.SYSTEM_EXCEPTION.getName() );
        }

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Application application = new Application();
        System.out.println(application.doWork( "鞋子", false ));

    }
}
