package cn.wuxiangknow.single.common.dto;

import cn.wuxiangknow.single.common.constant.HttpCodeEnum;
import cn.wuxiangknow.single.common.exception.BusinessException;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class HttpResponseWrapper<T> implements Serializable {
    private static final long serialVersionUID = -479334985505431036L;



    private int code;
    private String msg;
    private T result;
    public HttpResponseWrapper() {

    }
    private HttpResponseWrapper(int code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    private HttpResponseWrapper(int code, String msg) {
        this(code, msg, null);
    }

    private HttpResponseWrapper(HttpCodeEnum httpCodeEnum) {
        this(httpCodeEnum.getCode(), httpCodeEnum.getMsg());
    }

    private HttpResponseWrapper(HttpCodeEnum httpCodeEnum, T result) {
        this(httpCodeEnum.getCode(), httpCodeEnum.getMsg(),result);
    }

    public static HttpResponseWrapper success(){
        return new HttpResponseWrapper(HttpCodeEnum.GL00000200);
    }

    public static <T> HttpResponseWrapper success(T result){
        return new HttpResponseWrapper<T>(HttpCodeEnum.GL00000200,result);
    }

    public static HttpResponseWrapper error(){
        return new HttpResponseWrapper(HttpCodeEnum.GL00000500);
    }

    public static  HttpResponseWrapper error(HttpCodeEnum httpCodeEnum ){
        return new HttpResponseWrapper<>(httpCodeEnum);
    }
    public static  HttpResponseWrapper error(HttpCodeEnum httpCodeEnum ,Object ... args ){
        return new HttpResponseWrapper<>(httpCodeEnum.getCode(),String.format(httpCodeEnum.getMsg(),args));
    }

    public static  HttpResponseWrapper error(HttpResponseWrapper httpResponseWrapper ){
        return new HttpResponseWrapper(httpResponseWrapper.getCode(),httpResponseWrapper.getMsg());
    }

    public static HttpResponseWrapper error(BusinessException businessException) {
        return new HttpResponseWrapper(businessException.getCode(),businessException.getMessage());
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return HttpCodeEnum.GL00000200.getCode() == this.code;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isUnexpectedError() {
        return !isSuccess() && HttpCodeEnum.GL00000500.getCode() == this.code;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public   T assertSuccessAndGetResult() throws Exception {
        if(isSuccess()){
            return getResult();
        }else{
            throw new BusinessException(this.code, this.msg);
        }
    }

}
