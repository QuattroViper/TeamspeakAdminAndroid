package com.example.marno.teamspeakapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import static android.R.layout.simple_list_item_1;

public class ClientsActivity extends AppCompatActivity {

    JTS3ServerQuery tsQuery;
    ConnectionParams CParams;
    ArrayList<Client> clientList = new ArrayList<Client>();
    ArrayList<String> ClientNames = new ArrayList<String>();
    ListView lstClients;
    int WhichClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        getSupportActionBar().setTitle("Clients");
        getSupportActionBar().setSubtitle("Connected Clients");
        CParams = (ConnectionParams)getIntent().getSerializableExtra("Query");
        lstClients = (ListView)findViewById(R.id.ListViewUsers);


        tsQuery = new JTS3ServerQuery();
        try
        {
            tsQuery.connectTS3Query(CParams.getIp(),CParams.getPort());
            tsQuery.selectVirtualServer(1,false,true);
            tsQuery.loginTS3(CParams.getLoginname(),CParams.getLoginpassword());
            tsQuery.setDisplayName("serveradminAndroidClients");

            Vector<HashMap<String,String>> clients = tsQuery.getList(1,"-ip,-times");
            for (HashMap client:clients)
            {
                if (!client.get("client_nickname").toString().contains("serveradmin"))
                {
                    clientList.add(new Client(Integer.parseInt(client.get("client_database_id").toString()),Integer.parseInt(client.get("cid").toString()),Integer.parseInt(client.get("client_type").toString()),Integer.parseInt(client.get("clid").toString()),client.get("client_nickname").toString(),client.get("connection_client_ip").toString(),client.get("client_created").toString(),client.get("client_lastconnected").toString(),client.get("client_idle_time").toString()));
                    ClientNames.add(client.get("client_nickname").toString());
                }


                //Set set = client.entrySet();
                //Iterator iterator = set.iterator();
                //while(iterator.hasNext())
                //{
                //    Map.Entry mentry = (Map.Entry)iterator.next();
                //    Toast.makeText(getApplicationContext(),mentry.getKey() + "  -  " + mentry.getValue(),Toast.LENGTH_SHORT).show();
                //}

            }
            ArrayAdapter adapter = new ArrayAdapter<String>(ClientsActivity.this,simple_list_item_1,ClientNames);
            lstClients.setAdapter(adapter);

            lstClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Client client = clientList.get(position);
                    Intent intent = new Intent(ClientsActivity.this,ClientDetailActivity.class);
                    intent.putExtra("Query",CParams);
                    intent.putExtra("Client",client);
                    intent.putExtras(intent);
                    startActivity(intent);
                }
            });


            lstClients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    CharSequence options[] = new CharSequence[] { "Poke","Kick","Ban"};
                    WhichClient = position;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ClientsActivity.this);
                    builder.setTitle("Action");
                    builder.setCancelable(true);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which)
                            {
                                case 0:
                                {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClientsActivity.this);
                                    alertDialog.setTitle("POKE");
                                    alertDialog.setMessage("Poke Reason");
                                    alertDialog.setCancelable(true);
                                    final EditText input = new EditText(ClientsActivity.this);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                    input.setLayoutParams(lp);
                                    alertDialog.setView(input);
                                    alertDialog.setPositiveButton("Poke", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                tsQuery.pokeClient(clientList.get(WhichClient).getClid(), input.getText().toString());
                                            } catch (TS3ServerQueryException e) {
                                                Toast.makeText(getApplicationContext(), "Poke Failed. Reason : " + e.errorMessage, Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.show();
                                    //Toast.makeText(getApplicationContext(),"Poked",Toast.LENGTH_SHORT).show();
                                }
                                break;
                                case 1:
                                {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClientsActivity.this);
                                    alertDialog.setTitle("KICK");
                                    alertDialog.setMessage("Kick Reason");
                                    alertDialog.setCancelable(true);
                                    final EditText input = new EditText(ClientsActivity.this);
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                                    input.setLayoutParams(lp);
                                    alertDialog.setView(input);
                                    alertDialog.setPositiveButton("Kick", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try
                                            {
                                                tsQuery.kickClient(clientList.get(WhichClient).getClid(),false,input.getText().toString());
                                            }
                                            catch (TS3ServerQueryException e)
                                            {
                                                Toast.makeText(getApplicationContext(),"Kick Failed. Reason : "+ e.errorMessage,Toast.LENGTH_SHORT).show();
                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.show();
                                }
                                //Toast.makeText(getApplicationContext(),"Kicked",Toast.LENGTH_SHORT).show();
                                break;
                                case 2:
                                {
                                    Toast.makeText(getApplicationContext(),"The Ban feature is not yet implemented",Toast.LENGTH_LONG).show();
                                }

                                break;
                            }
                        }
                    });
                    builder.show();
                    return false;
                }
            });


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
