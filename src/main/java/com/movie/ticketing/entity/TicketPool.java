package com.movie.ticketing.entity;

import com.movie.ticketing.configuration.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TicketPool {
    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());
    private int maxCapacity;
    private int totalTickets;

    @Autowired
    public TicketPool(Config config) {

    }

    public void initialize(Config config) {
        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTickets = config.getTotalTickets();

        // Initialize the pool with the minimum between totalTickets and maxCapacity
        int initialTicketCount = Math.min(totalTickets, maxCapacity);
        for (int i = 0; i < initialTicketCount; i++) {
            tickets.add(new Ticket(i + 1, "Ticket-" + (i + 1)));
        }

        // Update the total tickets to reflect the initialized pool
        this.totalTickets -= initialTicketCount;
        System.out.println("Initialized ticket pool with " + tickets.size() + " tickets.");
    }

    public void addTickets(int ticketCount, String vendorName) {
        synchronized (tickets) {
            if (totalTickets <= 0) {
                System.out.println("All tickets are already in the pool. " + vendorName + " has no tickets to add.");
                return;
            }

            int ticketsToAdd = Math.min(ticketCount, Math.min(maxCapacity - tickets.size(), totalTickets));
            for (int i = 0; i < ticketsToAdd; i++) {
                tickets.add(new Ticket(tickets.size() + 1, "Ticket-" + (tickets.size() + 1)));
            }

            totalTickets -= ticketsToAdd;
            System.out.println(vendorName + " added " + ticketsToAdd + " tickets. Current pool size: " + tickets.size() + ", Remaining total tickets: " + totalTickets);
            tickets.notifyAll();
        }
    }

    public boolean purchaseTickets(String customerName, int ticketCount) {
        synchronized (tickets) {
            while (tickets.size() < ticketCount) {
                if (totalTickets <= 0 && tickets.isEmpty()) {
                    System.out.println("No more tickets available for " + customerName);
                    return false;
                }
                try {
                    tickets.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            for (int i = 0; i < ticketCount; i++) {
                Ticket removedTicket = tickets.remove(0);
                System.out.println(customerName + " purchased " + removedTicket);
            }
            System.out.println("Tickets remaining in pool: " + tickets.size());
            tickets.notifyAll();
            return true;
        }
    }

    public int getTotalTickets() {
        synchronized (tickets) {
            return totalTickets;
        }
    }

    public void initialize() {
    }
}
