package com.github.leialoha.tiltify.requests.exceptions;

public class InvalidTokenException extends Exception {
	
	private static final long serialVersionUID = 5135415736226385031L;
	private final Throwable cause;
 
	public InvalidTokenException(Throwable throwable) {
	   this.cause = throwable;
	}
 
	public InvalidTokenException() {
	   this.cause = null;
	}
 
	public InvalidTokenException(Throwable cause, String message) {
	   super(message);
	   this.cause = cause;
	}
 
	public InvalidTokenException(String message) {
	   super(message);
	   this.cause = null;
	}
 
	public Throwable getCause() {
	   return this.cause;
	}
	
}
