package vip.gadfly.sandauactivity.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public GlobalJSONResult defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("未知异常：", e);
        return GlobalJSONResult.errorException("未知异常！请检查提交参数是否正确！");
    }
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public GlobalJSONResult maxUploadSizeExceededExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("文件过大：", e);
        return GlobalJSONResult.errorException("上传文件过大！");
    }
}
