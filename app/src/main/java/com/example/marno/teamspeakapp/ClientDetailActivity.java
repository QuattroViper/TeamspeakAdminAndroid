package com.example.marno.teamspeakapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.TextView;
import android.widget.Toast;

import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.TimeUnit;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ClientDetailActivity extends AppCompatActivity {

    JTS3ServerQuery tsQuery;
    ConnectionParams CParams;
    Client client;
    TextView lblMic;
    TextView lblSpeaker;
    TextView lblCreated;
    TextView lblIdle;
    TextView lblConnected;
    TextView lblChannel;
    TextView lblIPAdress;
    TextView lblMBSent;
    TextView lblMBReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);
        lblMic = (TextView)findViewById(R.id.TextViewMicMuted);
        lblSpeaker =(TextView)findViewById(R.id.TextViewSpeakerMuted);
        lblCreated = (TextView)findViewById(R.id.TextViewCreated);
        lblIdle = (TextView)findViewById(R.id.TextViewIdle);
        lblConnected = (TextView)findViewById(R.id.TextViewConncted);
        lblChannel = (TextView)findViewById(R.id.TextViewChannel);
        lblIPAdress = (TextView)findViewById(R.id.TextViewIP);
        lblMBSent = (TextView)findViewById(R.id.TextViewSent);
        lblMBReceived = (TextView)findViewById(R.id.textViewReceived);


        CParams = (ConnectionParams)getIntent().getSerializableExtra("Query");
        client = (Client)getIntent().getSerializableExtra("Client");
        tsQuery = new JTS3ServerQuery();
        getSupportActionBar().setTitle("Client");
        getSupportActionBar().setSubtitle(client.getClient_nickname());
        String MicMuted ="";
        String SpeakerMuted ="";
        String DateCreated ="";
        String TimeInIdle ="";
        String TimeConnected ="";
        String Channel ="";
        String IPAddress ="";
        String MBSent ="";
        String MBReceived ="";


        try
        {
            tsQuery.connectTS3Query(CParams.getIp(),CParams.getPort());
            tsQuery.selectVirtualServer(1,false,true);
            tsQuery.loginTS3(CParams.getLoginname(),CParams.getLoginpassword());
            tsQuery.setDisplayName("serveradminAndroidClient");

            HashMap<String,String> ClientInfo = tsQuery.getInfo(13,client.getClid());
            if (ClientInfo.get("client_input_muted").equals("0")) MicMuted = "Mic not Muted"; else MicMuted = "Mic Muted";
            if (ClientInfo.get("client_output_muted").equals("0")) SpeakerMuted = "Speaker not Muted"; else SpeakerMuted = "Speaker Muted";
            if (Long.parseLong(ClientInfo.get("client_created")) > 0)
            {
                //PrettyTime p = new PrettyTime();
                //long daysago = System.currentTimeMillis() - (Long.parseLong(ClientInfo.get("client_created")));
                //Date da = new Date(daysago);
                //DateCreated = p.format(da);
                DateCreated = String.valueOf((((Long.parseLong(ClientInfo.get("client_created")) /1000) /60) /60) /24) + " Days Ago";
                //Toast.makeText(getApplicationContext(),DateCreated,Toast.LENGTH_LONG).show();
            }
            if (Long.parseLong(ClientInfo.get("client_idle_time")) > 0)
            {
                if (((Long.parseLong(ClientInfo.get("client_idle_time")))/60000)  < 60)
                {
                    TimeInIdle = String.valueOf((Long.parseLong(ClientInfo.get("client_idle_time")))/60000) + " Minutes";
                }
                else if (((Long.parseLong(ClientInfo.get("client_idle_time")))/36000000) < 60)
                {
                    TimeInIdle = String.valueOf((Long.parseLong(ClientInfo.get("client_idle_time")))/36000000) + " Hours";
                }

            }
            if (Long.parseLong(ClientInfo.get("connection_connected_time")) > 0)
            {
                if ((((Long.parseLong(ClientInfo.get("connection_connected_time")))/1000) / 60) < 60)
                {
                    TimeConnected = String.valueOf(((Long.parseLong(ClientInfo.get("connection_connected_time")))/1000) / 60) + " Minutes";
                }
                else if (((((Long.parseLong(ClientInfo.get("connection_connected_time")))/1000) / 60) / 60) < 60)
                {
                    TimeConnected = String.valueOf((((Long.parseLong(ClientInfo.get("connection_connected_time")))/1000) / 60) / 60) + " Hours";
                }

            }
            HashMap<String,String> ChannelInfo =  tsQuery.getInfo(12,Integer.parseInt(ClientInfo.get("client_channel_group_inherited_channel_id").toString()));
            Channel = ChannelInfo.get("channel_name");
            IPAddress = ClientInfo.get("connection_client_ip");
            MBSent = String.valueOf(Long.parseLong(ClientInfo.get("connection_bytes_sent_total"))/ 1048576) + " MB ";
            MBReceived = String.valueOf(Long.parseLong(ClientInfo.get("connection_bytes_received_total"))/ 1048576) + " MB ";

            lblMic.setText(MicMuted);
            lblSpeaker.setText(SpeakerMuted);
            lblCreated.setText(DateCreated);
            lblIdle.setText(TimeInIdle);
            lblConnected.setText(TimeConnected);
            lblChannel.setText(Channel);
            lblIPAdress.setText(IPAddress);
            lblMBSent.setText(MBSent);
            lblMBReceived.setText(MBReceived);


            //Set set = ChannelInfo.entrySet();
            //Iterator iterator = set.iterator();
            //while(iterator.hasNext())
            //{
            //    Map.Entry mentry = (Map.Entry)iterator.next();
            //    Toast.makeText(getApplicationContext(),mentry.getKey() + "  -  " + mentry.getValue(),Toast.LENGTH_SHORT).show();
            //}

        }
        catch (final IllegalStateException e)
        {
            Toast.makeText(getApplicationContext(),"Not connected or already connected",Toast.LENGTH_SHORT).show();
        }
        catch (final TS3ServerQueryException e)
        {
            Toast.makeText(getApplicationContext(),"Error code : " + e.errorID + " - Message : " + e.errorMessage ,Toast.LENGTH_SHORT).show();
        }
        catch (final Exception exc)
        {
            Toast.makeText(getApplicationContext(),"Critical Error : " + exc.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        tsQuery.closeTS3Connection();
        finish();
    }
}
