package entity;

/**
 * @author 刘秋荣
 * @category网络资源
 *
 */
public class UrlRes {
	private String url;
	private String file;
	private  boolean isok=false;
	/**
	 * @param url
	 * @param file
	 * @param isok
	 */
	public UrlRes(String url, String file, boolean isok) {
		super();
		this.url = url;
		this.file = file;
		this.isok = isok;
	}
	//synchronized 
	public synchronized  boolean isIsok() {
		return isok;
	}
	//synchronized
	public synchronized  void setIsok(boolean isok) {
		this.isok = isok;
	}
	public String getUrl() {
		return url;
	}
	/**
	 * @param url
	 * @param file
	 */
	public UrlRes(String url, String file) {
		super();
		this.url = url;
		this.file = file;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
}
