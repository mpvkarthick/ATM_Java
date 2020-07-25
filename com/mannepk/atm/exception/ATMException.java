/**
 * 
 */
package com.mannepk.atm.exception;

/**
 * @author Karthik Mannepalli
 *
 */
public class ATMException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int errorCode;
	
	protected String reason;
	
	
	public ATMException() {
		
	}
	public ATMException(int errorCode, String reason) {
		this.errorCode = errorCode;
		this.reason = reason;
	}
	
	public ATMException(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	

}
