package br.radixeng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PagesController {

    @RequestMapping("/distanciaMinima")
    public String distanciaMinima() {
        return "distanciaMinima";
    }

    @RequestMapping("/")
    public String index() {
        return "redirect:/distanciaMinima";
    }
}