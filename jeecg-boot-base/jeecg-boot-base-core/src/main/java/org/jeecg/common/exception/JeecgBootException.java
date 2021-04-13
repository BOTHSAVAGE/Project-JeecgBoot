package org.jeecg.common.exception;

/**
 * RuntimeException运行时异常，这种异常我们不需要处理，完全由虚拟机接管
 * Exception必须处理
 */
public class JeecgBootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JeecgBootException(String message){
		super(message);
	}
	
	public JeecgBootException(Throwable cause)
	{
		super(cause);
	}
	
	public JeecgBootException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
