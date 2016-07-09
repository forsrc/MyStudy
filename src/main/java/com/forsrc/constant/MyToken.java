package com.forsrc.constant;


import com.forsrc.utils.MyAesUtils;
import com.forsrc.utils.MyDesUtils;
import com.forsrc.utils.MyRsaUtils;

import java.util.Date;
import java.util.UUID;

public class MyToken {
    private String loginToken;
    private long loginTokenTime = -1;
    private String token;
    private long tokenTime = -1;
    private MyAesUtils.AesKey aesKey;
    private MyDesUtils.DesKey desKey;
    private MyRsaUtils.RsaKey rsaKey4Client;
    private MyRsaUtils.RsaKey rsaKey4Server;

    public MyToken(){
        generate();
    }

    public void generate(){
        this.token = UUID.randomUUID().toString();
        this.tokenTime = new Date().getTime();
        this.aesKey = new MyAesUtils.AesKey();
        this.desKey = new MyDesUtils.DesKey();
        //this.rsaKey4Client = new MyRsaUtils.RsaKey();
        this.rsaKey4Server = new MyRsaUtils.RsaKey();
        this.loginToken = UUID.randomUUID().toString();
        this.loginTokenTime = new Date().getTime();
    }

    public String getLoginToken() {
        return loginToken;
    }

    public long getLoginTokenTime() {
        return loginTokenTime;
    }

    public String getToken() {
        return token;
    }

    public long getTokenTime() {
        return tokenTime;
    }

    public MyAesUtils.AesKey getAesKey() {
        return aesKey;
    }

    public MyDesUtils.DesKey getDesKey() {
        return desKey;
    }

    public MyRsaUtils.RsaKey getRsaKey4Server() {
        return rsaKey4Server;
    }

    public void setRsaKey4Server(MyRsaUtils.RsaKey rsaKey4Server) {
        this.rsaKey4Server = rsaKey4Server;
    }

    public MyRsaUtils.RsaKey getRsaKey4Client() {
        return rsaKey4Client;
    }

    public void setRsaKey4Client(MyRsaUtils.RsaKey rsaKey4Client) {
        this.rsaKey4Client = rsaKey4Client;
    }
}
