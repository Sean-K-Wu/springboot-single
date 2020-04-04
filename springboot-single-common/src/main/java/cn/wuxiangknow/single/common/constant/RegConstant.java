package cn.wuxiangknow.single.common.constant;

/**
 * 正则表达式常量
 */
public class RegConstant {

    /**
     * 整数的正则表达式
     */
    public static final String INTEGER_REGEX ="^[-\\+]?[\\d]*$";
    /**
     * 数值的正则表达式
     */
    public static final String NUMBER_REGEX ="^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?[dD]?[fF]?$";
    /**
     * 手机号码的正则表达式
     */
    public static final String TEL_REGEX ="^1[3|4|5|6|7|8|9][0-9]{9}$";
    /**
     * 邮箱的正则表达式
     */
    public static final String USER_EMAIL_REGEX ="^[0-9A-Za-z-_]+@[[A-Za-z0-9-_]+\\.[A-Za-z-_]+]+$";
    /**
     * 日期的正则表达式
     */
    public static final String DATE_REGEX ="^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$";
    public static final String DATE_YEAR_REGEX ="^[0-9]{4}$";


}
