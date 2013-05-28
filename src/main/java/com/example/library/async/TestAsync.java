package com.example.library.async;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TestAsync {
    
    
    final Logger logger = LoggerFactory.getLogger(TestAsync.class);
    private List<Date> execs = new ArrayList<Date>();
    private int times = 0;
    private Reference reference;
    
    // @Scheduled(fixedRate = 1000) // calledy every day at midnight
    public void send()
    {                
        reference = new Reference(""+times);
        logger.info("TestAsync reference is " + reference);
        logger.info("TestAsync launched at " + ++times + " time");
        logger.info("TestAsync execs before adding " + execs);
        execs.add(new Date());
        logger.info("TestAsync execs after adding " + execs); try
                { 
        toto(reference);
                    logger.info("============> looking for new newsletters at " + new Date());
                    // Thread.sleep(600000); // every 10 minutes
                   
                    Thread.sleep(5000); }
                catch(InterruptedException e)
                {
                    logger.error("An InterruptedException occured on looking for new newsletters to send", e);
                }
                catch(Exception ex)
                {
                    logger.error("An exception occured on looking for new newsletters to send", ex);
                }
    }
    
    private synchronized void toto(Reference r) {
	  logger.info("TestAsync reference from toto is " + r);
	}
    
    private class Reference {
        private String id;
        protected Reference(String id) {
            this.id = id;
        }
        public String toString() {
            return "Reference : "+id;
        }
    }
}