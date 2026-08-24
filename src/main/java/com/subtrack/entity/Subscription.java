package com.subtrack.entity;

import com.subtrack.entity.BillingCycle;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    private BillingCycle billingCycle = BillingCycle.MONTHLY;

    @Column(name = "renewal_date", nullable = false)
    private LocalDate renewalDate;

    @Column(nullable = false)
    private boolean active = true;

    public Subscription() {
    }

    public Subscription(Long id, String name, String category, BigDecimal price,
                        BillingCycle billingCycle, LocalDate renewalDate, boolean active) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.billingCycle = billingCycle;
        this.renewalDate = renewalDate;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BillingCycle getBillingCycle() { return billingCycle; }
    public void setBillingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; }
    public LocalDate getRenewalDate() { return renewalDate; }
    public void setRenewalDate(LocalDate renewalDate) { this.renewalDate = renewalDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
