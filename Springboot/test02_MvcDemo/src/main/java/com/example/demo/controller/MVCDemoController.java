package com.example.demo.controller;


import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MVCDemoController {
    @GetMapping ("/mvcdemo")
    public ModelAndView hello () {
        User user = new User();
        user.setName("zhonghua");
        user.setAge(28);

        //定义MVC中的视图模板
        ModelAndView modelAndView = new ModelAndView("mvcdemo");

        //传递user实体对象给视图
        modelAndView.addObject("user",user);
        return modelAndView;
    }
}
