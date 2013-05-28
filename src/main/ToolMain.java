package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.HtmlUtil;
import util.UrlResource;
import entity.UrlRes;
import entity.WebCss;

public class ToolMain extends JFrame {

	private JPanel contentPane;
	private JTextField txtURL;
	private JList jlist;
	private DefaultListModel listModel;
	private JProgressBar pb;
	private Elements imgs;//当前网页的图片
	private String domain;//网站的域名
	private String webPath;//网址的路径
	private ArrayList<WebCss> cssList=new ArrayList<WebCss>();//网站所有CSS的集合
	private Elements js;//网站所有JS的集合
	private ArrayList<UrlRes> allList=new ArrayList<UrlRes>();//所有需要下载的资源集合
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ToolMain frame = new ToolMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ToolMain() {
		setTitle("\u7F51\u9875\u8D44\u6E90\u7BA1\u7406\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 613, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("\u8BF7\u8F93\u5165\u7F51\u9875\u5730\u5740\uFF1A");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(10, 10, 108, 15);
		contentPane.add(label);
		
		txtURL = new JTextField();
		txtURL.setToolTipText("\u5728\u8FD9\u8FB9\u8F93\u5165\u9700\u8981\u4E0B\u8F7D\u7684\u7F51\u9875\u5730\u5740\uFF0C\u53EA\u6709\u57DF\u540D\u65F6\u6700\u540E\u8981\u52A0\u4E0A/\u53F7\uFF0C\u6709\u5177\u4F53\u6587\u4EF6\u5730\u5740\u4E0D\u9700\u8981\u52A0\u3002");
		txtURL.setText("http://www.hua.com/");
		txtURL.setBounds(111, 7, 378, 21);
		contentPane.add(txtURL);
		txtURL.setColumns(10);
		
		JButton button = new JButton("\u5F00\u59CB\u4E0B\u8F7D");
		button.addActionListener(new BtnActionLinstener());
		button.setBounds(499, 5, 95, 25);
		contentPane.add(button);
		
		pb = new JProgressBar();
		pb.setBounds(10, 627, 595, 16);
		contentPane.add(pb);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setBounds(20, 35, 574, 582);
		contentPane.add(scrollPane);
		listModel=new DefaultListModel();
		jlist = new JList(listModel);
		scrollPane.setViewportView(jlist);
	}
	/**
	 * 开始加载解析所有的资源
	 */
	private void starLoad(){
		String path=HtmlUtil.getURLDomain(txtURL.getText(), false);
		path=ToolMain.class.getResource("/").getPath()+path;
		System.out.println(path);
		File file=new File(path);
		if(!file.exists()){
			try {
				file.mkdir();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//先下载当前页面所有的图片
		for(Element ele :imgs){
			allList.add(new UrlRes(getRealPath(ele.attr("src")),path+"/images/" ));
		}
		//再下载当前页的JS
		for(Element ele:js){
			allList.add(new UrlRes(getRealPath(ele.attr("src")),path+"/" ));
		}
		//再下载CSS中的所有图片
		for(WebCss css:cssList){
			allList.add(new UrlRes(css.getWebUrl(),path+"/"));
			for(String str:css.getImgsList()){
				allList.add(new UrlRes(getRealPath(str),path+"/cssimages/" ));
			}
		}
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		allList.clear();
		cssList.clear();
		jlist.removeAll();
		pb.setValue(0);
		pb.setMaximum(0);
	}
	/**
	 * 加载解析
	 */
	private void loadWebHtml(){
		//获取域名
		domain=HtmlUtil.getURLDomain(txtURL.getText(), true);
		//获取网址的路径
		webPath=txtURL.getText().substring(0,txtURL.getText().lastIndexOf("/")+1);
		try {
			//访问URL地址，返回HTML文档
			Document html=Jsoup.connect(txtURL.getText()).get();
			//先筛选出当前页面所有的IMG标签，并获取SRC的路径值
			imgs=html.select("img[src]");		
			//设置进度条的最大数量
			pb.setMaximum(imgs.size()+pb.getMaximum());
			
			//再筛选出当前页面所有的JS
			js=html.select("script[src]");	
			//设置进度条的最大数量
			pb.setMaximum(js.size()+pb.getMaximum());
			
			//再筛选出当前页面的所有的CSS文件
			Elements cssEle=html.select("link[href]").select("[rel=stylesheet]");
			for(Element ele:cssEle){
				//获取href属性的值
				String str=ele.attr("href");
				if(str.indexOf("/")==0){
					str=domain+str;
				}else if(str.indexOf("http")==0){
					
				}else{
					str=webPath+str;
				}
				WebCss css=new WebCss();
				css.setWebUrl(str);				
				//获取CSS的路径
				String cssPath=str.substring(0,str.lastIndexOf("/")+1);
				css.setFileName(str.substring(str.lastIndexOf("/")+1));
				ArrayList<String> cList=new ArrayList<String>();
				//访问CSS，获取CSS源码
				Document doc=Jsoup.connect(str).get();
				//获取CSS源码文本
				str=doc.text();
				//正则表达式
				Pattern p=Pattern.compile("url\\(.+?\\)", Pattern.CASE_INSENSITIVE);
				//筛选
				Matcher m=p.matcher(str);
				//遍历筛选出来的字符串
				while(m.find()){
					String reg=m.group();
					reg=reg.replace("url(", "").replace(")", "");
					if(reg.indexOf("/")==0){
						reg=domain+reg;
					}else if(reg.indexOf("http")==0){
						
					}else{
						reg=cssPath+reg;
					}
					cList.add(reg);
					//System.out.println(reg);
				}
				css.setImgsList(cList);	
				cssList.add(css);
				//设置进度条的最大数量
				pb.setMaximum(cList.size()+pb.getMaximum()+1);
			}
			
			
			
//			pb.setValue(0);
//			pb.setMaximum(imgs.size());
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
	/**
	 * 下载资料
	 */
	private void downloadFile(String url,String filePath){
			
		URI uri;
		try {			
			String file=url.substring(url.lastIndexOf("/")+1);
			File fi=new File(filePath);
			if(!fi.exists()){
				fi.mkdir();
			}
			UrlResource.saveUrlFile(url, filePath+file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * @param str
	 * @return获取路径
	 */
	private String getRealPath(String str){
		if(str.indexOf("/")==0){
			str=domain+str;
		}else if(str.indexOf("http")==0){
			
		}else{
			str=webPath+str;
		}
		return str;
	}
	/**
	 * 解析并下载
	 * @param str
	 * @param path
	 */
	private void encode(String str,String path){
				
		File file=new File(path);
		if(!file.exists()){
			file.mkdir();
		}
		listModel.addElement(str);
		//进入下载
		downloadFile(str, path);
		pb.setValue(pb.getValue()+1);
	}
	private class BtnActionLinstener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			initData();
			loadWebHtml();
			starLoad();
			//开启五个线程下载
			for(int i=0;i<5;i++){
				new Thread(new RunData()).start();
			}
		}
		
	}
	
	private class RunData implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				
				for(UrlRes url:allList){
					if(!url.isIsok()){
						url.setIsok(true);
						encode(url.getUrl(), url.getFile());
						
					}
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
		
	}
}
