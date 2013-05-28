package entity;

import java.util.ArrayList;

/**
 * @author Administrator
 *网页样式表类
 */
public class WebCss {
	private String fileName;//文件名称
	private String webUrl;//CSS的URL地址
	private ArrayList<String> imgsList=new ArrayList<String>();//CSS中的所有图片的URL地址集合
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public ArrayList<String> getImgsList() {
		return imgsList;
	}
	public void setImgsList(ArrayList<String> imgsList) {
		this.imgsList = imgsList;
	}
	/**
	 * @param fileName 文件名称
	 * @param webUrl CSS的URL地址
	 * @param imgsList CSS中的所有图片的URL地址集合
	 */
	public WebCss(String fileName, String webUrl, ArrayList<String> imgsList) {
		super();
		this.fileName = fileName;
		this.webUrl = webUrl;
		this.imgsList = imgsList;
	}
	/**
	 * 
	 */
	public WebCss() {
		super();
	}
	
}
