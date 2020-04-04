
package cn.wuxiangknow.single.common.exception;



import cn.wuxiangknow.single.common.constant.HttpCodeEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常
 */
@Slf4j
public class BusinessException extends RuntimeException {

	private int code;

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
	}

	public BusinessException(HttpCodeEnum codeEnum) {
		super(codeEnum.getMsg());
		this.code = codeEnum.getCode();
	}

	public BusinessException(HttpCodeEnum codeEnum, Object... args) {
		super(String.format(codeEnum.getMsg(), args));
		this.code = codeEnum.getCode();
	}

	public int getCode() {
		return code;
	}

}
