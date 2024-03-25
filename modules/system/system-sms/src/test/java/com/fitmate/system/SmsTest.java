package com.fitmate.system;

import com.fitmate.system.util.SmsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("sms")
public class SmsTest {

    @Autowired
    private SmsUtil smsUtil;

    @Test
    public void sendOneTest() {

        String to = "01020601122";
        String content = "sms 테스트";

        smsUtil.sendOne(to, content);
    }
}
