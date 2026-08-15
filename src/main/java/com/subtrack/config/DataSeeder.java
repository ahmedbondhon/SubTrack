package com.subtrack.config;

import com.subtrack.entity.BillingCycle;
import com.subtrack.entity.Subscription;
import com.subtrack.repository.SubscriptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SubscriptionRepository subscriptionRepository;

    public DataSeeder(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void run(String... args) {
        if (subscriptionRepository.count() > 0) {
            return;
        }

        List<Subscription> sampleSubscriptions = List.of(
                new Subscription(null, "Netflix", "Entertainment", new BigDecimal("15.99"), BillingCycle.MONTHLY, LocalDate.now().plusDays(8), true),
                new Subscription(null, "GitHub", "Development", new BigDecimal("48.00"), BillingCycle.ANNUAL, LocalDate.now().plusDays(12), true),
                new Subscription(null, "AWS", "Cloud", new BigDecimal("29.50"), BillingCycle.MONTHLY, LocalDate.now().plusDays(2), false),
                new Subscription(null, "Spotify", "Music", new BigDecimal("12.99"), BillingCycle.WEEKLY, LocalDate.now().plusDays(5), true)
        );

        subscriptionRepository.saveAll(sampleSubscriptions);
    }
}
