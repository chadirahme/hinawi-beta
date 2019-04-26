package common;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public final class GoogleAuthHelper {
	//http://www.ocpsoft.org/java/setting-up-google-oauth2-with-java/#section-7
	
	private static final String CLIENT_ID = "363123841814-gc12l78c1f6k26qd9gsj479h99nfb65o.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "9DvzOt3OXqaee0fHlwZYBviD";
	private static final String CALLBACK_URI = "http://localhost:8080/OAuth2v1/index.jsp";
	
	// start google authentication constants
		private static final Collection<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
		private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
		private static final JsonFactory JSON_FACTORY = new JacksonFactory();
		private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		// end google authentication constants
		
		private String stateToken;
		
		private final GoogleAuthorizationCodeFlow flow;
		
		public GoogleAuthHelper() {
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
					JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).build();
			
			generateStateToken();
		}
		
		
		public String buildLoginUrl() {
			
			final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
			
			return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
		}
		
		/**
		 * Generates a secure state token 
		 */
		private void generateStateToken(){
			
			SecureRandom sr1 = new SecureRandom();
			
			stateToken = "google;"+sr1.nextInt();
			
		}
		
		public String getStateToken(){
			return stateToken;
		}
		
		
		public String getUserInfoJson(final String authCode) throws IOException {

			final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
			final Credential credential = flow.createAndStoreCredential(response, null);
			final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
			// Make an authenticated request
			final GenericUrl url = new GenericUrl(USER_INFO_URL);
			final HttpRequest request = requestFactory.buildGetRequest(url);
			request.getHeaders().setContentType("application/json");
			final String jsonIdentity = request.execute().parseAsString();

			return jsonIdentity;

		}
}
