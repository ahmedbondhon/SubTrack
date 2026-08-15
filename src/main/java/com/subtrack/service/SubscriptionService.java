package com.subtrack.service;

import com.subtrack.entity.BillingCycle;
import com.subtrack.entity.Subscription;
import com.subtrack.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Long id, Subscription updatedSubscription) {
        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + id));

        existingSubscription.setName(updatedSubscription.getName());
        existingSubscription.setCategory(updatedSubscription.getCategory());
        existingSubscription.setPrice(updatedSubscription.getPrice());
        existingSubscription.setBillingCycle(updatedSubscription.getBillingCycle());
        existingSubscription.setRenewalDate(updatedSubscription.getRenewalDate());
        existingSubscription.setActive(updatedSubscription.isActive());

        return subscriptionRepository.save(existingSubscription);
    }

    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public BigDecimal calculateTotalMonthlySpend() {
        return subscriptionRepository.findAll().stream()
                .filter(Subscription::isActive)
                .map(this::convertToMonthlyEquivalent)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalAnnualSpend() {
        return subscriptionRepository.findAll().stream()
                .filter(Subscription::isActive)
                .map(this::convertToAnnualEquivalent)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public List<Subscription> getUpcomingRenewals() {
        LocalDate today = LocalDate.now();
        LocalDate threeDaysFromNow = today.plusDays(3);
        return subscriptionRepository.findUpcomingRenewalsWithinThreeDays(today, threeDaysFromNow);
    }

    private BigDecimal convertToMonthlyEquivalent(Subscription subscription) {
        if (subscription.getPrice() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal quantity = BigDecimal.ONE;
        BigDecimal cost = subscription.getPrice().multiply(quantity);

        return switch (subscription.getBillingCycle()) {
            case WEEKLY -> cost.multiply(BigDecimal.valueOf(4.33));
            case MONTHLY -> cost;
            case ANNUAL -> cost.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        };
    }

    private BigDecimal convertToAnnualEquivalent(Subscription subscription) {
        if (subscription.getPrice() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal quantity = BigDecimal.ONE;
        BigDecimal cost = subscription.getPrice().multiply(quantity);

        return switch (subscription.getBillingCycle()) {
            case WEEKLY -> cost.multiply(BigDecimal.valueOf(52));
            case MONTHLY -> cost.multiply(BigDecimal.valueOf(12));
            case ANNUAL -> cost;
        };
    }
}
