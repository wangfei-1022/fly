package com.wf.imaotai;

import com.wf.imaotai.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImaotaiTestApplication {

    @Autowired
    public ShopService shopService;
    @Test
    public void test(){
       String mtSessionId = shopService.getCurrentSessionId();
       System.out.println(mtSessionId);
    }
}
