package com.example.marno.teamspeakapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.EOFException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ChannelsActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    private JTS3ServerQuery tsQuery;
    private ConnectionParams CParams;
    private List<Channel> channelList = new ArrayList<>();
    private List<Client> clientList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();
    private List<String> chhCheckList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        listView = (ExpandableListView)findViewById(R.id.expChannels);
        iniData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);
    }

    private void iniData()
    {
        CParams = (ConnectionParams)this.getIntent().getSerializableExtra("Query");
        tsQuery = new JTS3ServerQuery();
        try
        {
            tsQuery.connectTS3Query(CParams.getIp(),CParams.getPort());
            tsQuery.selectVirtualServer(1,false,true);
            tsQuery.loginTS3(CParams.getLoginname(),CParams.getLoginpassword());
            tsQuery.setDisplayName("serveradminAndroidChannels");

            Vector<HashMap<String,String>> ChannelInfo = tsQuery.getList(2);
            for (HashMap channel:ChannelInfo)
            {
                channelList.add(new Channel(Integer.parseInt(channel.get("cid").toString()),Integer.parseInt(channel.get("pid").toString()),channel.get("channel_name").toString(),Integer.parseInt(channel.get("total_clients").toString())));
            }

            Vector<HashMap<String,String>> clients = tsQuery.getList(1,"-ip,-times");
            for (HashMap client:clients)
            {
                if (!client.get("client_nickname").toString().contains("serveradmin"))
                {
                    clientList.add(new Client(Integer.parseInt(client.get("client_database_id").toString()),Integer.parseInt(client.get("cid").toString()),Integer.parseInt(client.get("client_type").toString()),Integer.parseInt(client.get("clid").toString()),client.get("client_nickname").toString(),client.get("connection_client_ip").toString(),client.get("client_created").toString(),client.get("client_lastconnected").toString(),client.get("client_idle_time").toString()));
                }
            }
        }
        catch (final EOFException e)
        {
            Toast.makeText(getApplicationContext(),"Connection closed. Maybe banned",Toast.LENGTH_SHORT).show();
        }
        catch (final IllegalStateException e)
        {
            Toast.makeText(getApplicationContext(),"Not connected or already connected",Toast.LENGTH_SHORT).show();
        }
        catch (final TS3ServerQueryException e)
        {
            Toast.makeText(getApplicationContext(),"Error code : " + e.errorID + " - Message : " + e.errorMessage ,Toast.LENGTH_SHORT).show();
        }
        catch (final ConnectException e)
        {
            Toast.makeText(getApplicationContext(),"Could not reach Teamspeak 3 server" ,Toast.LENGTH_SHORT).show();
        }
        catch (final Exception exc)
        {
            Toast.makeText(getApplicationContext(),"Critical Error : " + exc.toString(),Toast.LENGTH_SHORT).show();
        }

        try
        {
            listDataHeader = new ArrayList<>();
            listHash = new HashMap<>();
            int channelcount = 0;
            String[][] channels = new String[channelList.size()][clientList.size()];
            String[] channelNames = new String[channelList.size()];

            for (Channel channel:channelList)
            {
                int clientcount = 0;
                for (Client client:clientList)
                {
                    if (client.getCid() == channel.getCid())
                    {
                        channels[channelcount][clientcount] = client.getClient_nickname();
                        clientcount++;
                    }

                }
                channelNames[channelcount] = channel.getChannel_name();
                channelcount++;
            }


            for (int i = 0; i < channelNames.length ; i++)
            {
                listDataHeader.add(channelNames[i]);
                for (int j = 0; j < channels[i].length ; j++)
                {

                    if (channels[i][j] != null)
                    {
                        nameList = new ArrayList<>();
                        nameList = Arrays.asList(channels[i]).subList(0,channelList.get(i).getTotal_clients());
                        listHash.put(channelNames[i], nameList);
                        chhCheckList.add(channelNames[i]);
                    }
                    else if (chhCheckList.contains(channelNames[i]))
                    {

                    }
                    else
                    {
                        listHash.put(channelNames[i], new ArrayList<String>());
                    }
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Client in Channel error",Toast.LENGTH_SHORT).show();
            finish();
        }



        //listHash.put(channel.getChannel_name(),nameList);



    }

    @Override
    public void onBackPressed()
    {
        tsQuery.closeTS3Connection();
        finish();
    }
}
