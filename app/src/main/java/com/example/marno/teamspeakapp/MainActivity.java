package com.example.marno.teamspeakapp;

import com.example.marno.teamspeakapp.JTS3ServerQuery;
import com.example.marno.teamspeakapp.TS3ServerQueryException;
import com.example.marno.teamspeakapp.TeamspeakActionListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.EOFException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class MainActivity extends AppCompatActivity  {

    EditText edtServerIP;
    EditText edtServerPort;
    EditText edtLoginName;
    EditText edtLoginPassword;
    Button btnConnectToServer;
    Button btnDisconnectFromServer;
    JTS3ServerQuery tsQuery;
    ConnectionParams CParams;

    String ServerClientsOnline="";
    String ServerUptime="";
    String ServerName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Teamspeak Server");
        getSupportActionBar().setSubtitle("Disconnected");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tsQuery = new JTS3ServerQuery();

        edtServerIP = (EditText)findViewById(R.id.editTextServerIP);
        edtServerPort = (EditText)findViewById(R.id.editTextServerPort);
        edtLoginName = (EditText)findViewById(R.id.editTextLoginName);
        edtLoginPassword = (EditText)findViewById(R.id.editTextLoginPassword);
        btnConnectToServer = (Button)findViewById(R.id.buttonConnectToServer);
        btnDisconnectFromServer = (Button)findViewById(R.id.buttonDisconnectFromServer);

        btnConnectToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = false;
                String[] IP = edtServerIP.getText().toString().split("\\.",4);
                if (IP.length == 4)
                {
                    for (String ip:IP) {
                        if (CheckForInt(ip) == true)
                        {
                            correct = true;
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Enter correct IP",Toast.LENGTH_SHORT).show();
                            correct = false;
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter correct IP",Toast.LENGTH_SHORT).show();
                    correct = false;
                }

                if (correct == true)
                {
                    //ConnectionParams params = new ConnectionParams(edtServerIP.getText().toString(),Integer.parseInt(edtServerPort.getText().toString()),"Query");
                    //new Connection().execute(params);

                    try
                    {

                        tsQuery.connectTS3Query(edtServerIP.getText().toString(),Integer.parseInt(edtServerPort.getText().toString()));
                        tsQuery.selectVirtualServer(1,false,true);
                        tsQuery.loginTS3(edtLoginName.getText().toString(),edtLoginPassword.getText().toString());
                        tsQuery.setDisplayName("serveradminAndroid");
                        // getSupportActionBar().setSubtitle("Connected Clients");  --> Number connected Clients en some sorts
                        CParams = new ConnectionParams(edtServerIP.getText().toString(),Integer.parseInt(edtServerPort.getText().toString()),edtLoginName.getText().toString(),edtLoginPassword.getText().toString());
                        Vector<HashMap<String,String>> serverInfo = tsQuery.getList(3);
                        for (HashMap server:serverInfo)
                        {
                            ServerName = server.get("virtualserver_name").toString();
                            ServerClientsOnline = String.valueOf(Integer.parseInt(server.get("virtualserver_clientsonline").toString()) -1);
                            if (((Long.parseLong(server.get("virtualserver_uptime").toString())/60)) < 24)
                            {
                                ServerUptime = String.valueOf(Long.parseLong(server.get("virtualserver_uptime").toString())/60) + " Hours";
                            }
                            else
                            {
                                ServerUptime = String.valueOf((Long.parseLong(server.get("virtualserver_uptime").toString())/60)/24) + " Days";
                            }

                        }
                        getSupportActionBar().setTitle(ServerName);
                        getSupportActionBar().setSubtitle("Clients online = " + ServerClientsOnline);


                        Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                        intent.putExtra("Query",CParams);
                        intent.putExtra("Name",ServerName);
                        intent.putExtra("OnlineClients",ServerClientsOnline);
                        intent.putExtra("ServerUptime",ServerUptime);
                        intent.putExtras(intent);
                        startActivity(intent);


                        btnConnectToServer.setEnabled(false);
                        btnDisconnectFromServer.setEnabled(true);
                        edtServerPort.setEnabled(false);
                        edtServerIP.setEnabled(false);
                        edtLoginPassword.setEnabled(false);
                        edtLoginName.setEnabled(false);

                        //Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
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
                }



            }
        });

        btnDisconnectFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tsQuery.closeTS3Connection();
                btnDisconnectFromServer.setEnabled(false);
                btnConnectToServer.setEnabled(true);
                edtServerPort.setEnabled(true);
                edtServerIP.setEnabled(true);
                edtLoginPassword.setEnabled(true);
                edtLoginName.setEnabled(true);
                getSupportActionBar().setTitle("Teamspeak Server");
                getSupportActionBar().setSubtitle("Disconnected");
            }
        });


    }

    private class Connection extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {

            ConnectionParams connectionDetails = (ConnectionParams)params[0];
            tsQuery = new JTS3ServerQuery();
            try
            {
                tsQuery.connectTS3Query(connectionDetails.getIp(),connectionDetails.getPort());
                tsQuery.loginTS3("serveradmin","EiPTMPVM");
                tsQuery.setDisplayName("QueryAdmin");
                tsQuery.selectVirtualServer(0);

                //Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();
                //return new ConnectionParams("Connected");
                return new Object();
            }
            catch (final Exception exc)
            {
                //Toast.makeText(MainActivity.this,"Not connected",Toast.LENGTH_SHORT).show();
                //return new ConnectionParams("Error : " + exc.toString());
                return new Object();
            }
        }

    }

    private boolean CheckForInt(String string)
    {
        boolean GoAhead = false;

        try
        {
            Integer.parseInt(string);
            GoAhead = true;
        }
        catch (NumberFormatException  e)
        {
            GoAhead = false;
        }

        return GoAhead;
    }
}


