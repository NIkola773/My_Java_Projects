package com.MyWebApp.Java_Web_App;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
@GetMapping("/adminPanel")
public String adminPanel() {
    return "adminPanel";
}
    
}
