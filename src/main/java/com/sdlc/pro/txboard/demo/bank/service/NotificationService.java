package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {

    // Non-transactional service to show mixed transactional/non-transactional calls - Reactive version
    public Mono<Void> sendTransferNotification(Transfer transfer) {
        // Simulate sending notification (email, SMS, etc.)
        // This is non-transactional and won't appear in the transaction tree
        return Mono.fromRunnable(() ->
            System.out.println("Notification sent for transfer: " + transfer.getId())
        );
    }
}
