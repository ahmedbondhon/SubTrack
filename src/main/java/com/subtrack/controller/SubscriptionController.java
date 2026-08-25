package com.subtrack.controller;

import com.subtrack.entity.BillingCycle;
import com.subtrack.entity.Subscription;
import com.subtrack.service.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /** Returns true if user is logged in */
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("totalMonthlySpend", subscriptionService.calculateTotalMonthlySpend());
        model.addAttribute("totalAnnualSpend", subscriptionService.calculateTotalAnnualSpend());
        model.addAttribute("subscriptions", subscriptionService.getAllSubscriptions());
        model.addAttribute("upcomingRenewals", subscriptionService.getUpcomingRenewals());
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "index";
    }

    @GetMapping("/subscriptions/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("subscription", new Subscription());
        model.addAttribute("billingCycles", BillingCycle.values());
        return "subscription-form";
    }

    @PostMapping("/subscriptions/save")
    public String saveSubscription(@ModelAttribute Subscription subscription, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        if (subscription.getId() == null) {
            subscriptionService.createSubscription(subscription);
        } else {
            subscriptionService.updateSubscription(subscription.getId(), subscription);
        }
        return "redirect:/";
    }

    @GetMapping("/subscriptions/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Subscription subscription = subscriptionService.getSubscriptionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found with id: " + id));

        model.addAttribute("subscription", subscription);
        model.addAttribute("billingCycles", BillingCycle.values());
        return "subscription-form";
    }

    @GetMapping("/subscriptions/delete/{id}")
    public String deleteSubscription(@PathVariable Long id, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        subscriptionService.deleteSubscription(id);
        return "redirect:/";
    }
}

