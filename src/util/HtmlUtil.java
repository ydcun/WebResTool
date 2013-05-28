package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @category HTML开发工具类
 * @author 刘秋荣
 * @version 1.0
 */
public class HtmlUtil {
	/**
	 * @param url 地址
	 * @param hasProtocol 是否需要返回协议，如:http://
	 * @return获取URL地址的域名，如:www.baidu.com
	 */
	public static String getURLDomain(String url,boolean hasProtocol){
		Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(url);
		matcher.find();
		String str=matcher.group();
		return hasProtocol?"http://"+str :str;
	}
}
