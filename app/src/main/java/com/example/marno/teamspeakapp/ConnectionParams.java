package com.example.marno.teamspeakapp;

import java.io.Serializable;

/**
 * Created by Marno on 2017/03/26.
 */

public class ConnectionParams implements Serializable{
    private String ip;
    private int port;
    private String extra;
    private String reply;
    private String loginname;
    private String loginpassword;

    public ConnectionParams() {
    }

    public ConnectionParams(String reply) {
        this.reply = reply;
    }

    public ConnectionParams(String ip, int port, String loginname, String loginpassword) {
        this.ip = ip;
        this.port = port;
        this.loginname = loginname;
        this.loginpassword = loginpassword;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getExtra() {
        return extra;
    }

    public String getReply() {
        return reply;
    }

    public String getLoginname() {
        return loginname;
    }

    public String getLoginpassword() {
        return loginpassword;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public void setLoginpassword(String loginpassword) {
        this.loginpassword = loginpassword;
    }
}
