package cn.wuxiangknow.single.common.constant;

public enum  HttpCodeEnum {

    GL00000100(100, "参数异常"),
    GL00000200(200, "成功"),
    GL00000401(401, "未认证"),
    GL00000403(403, "拒绝访问"),
    GL00000404(404, "找不到指定资源"),
    GL00000500(500, "服务器异常"),

    ;

    private int code;
    private String msg;


    HttpCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static HttpCodeEnum getEnum(int code) {
        for (HttpCodeEnum ele : HttpCodeEnum.values()) {
            if (ele.getCode() == code) {
                return ele;
            }
        }
        return null;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
