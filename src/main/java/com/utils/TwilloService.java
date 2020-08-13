package com.utils;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class TwilloService {
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    private static Logger logger = LoggerFactory.getLogger(TwilloService.class);
    private static Properties configProperties = null;

    private TwilloService() {
        super();
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("CountryCodes.properties")) {
            if (inputStream != null) {
                configProperties = new Properties();
                configProperties.load(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Properties getConfigProperties() {
        if (configProperties == null)
            new TwilloService();

        return configProperties;
    }

    public static void sendMessage(final String countryCode,final String toNumber,final String txtMessage) {
        if (!Utils.isEmpty(countryCode) && !Utils.isEmpty(toNumber) && !Utils.isEmpty(txtMessage)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
                        // Build the parameters
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("From", "+14085330827"));
                        params.add(new BasicNameValuePair("To", (countryCode + toNumber)));
                        params.add(new BasicNameValuePair("Body", txtMessage.trim()));

                        MessageFactory messageFactory = client.getAccount().getMessageFactory();
                        Message message = messageFactory.create(params);
                        logger.debug("Message successfully sent :" + message.getSid());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }
}
