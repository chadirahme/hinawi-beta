package common;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;


@ServerEndpoint("/websocket")
public class TestWebSocketServlet
{
	//http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/HomeWebsocket/WebsocketHome.html
	//https://dzone.com/articles/sample-java-web-socket-client
	
		
	private static Set<Session> clients = 
		    Collections.synchronizedSet(new HashSet<Session>());
	
	@OnMessage
	  public void onMessage(String message, Session session) 
	    throws IOException {
	    
	    synchronized(clients){
	      // Iterate over the connected sessions
	      // and broadcast the received message
	      for(Session client : clients){
	    	  client.getRequestParameterMap();
	    	  
	        if (!client.equals(session)){
	        	System.out.println(client.getId() + " send msg");	        
	        	//org.zkoss.zk.ui.Session sess1 = Sessions.getCurrent();
	        	//WebusersModel dbUser=(WebusersModel)sess1.getAttribute("Authentication");
	        	//client.getBasicRemote().se .sendObject(client.getId()+": "+message);
	          
	        	//client.getBasicRemote().sendText(client.getId()+": "+message + " :");
	        	
	          client.getBasicRemote().sendText(message);
	        }
	      }
	    }
	    
	  }	
	
	@OnOpen
	  public void onOpen (Session session) {
		System.out.println("onOpen server");
	  // Add session to the connected sessions set
	    clients.add(session);
	  }

	  @OnClose
	  public void onClose (Session session) {
	    // Remove session from the connected sessions set
	    clients.remove(session);
	  }
	  
	  @OnError
      public void onError(Throwable error)
	  {
		  
	  }
	
}
