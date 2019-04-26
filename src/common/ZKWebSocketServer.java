package common;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

@ClientEndpoint
public class ZKWebSocketServer 
{
	private static Object waitLock = new Object();
	private MessageHandler messageHandler;
	Session userSession=null;
	private String msg;
	 
	public ZKWebSocketServer()
	{
		
	}
	
	public ZKWebSocketServer(final URI endpointURI)
	{
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@OnOpen
	public void onOpen(final Session userSession) {
		this.userSession = userSession;
		 System.out.println("open socket: "+userSession.getId());  
	}
 
	@OnClose
	public void onClose(final Session userSession, final CloseReason reason) {
		this.userSession = null;
	}
	
	
	@OnMessage
	@NotifyChange("msg")
    public void onMessage(String message) {
//the new USD rate arrives from the websocket server side.		
		
		if (messageHandler != null) 
		{										
		messageHandler.handleMessage(message);						
		}			
       System.out.println("Received msg: "+message);  
       msg=message;
    }
				
	public void addMessageHandler(final MessageHandler msgHandler) {
		messageHandler = msgHandler;
	}
	
	public void sendMessage(final String message) 
	{
		org.zkoss.zk.ui.Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
		System.out.println(dbUser.getCompanyName());
		if(dbUser.getCompanyName().equalsIgnoreCase("Explorer"))
			userSession.getAsyncRemote().sendText(message);
		}		
	}
 
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static interface MessageHandler {
		public void handleMessage(String message);
	}
	
	
}
