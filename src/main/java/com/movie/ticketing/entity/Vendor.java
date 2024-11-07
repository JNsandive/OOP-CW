package com.movie.ticketing.entity;

import com.movie.ticketing.configuration.Config;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Data
@Service
public class Vendor extends User implements Runnable {
    private String companyId;
    private int releaseRate;
    private TicketPool ticketPool;
    private volatile boolean continueRunning = true;

    // No-argument constructor for Spring
    public Vendor() {}

    // Constructor for manual instantiation with parameters
    public Vendor(String id, String NIC, String firstName, String lastName, String email, String phoneNumber, String companyId, TicketPool ticketPool, Config config) {
        super(id, NIC, firstName, lastName, email, phoneNumber);
        this.companyId = companyId;
        this.releaseRate = config.getTicketReleaseRate();
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (continueRunning) {
            synchronized (ticketPool) {
                if (ticketPool.getTotalTickets() <= 0) {
                    System.out.println("All tickets are already loaded or sold out. " + this.firstName + " " + this.lastName + " will stop adding tickets.");
                    break;
                }
            }
            ticketPool.addTickets(releaseRate, this.firstName + " " + this.lastName);
            try {
                Thread.sleep(5000); // Simulate the delay between ticket additions
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public boolean validateUser() {
        boolean isValid = true;
        isValid &= validatePhoneNumber();
        isValid &= validateNIC();
        isValid &= validateEmail();

        if (companyId == null || !companyId.matches("CG\\d+")) {
            logger.warning("Invalid company ID for vendor: " + companyId);
            isValid = false;
        }
        if (releaseRate <= 0) {
            logger.warning("Invalid release rate: " + releaseRate);
            isValid = false;
        }
        return isValid;
    }

    public void stop() {
        continueRunning = false;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "id='" + id + '\'' +
                ", NIC='" + NIC + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", companyId='" + companyId + '\'' +
                ", releaseRate=" + releaseRate +
                '}';
    }
}
