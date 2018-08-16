package com.example.marno.teamspeakapp;

/**
 * Created by Marno on 2017/03/28.
 */

public class Channel {
    private int cid;
    private int pid;
    private String channel_name;
    private int total_clients;

    public Channel() { }

    public Channel(int cid, int pid, String channel_name, int total_clients) {
        this.cid = cid;
        this.pid = pid;
        this.channel_name = channel_name;
        this.total_clients = total_clients;
    }

    public int getCid() {
        return cid;
    }

    public int getPid() {
        return pid;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public int getTotal_clients() {
        return total_clients;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public void setTotal_clients(int total_clients) {
        this.total_clients = total_clients;
    }
}
