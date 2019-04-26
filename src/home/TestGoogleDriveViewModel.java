package home;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import common.GoogleAuthHelper;
import common.GoogleDrive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestGoogleDriveViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	public TestGoogleDriveViewModel()
	{
		try
		{		
		logger.info("inti TestGoogleDriveViewModel >>>> ");
		/*String fileURL="https://drive.google.com/drive/folders/0B3es5IU532J5c0x4clgyNFhPbDg/remote.jpg";
		URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        
        logger.info("responseCode>> " +  responseCode);
        
     // always check HTTP response code first
        if (responseCode == 400) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();
 
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);
        }*/
        
       /* Document doc = Jsoup.connect("https://drive.google.com/drive/folders/0B3es5IU532J5c0x4clgyNFhPbDg").get();
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            System.out.println(link.attr("href") + " - " + link.text());
        }*/
        
		/*final GoogleAuthHelper helper = new GoogleAuthHelper();
		logger.info("loginurl>> " +  helper.buildLoginUrl());
		
		logger.info("getStateToken>> " +  helper.getStateToken());
		
		logger.info("getUserInfoJson>> " +  helper.getUserInfoJson("4"));*/
		
//		helper.getUserInfoJson("");
		
		//GoogleAuthHelper g=new GoogleAuthHelper();
		//g.buildLoginUrl();
		//GoogleDrive g=new GoogleDrive();
		//g.getFiles();
		
		}
		catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			//logger.error(sw.toString());
			logger.info("inti TestGoogleDriveViewModel",ex);
		}
	}
}
