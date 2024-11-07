package com.movie.ticketing.entity;

import com.movie.ticketing.configuration.Config;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Data
public class Consumer extends User implements Runnable {
    private final int retrievalRate;
    private final String type;
    private final TicketPool ticketPool;
    private volatile boolean continueRunning = true;


    public Consumer(String id, String NIC, String firstName, String lastName, String email, @NonNull String phoneNumber, String type, TicketPool ticketPool,Config config) {
        super(id, NIC, firstName, lastName, email, phoneNumber);
        this.retrievalRate = config.getCustomerRetrievalRate();
        this.type = type;
        this.ticketPool = ticketPool;
//        this.continueRunning = continueRunning;
    }

    @Autowired // Use this to tell Spring to use this constructor for injection
    public Consumer(TicketPool ticketPool) {
        super("defaultId", "defaultNIC", "defaultFirstName", "defaultLastName", "defaultEmail", "defaultPhoneNumber");
        this.ticketPool = ticketPool;
        this.retrievalRate = 5; // Example default value
        this.type = "Normal"; // Example default value
    }

    @Override
    public boolean validateUser() {
        boolean isValid = true;
        isValid &= validatePhoneNumber();
        isValid &= validateNIC();
        isValid &= validateEmail();

        if (!(type.equals("VIP") || type.equals("Normal"))) {
            logger.warning("Invalid customer type: " + type);
            isValid = false;
        }
        if (retrievalRate <= 0) {
            logger.warning("Invalid retrieval rate: " + retrievalRate);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void run() {
        while (continueRunning) {
            if (!ticketPool.purchaseTickets(this.firstName + " " + this.lastName + " (" + type + ")", retrievalRate)) {
                break;
            }
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        continueRunning = false;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "id='" + id + '\'' +
                ", NIC='" + NIC + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type='" + type + '\'' +
                ", retrievalRate=" + retrievalRate +
                '}';
    }
}
