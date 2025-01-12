package com.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/market")
public class MarketController {

    @GetMapping("/main")
    public String marketMain(Model model) {
        model.addAttribute("content", "main");
        return "content/market/marketMain"; // Points to the "content/market/marketMain" template
    }

    @GetMapping("/list")
    public String marketList(Model model) {
        model.addAttribute("content", "list");
        return "content/market/marketList"; // Points to the "content/market/marketList" template
    }

    @GetMapping("/search")
    public String marketSearch(Model model) {
        model.addAttribute("content", "search");
        return "content/market/marketSearch"; // Points to the "content/market/marketSearch" template
    }

    @GetMapping("/view")
    public String marketView(Model model) {
        model.addAttribute("content", "view");


        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }

    @GetMapping("/cart")
    public String marketCart(Model model) {
        model.addAttribute("content", "cart");
        return "content/market/marketcart"; // Points to the "content/market/marketcart" template
    }


    @GetMapping("/order")
    public String marketOrder(Model model) {
        model.addAttribute("content", "order");
        return "content/market/marketorder"; // Points to the "content/market/marketorder" template
    }

    @GetMapping("/completed")
    public String marketOrderCompleted(Model model) {
        model.addAttribute("content", "completed");
        return "content/market/marketorderCompleted"; // Points to the "content/market/marketorderCompleted" template
    }

}
