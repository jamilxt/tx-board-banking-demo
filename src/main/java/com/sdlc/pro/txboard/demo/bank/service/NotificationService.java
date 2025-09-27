package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // Non-transactional service to show mixed transactional/non-transactional calls
    public void sendTransferNotification(Transfer transfer) {
        // Simulate sending notification (email, SMS, etc.)
        // This is non-transactional and won't appear in the transaction tree
        System.out.println("Notification sent for transfer: " + transfer.getId());
    }
}
