package com.movie.ticketing.configuration;

import lombok.Data;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;

@Data
@NonNull
@Configuration
public class Config {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
}
