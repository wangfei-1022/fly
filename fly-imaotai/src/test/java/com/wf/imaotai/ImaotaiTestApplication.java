package com.wf.imaotai;

import com.wf.imaotai.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImaotaiTestApplication {

    @Autowired
    public ItemService itemService;
    @Test
    public void test(){
       String mtSessionId = itemService.getCurrentSessionId();
       System.out.println(mtSessionId);
    }
}
