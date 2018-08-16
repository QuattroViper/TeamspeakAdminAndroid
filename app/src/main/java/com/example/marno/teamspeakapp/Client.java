package com.example.marno.teamspeakapp;

import java.io.Serializable;

/**
 * Created by Marno on 2017/03/27.
 */

public class Client implements Serializable {

    private int client_database_id;
    private int cid;
    private int client_type;
    private int clid;
    private String client_nickname;
    private String connection_client_ip;
    private String client_created;
    private String client_lastconnected;
    private String client_idle_time;

    public Client() {
    }

    public Client(int client_database_id, int cid, int client_type, int clid, String client_nickname, String connection_client_ip, String client_created, String client_lastconnected, String client_idle_time) {
        this.client_database_id = client_database_id;
        this.cid = cid;
        this.client_type = client_type;
        this.clid = clid;
        this.client_nickname = client_nickname;
        this.connection_client_ip = connection_client_ip;
        this.client_created = client_created;
        this.client_lastconnected = client_lastconnected;
        this.client_idle_time = client_idle_time;
    }

    public int getClient_database_id() {
        return client_database_id;
    }

    public int getCid() {
        return cid;
    }

    public int getClient_type() {
        return client_type;
    }

    public int getClid() {
        return clid;
    }

    public String getClient_nickname() {
        return client_nickname;
    }

    public String getConnection_client_ip() {
        return connection_client_ip;
    }

    public String getClient_created() {
        return client_created;
    }

    public String getClient_lastconnected() {
        return client_lastconnected;
    }

    public String getClient_idle_time() {
        return client_idle_time;
    }

    public void setClient_created(String client_created) {
        this.client_created = client_created;
    }

    public void setClient_lastconnected(String client_lastconnected) {
        this.client_lastconnected = client_lastconnected;
    }

    public void setClient_idle_time(String client_idle_time) {
        this.client_idle_time = client_idle_time;
    }

    public void setClient_database_id(int client_database_id) {
        this.client_database_id = client_database_id;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setClient_type(int client_type) {
        this.client_type = client_type;
    }

    public void setClid(int clid) {
        this.clid = clid;
    }

    public void setClient_nickname(String client_nickname) {
        this.client_nickname = client_nickname;
    }

    public void setConnection_client_ip(String connection_client_ip) {
        this.connection_client_ip = connection_client_ip;
    }
}
