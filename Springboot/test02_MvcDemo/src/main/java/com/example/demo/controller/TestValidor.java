package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import javax.validation.Valid;

@Controller
public class TestValidor {
    @GetMapping("/test")
    public String showForm (User user) {
        return "form";
    }
    @GetMapping("/results")
    public String results () {
        return "results";
    }

    @PostMapping("/test")
    public String checkUser (@Valid User user, BindingResult bindingResult, RedirectAttributes attr) {

        //特别注意，实体中的属性都必须被验证过，否则不会成功
        if (bindingResult.hasErrors()) {
            return "form";
        }
        attr.addFlashAttribute("user",user);
        return "redirect:/results";
    }
}
