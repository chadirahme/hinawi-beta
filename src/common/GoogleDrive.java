package common;

//import com.google.api.client.googleapis.auth.oauth2.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class GoogleDrive {

	private Logger logger = Logger.getLogger(this.getClass());
	//363123841814-gc12l78c1f6k26qd9gsj479h99nfb65o.apps.googleusercontent.com

	//9DvzOt3OXqaee0fHlwZYBviD
	
	// start google authentication constants
		private static final Iterable<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
		private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
		private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
		private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		// end google authentication constants

	
	
	 private  final String APPLICATION_NAME = "ERP Driver";
		        //"Drive API Quickstart";
	 
	//private  final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	 //private  HttpTransport HTTP_TRANSPORT;
	  private  FileDataStoreFactory DATA_STORE_FACTORY;
	  
	  private  final java.io.File DATA_STORE_DIR = new java.io.File(
		        System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");
	  
	  private  final List<String> SCOPES =
		        Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

		     {
		        try {
		            //HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		        } catch (Throwable t) {
		            t.printStackTrace();
		            logger.info("error at SCOPES >> ",t);
		           // System.exit(1);
		        }
		    }
	public GoogleDrive()
	{
		
		
	}
	public  void getFiles() throws IOException
	{
		Drive service = getDriveService();
		
		/*com.google.api.services.drive.Drive.Files.List request = service.files().list().setQ("mimeType = 'application/vnd.google-apps.folder' and title = 'ERPImages852'");
		 if(request!=null)
		 {
			 System.out.println("request1 >>> " + request.size());			
		 }
		 FileList result = service.files().list()
				 .setQ("mimeType='image/jpeg' and '0B3es5IU532J5c0x4clgyNFhPbDg' in parents")
	             .setPageSize(10)
	             .setFields("nextPageToken, files(id, name)")
	             .execute();
		 List<File> files = result.getFiles();
	        if (files == null || files.size() == 0) {
	            System.out.println("No files found.");
	        } else {
	            System.out.println("Files:");
	            for (File file : files) {
	                System.out.printf("%s (%s)\n", file.getName(), file.getId());
	            }
	        }*/
		
	}
	
	 public  Drive getDriveService() throws IOException {
	        Credential credential = authorize();
	        return new Drive.Builder(
	                HTTP_TRANSPORT, JSON_FACTORY, credential)
	                .setApplicationName(APPLICATION_NAME)
	                .build();
	    }

	 
	public  Credential authorize() 
	{
		try
		{
        // Load client secrets.
        InputStream in =
        		//GoogleDrive.class.getClassLoader().getResourceAsStream("resources/client_secret.json");
        		this.getClass().getClassLoader().getResourceAsStream("resources/client_secret.json");
      
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        logger.info("Credentials saved to 1"); 
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
              //  .setAccessType("offline")
                .build();
        
        logger.info("Credentials saved to 2"); 
        
        final LocalServerReceiver receiver = new LocalServerReceiver.Builder().
                setPort(8181).build();
        
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, receiver).authorize("user");
        
        logger.info("Credentials saved to 3"); 
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        logger.info("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
		}
		catch(IOException ex)
		{
			logger.info("error at authorize >> ",ex);
		}
		return null;
    }
}
