package com.movie.ticketing.controller;

import com.movie.ticketing.entity.Consumer;
import com.movie.ticketing.entity.TicketPool;
import com.movie.ticketing.entity.Vendor;
import com.movie.ticketing.configuration.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@RestController
public class TicketingController {

    private static final Logger logger = Logger.getLogger(TicketingController.class.getName());

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private Config config;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private List<Vendor> vendors;
    private List<Consumer> consumers;

    @PostMapping("/start-ticketing-system")
    public String startTicketingSystem() {
        initializeVendorsAndConsumers();

        for (Vendor vendor : vendors) {
            if (vendor.validateUser()) {
                executorService.submit(vendor);
            } else {
                logger.warning("Vendor data invalid for " + vendor.toString());
            }
        }

        for (Consumer consumer : consumers) {
            if (consumer.validateUser()) {
                executorService.submit(consumer);
            } else {
                logger.warning("Consumer data invalid for " + consumer.toString());
            }
        }

        logger.info("Ticketing system started!");
        return "Ticketing system started!";
    }

    private void initializeVendorsAndConsumers() {
        // Create and validate vendors
        vendors = new ArrayList<>();
        vendors.add(new Vendor("V1", "1234567891", "John", "Doe", "john@example.com", "0776078288", "CG123", ticketPool, config));
        vendors.add(new Vendor("V2", "1234567892", "Jane", "Smith", "jane@example.com", "0776078289", "CG456", ticketPool, config));

        // Create and validate consumers
        consumers = new ArrayList<>();
        consumers.add(new Consumer("C1", "1234567893", "Alice", "Johnson", "alice@example.com", "0776078244", "VIP", ticketPool, config));
        consumers.add(new Consumer("C2", "1234567894", "Bob", "Brown", "bob@example.com", "0776078241", "Normal", ticketPool, config));
    }
}
