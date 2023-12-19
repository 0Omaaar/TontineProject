package com.tontine.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tontine")
public class TontineController {

    @GetMapping("/home")
    public void home(){
        System.out.println("welcome home");
    }
}
