package com.example.library.async;

import java.util.Date;

import com.example.library.service.BorrowingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Both annotations, @Service and @Scheduled, are necessary to run this job asynchronously
 */
/**
 * Asynchronous revivals are send every day for delayed borrowings. If a borrowing achieves  
 * critical level revivals (5 by default), a penalty is added to user and a e-mail send to 
 * subscriber.
 * One borrowing can be treated only once a day.
 */
@Service
public class PenaltyCheckerAsync
{
    final Logger logger = LoggerFactory.getLogger(PenaltyCheckerAsync.class);
    @Autowired
    private BorrowingService borrowingService;
    
    @Scheduled(fixedRate = 1800000) // every 30 minutes
    public void lookForDelayed() {
        logger.info("=============> Looking for delayed at " + new Date());
        borrowingService.checkDelayed();
    }
}