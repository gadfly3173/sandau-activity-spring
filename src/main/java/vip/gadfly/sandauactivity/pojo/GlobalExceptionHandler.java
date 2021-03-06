package vip.gadfly.sandauactivity.pojo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public GlobalJSONResult defaultExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("未知异常：", e);
        return GlobalJSONResult.errorException("未知异常！请检查提交参数是否正确！");
    }
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public GlobalJSONResult httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("请求方法不支持：", e);
        return GlobalJSONResult.errorException("请求方法不支持！请检查提交参数是否正确！");
    }
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public GlobalJSONResult httpMessageNotReadableExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("参数无法解析：", e);
        return GlobalJSONResult.errorException("参数无法解析！请检查提交参数是否正确！" + getExMsg(e.getMessage()));
    }
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public GlobalJSONResult methodArgumentTypeMismatchExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("参数无法解析：", e);
        return GlobalJSONResult.errorException("参数无法解析！请检查提交参数是否正确！" + getExMsg(e.getMessage()));
    }
    @ExceptionHandler(value = RuntimeException.class)
    public GlobalJSONResult runtimeExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("运行时错误：", e);
        return GlobalJSONResult.errorException("运行时错误：" + e.getMessage().substring(0, e.getMessage().indexOf(";")));
    }
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public GlobalJSONResult missingServletRequestParameterExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("请求参数缺失：", e);
        return GlobalJSONResult.errorException("请求参数缺失！请检查提交参数是否正确！" + getExMsg(e.getMessage()));
    }
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public GlobalJSONResult httpMediaTypeNotSupportedExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("请求参数格式不支持：", e);
        return GlobalJSONResult.errorException("请求参数格式不支持！请检查提交参数是否正确！");
    }
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public GlobalJSONResult maxUploadSizeExceededExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        logger.error("文件过大：", e);
        return GlobalJSONResult.errorException("上传文件过大！只支持 4MB 以下的文件！");
    }

    private String getExMsg(String exMsg) {
        return StringUtils.contains(exMsg, ";") ? exMsg.substring(0, exMsg.indexOf(";")) : exMsg;
    }
}
