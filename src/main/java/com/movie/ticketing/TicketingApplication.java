package com.movie.ticketing;

import com.movie.ticketing.configuration.Config;
import com.movie.ticketing.entity.Consumer;
import com.movie.ticketing.entity.TicketPool;
import com.movie.ticketing.entity.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

@SpringBootApplication
public class TicketingApplication {
    protected static final Logger logger = Logger.getLogger(TicketingApplication.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private TicketPool ticketPool;

    public static void main(String[] args) {
        SpringApplication.run(TicketingApplication.class, args);
    }

    @PostConstruct
    public void runTicketingSystem() {
        Scanner scanner = new Scanner(System.in);

        // Get the total ticket inventory for the concert
        logger.info("Enter total tickets available: ");
        config.setTotalTickets(getValidatedInput(scanner));

        // Set max capacity of tickets that can be held in the ticket pool at once
        logger.info("Enter max capacity in ticket pool: ");
        config.setMaxTicketCapacity(getValidatedInput(scanner));

        // Get release and retrieval rates once for all vendors and customers
        logger.info("Enter ticket release rate (tickets per release) for all vendors: ");
        config.setTicketReleaseRate(getValidatedInput(scanner));

        //
        logger.info("Enter ticket retrieval rate (time in milliseconds between purchases) for all customers: ");
        config.setCustomerRetrievalRate(getValidatedInput(scanner));

        // Initialize the ticket pool with the collected configuration
        ticketPool.initialize(config);


        logger.info("Ticketing system started! Use Ctrl+C to stop.");
    }

    private static int getValidatedInput(Scanner scanner) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input <= 0) {
                    System.out.println("Please enter a positive number:");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number:");
            }
        }
        return input;
    }
}
