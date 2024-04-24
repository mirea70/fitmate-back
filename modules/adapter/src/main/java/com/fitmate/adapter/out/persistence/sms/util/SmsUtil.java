package com.fitmate.adapter.out.persistence.sms.util;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SmsUtil {
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${coolsms.url}")
    private String coolsmsUrl;
    @Value("${coolsms.from.num}")
    private String DEFAULT_FROM_NUM;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, coolsmsUrl);
    }

    public SingleMessageSentResponse sendOne(String to, String content) {
        Message messageObj = new Message();
        messageObj.setFrom(DEFAULT_FROM_NUM);
        messageObj.setTo(to);
        messageObj.setText(content);

//        return messageService.sendOne(new SingleMessageSendingRequest(messageObj));
        return null;
    }
}
