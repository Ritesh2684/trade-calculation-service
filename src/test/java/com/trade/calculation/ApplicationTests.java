package com.trade.calculation;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.trade.calculation.model.TradeEvent;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(value = "classpath:application-test.properties")
public class ApplicationTests {

    @Test
    public void tradeEventTest() {
        
    	TradeEvent tradeEvent = new TradeEvent();
    	
    	assertNotNull(tradeEvent);
    }


}
