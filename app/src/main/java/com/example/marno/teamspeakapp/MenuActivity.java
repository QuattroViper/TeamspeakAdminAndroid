package com.example.marno.teamspeakapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    Button btnConnectedClients;
    Button btnChannels;
    Button btnSettings;
    Button btnBans;
    Button btnPermissions;
    ConnectionParams CParams;
    String ServerName;
    String ConnectedClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnConnectedClients = (Button)findViewById(R.id.buttonConnectedClients);
        btnChannels = (Button)findViewById(R.id.buttonChannels);
        btnSettings = (Button)findViewById(R.id.buttonServerSettings);
        btnBans = (Button)findViewById(R.id.buttonBans);
        btnPermissions = (Button)findViewById(R.id.buttonPerms);

        CParams = (ConnectionParams)this.getIntent().getSerializableExtra("Query");
        ServerName = (String)this.getIntent().getSerializableExtra("Name");
        ConnectedClients = (String)this.getIntent().getSerializableExtra("OnlineClients");
        getSupportActionBar().setTitle(ServerName);
        getSupportActionBar().setSubtitle("Connected Clients = " + ConnectedClients);

        btnConnectedClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,ClientsActivity.class);
                intent.putExtra("Query",CParams);
                intent.putExtras(intent);
                startActivity(intent);
            }
        });

        btnChannels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,ChannelsActivity.class);
                intent.putExtra("Query",CParams);
                intent.putExtras(intent);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Not yet implemented",Toast.LENGTH_SHORT).show();
            }
        });

        btnPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Not yet implemented",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
