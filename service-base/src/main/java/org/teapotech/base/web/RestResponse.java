/**
 * 
 */
package org.teapotech.base.web;

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

	public static <K> RestResponse<K> ok(K body) {
		return new RestResponse<>(body);
	}

}
