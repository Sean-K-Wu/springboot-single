package cn.wuxiangknow.single.api.controller.exception;

import cn.wuxiangknow.single.common.dto.HttpResponseWrapper;
import cn.wuxiangknow.single.common.exception.BusinessException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @Descirption 全局异常处理
 * @Author WuXiang
 */
@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value=Exception.class)
    public HttpResponseWrapper handleException(HttpServletRequest request, Exception exception) throws Exception
    {
        HttpResponseWrapper responseData;
        if(exception instanceof BusinessException){
            BusinessException businessException = (BusinessException) exception;
            log.warn("全局捕获到业务异常:{URL:"+request.getRequestURI()+",PARAMETERS:"+ JSON.toJSONString(request.getParameterMap()+"}"),exception);
            responseData = HttpResponseWrapper.error(businessException);
        }else{
            log.error("全局捕获到意外异常:{URL:"+request.getRequestURI()+",PARAMETERS:"+ JSON.toJSONString(request.getParameterMap()+"}"),exception);
            responseData = HttpResponseWrapper.error();
        }
        return responseData;
    }
}
