package com.example.marno.teamspeakapp;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.example.marno.teamspeakapp.JTS3ServerQuery;
import com.example.marno.teamspeakapp.TS3ServerQueryException;
import com.example.marno.teamspeakapp.TeamspeakActionListener;

public class JTS3ServerQueryExample implements TeamspeakActionListener
{
	JTS3ServerQuery query;
	boolean debug = false; // Set to true if you want to see all key / values
	
	public static void main(String[] args)
	throws Exception
	{
		JTS3ServerQueryExample jts3 = new JTS3ServerQueryExample();
		
		jts3.runServerMod();
	}
	
	/*
	 * Just output all key / value pairs
	 */
	void outputHashMap(HashMap<String, String> hm, PrintStream stream)
	{
		if (hm == null)
		{
			return;
		}
		
	    Collection<String> cValue = hm.values();
	    Collection<String> cKey = hm.keySet();
	    Iterator<String> itrValue = cValue.iterator();
	    Iterator<String> itrKey = cKey.iterator();
		
		while (itrValue.hasNext() && itrKey.hasNext())
		{
			stream.println(itrKey.next() + ": " + itrValue.next());
		}
	}
	
	public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo)
	{
		if (debug) System.out.println(eventType + " received");
		if (debug) outputHashMap(eventInfo, System.out);
		
		if (eventType.equals("notifytextmessage"))
		{
			if (eventInfo.get("msg").equalsIgnoreCase("!quitbot")) // Quit this program
			{
				try
				{
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Bye Bye, my master!");
					query.removeTeamspeakActionListener();
					query.closeTS3Connection();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					}
					catch (Exception e2) { /* do nothing */ }
				}
				System.exit(0);
			}
			else if (eventInfo.get("msg").equalsIgnoreCase("!clientlist")) // Client List
			{
				try
				{
					Vector<HashMap<String, String>> dataClientList = query.getList(JTS3ServerQuery.LISTMODE_CLIENTLIST, "-info,-times");
					StringBuffer sb = new StringBuffer();
					for (HashMap<String, String> hashMap : dataClientList)
					{
						if (debug) outputHashMap(hashMap, System.out);
						if (sb.length() > 0)
						{
							sb.append(", ");
						}
						sb.append(hashMap.get("client_nickname"));
					}
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Client List (only client names displayed): " + sb.toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					}
					catch (Exception e2) { /* do nothing */ }
				}
			}
			else if (eventInfo.get("msg").equalsIgnoreCase("!channellist")) // Channel List
			{
				try
				{
					Vector<HashMap<String, String>> dataChannelList = query.getList(JTS3ServerQuery.LISTMODE_CHANNELLIST);
					StringBuffer sb = new StringBuffer();
					for (HashMap<String, String> hashMap : dataChannelList)
					{
						if (debug) outputHashMap(hashMap, System.out);
						if (sb.length() > 0)
						{
							sb.append(", ");
						}
						sb.append(hashMap.get("channel_name"));
					}
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Channel List (only channel names displayed): " + sb.toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					}
					catch (Exception e2) { /* do nothing */ }
				}
			}
			else if (eventInfo.get("msg").equalsIgnoreCase("!logview")) // Channel List
			{
				try
				{
					Vector<HashMap<String, String>> dataLogList = query.getLogEntries(4, false, false, 0);
					StringBuffer sb = new StringBuffer();
					for (HashMap<String, String> hashMap : dataLogList)
					{
						if (debug) outputHashMap(hashMap, System.out);
						if (sb.length() > 0)
						{
							sb.append("\n");
						}
						sb.append(hashMap.get("l"));
					}
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Log entries (last 4 lines):\n" + sb.toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					}
					catch (Exception e2) { /* do nothing */ }
				}
			}
			else if (eventInfo.get("msg").equalsIgnoreCase("!serverinfo")) // Server Info
			{
				try
				{
					HashMap<String, String> dataServerInfo = query.getInfo(JTS3ServerQuery.INFOMODE_SERVERINFO, 0);
					if (debug) outputHashMap(dataServerInfo, System.out);
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Server Info (only server name displayed): " + dataServerInfo.get("virtualserver_name"));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					}
					catch (Exception e2) { /* do nothing */ }
				}
			}
			else if (eventInfo.get("msg").equalsIgnoreCase("!myservergroups")) // Server Groups of own client
			{
				try
				{
					int clientDBID = query.getClientDBIDFromUID(eventInfo.get("invokeruid"));
					Vector<HashMap<String, String>> dataClientServerGroups = query.getList(JTS3ServerQuery.LISTMODE_SERVERGROUPSBYCLIENTID, "cldbid="+Integer.toString(clientDBID));
					StringBuffer sb = new StringBuffer();
					for (HashMap<String, String> hashMap : dataClientServerGroups)
					{
						if (debug) outputHashMap(hashMap, System.out);
						if (sb.length() > 0)
						{
							sb.append(", ");
						}
						sb.append(hashMap.get("sgid"));
					}
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "You are member of the following Server Group IDs: " + sb.toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					try
					{
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					}
					catch (Exception e2) { /* do nothing */ }
				}
			}
		}
	}

	void runServerMod()
	throws Exception
	{
		query = new JTS3ServerQuery();
		
		try
		{
			// Connect to TS3 Server, set your server data here
			query.connectTS3Query("localhost", 10011);
			
			// Login with an server query account. If needed, uncomment next line!
			//query.loginTS3("serveradmin", "password");
			
			// Set our class for receiving events
			query.setTeamspeakActionListener(this);
			
			// Select virtual Server
			query.selectVirtualServer(1);
			
			// Register some events
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0); // Server Chat event
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL, 0);  // Channel Chat event
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0);  // Private Chat event
		}
		catch (TS3ServerQueryException sqe)
		{
			System.err.println("An error occurred while connecting to the TS3 server, stopping now! More details below.");
			
			if (sqe.getFailedPermissionID() >= 0)
			{
				HashMap<String, String> permInfo = null;
				try
				{
					// This needs the permission b_serverinstance_permission_list
					permInfo = query.getPermissionInfo(sqe.getFailedPermissionID());
					System.err.println("Missing permission:");
					outputHashMap(permInfo, System.err);
				}
				catch (Exception e)
				{
					// Ignore this exception to make sure, that a missing b_serverinstance_permission_list don't quit this program. 
				}
			}
			
			throw sqe;
		}
		catch (Exception e)
		{
			System.err.println("An error occurred while connecting to the TS3 server, stopping now! More details below.");
			throw e;
		}
		
		System.out.println("You can now chat with this program, using server chat,");
		System.out.println("channel chat (in default channel) or by private messaging the query connection!");
		System.out.println("Commands are (some might need serveradmin permissions):");
		System.out.println("!channellist");
		System.out.println("!clientlist");
		System.out.println("!logview");
		System.out.println("!myservergroups");
		System.out.println("!serverinfo");
		System.out.println("!quitbot");
		System.out.println();
		
		while(true)
		{
			try
			{
				/*
				 * Make sure that the Java VM don't quit this program.
				 */
				Thread.sleep(100);
			}
			catch (Exception e)
			{
			}
		}
	}
}
