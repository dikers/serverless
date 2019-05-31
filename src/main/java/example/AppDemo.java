package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Date;

/**
 * @author dikers
 * @date 2019-03-29
 * 实现AWS lambda服务的接口
 */

public class AppDemo implements RequestHandler<String, String> {


    /**
     * Lambda 需要实现的接口
     * @param request 请求参数
     * @param context 上下文环境
     * @return
     */
    @Override
    public String handleRequest(String request, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log( "request Data: '"+request  );
        return request + new Date(System.currentTimeMillis()).toString();
    }


}
