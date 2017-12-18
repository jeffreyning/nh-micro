package com.nh.micro.nhs;


/**
 * 
 * @author ninghao
 *
 */
public interface ILoadHandler {
	public boolean check(String name, String content);
	public String parse(String name, String content) throws Exception;
}
