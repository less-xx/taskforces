/**
 * 
 */
package org.teapotech.taskforce.web;

/**
 * @author jiangl
 *
 */
public class RestResponse<T> {

	T body;

	public RestResponse() {
	}

	public RestResponse(T body) {
		this.body = body;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
