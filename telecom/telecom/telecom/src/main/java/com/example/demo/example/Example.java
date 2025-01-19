package com.example.demo.example;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Example {

    // Find your Account Sid and Token at console.twilio.com
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Call call = Call.fetcher("CA42ed11f93dc08b952027ffbc406d0868").fetch();

        System.out.println(call.getTo());
    }
}
