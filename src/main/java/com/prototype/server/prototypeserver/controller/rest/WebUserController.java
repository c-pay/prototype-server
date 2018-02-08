package com.prototype.server.prototypeserver.controller.rest;

import com.prototype.server.prototypeserver.entity.User;
import com.prototype.server.prototypeserver.service.AdvertService;
import com.prototype.server.prototypeserver.service.MailService;
import com.prototype.server.prototypeserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class WebUserController {

    @Autowired
    MailService mailService;

    @Autowired
    private AdvertService advertService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public String sendMail(@RequestParam("name") String name, @RequestParam("mail") String mail, @RequestParam("tel") String tel, @RequestParam("priority") String priority) {
////        ResponseEntity<String>
//        StringBuilder builder = new StringBuilder();
//        builder.append("<table cellpadding=10 style=\"margin-top:10px; margin-left:10px;\" border=\"1\">");
//        builder.append("<tr><td>Имя</td><td>" + name + "</td></tr>");
//        builder.append("<tr><td >Почта</td><td>" + mail + "</td></tr>");
//        builder.append("<tr><td>Телефон</td><td>" + tel + "</td></tr>");
//        builder.append("<tr><td>Приоритетный способ связи</td><td>" + priority + "</td></tr></table>");
//        mailService.send(name, "info@hashfactory.ru", "Заявка", builder.toString());
//
//        builder = new StringBuilder();
//
//        builder.append("<table cellpadding=15 style='margin-top:10px; margin-left:20px;' border='0'>");
//        builder.append("<tr><td align=center  colspan='2'><a href='www.hashfactory.ru'>logo</a></td></tr>");
//        builder.append("<tr><td align=center ><br/><h2>Приветствуем!</h2>");
//        builder.append("<h3>В ближайшее время мы с вами свяжемся!</h3><br/></td>");
//        builder.append("<td align=center>team2</td></tr>");
//        builder.append("<tr><td></td><td align=right><p>Служба поддежки <a href='mailto:admin@hashactory.ru'>admin@hashactory.ru</a></p>");
//        builder.append("<p>Телефон 8 800 100 5979 (по России бесплатно)</p></td></tr></table>");
//
//        mailService.send(name, mail, "Мы с вами свяжемся!", builder.toString());
//

        return "redirect:/";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration(@RequestParam(required = false, defaultValue = "") String email, @RequestParam(required = false, defaultValue = "false") boolean recovery) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("email",email);
        modelAndView.addObject("recovery",recovery);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registrationCompleted(@RequestParam String email, @RequestParam String password, @RequestParam String password2) {
        ModelAndView modelAndView = new ModelAndView();
        boolean flagError = false;
       if (email.trim().isEmpty()) {
            modelAndView.addObject("error", "Empty email");
            flagError = true;
        } else if( (userService.findUserByEmail(email.trim()))!=null){
            modelAndView.addObject("error", "User mail already exists");
            flagError = true;
        } else if (password.isEmpty()) {
            modelAndView.addObject("error", "Check password");
            modelAndView.addObject("email",email.trim());
            flagError = true;
        } else if (!password.equals(password2)) {
            modelAndView.addObject("error", "Repeat passwords");
            modelAndView.addObject("email",email.trim());
            flagError = true;
        }

        if (!flagError) {
            User user = new User();
            user.setName(email);
            user.setEmail(email);
            user.setPassword(password.trim());
            user.setActive(0);
            userService.saveNewUser(user);
            modelAndView.setViewName("check");

            StringBuilder builder = new StringBuilder();

            builder.append("<table cellpadding=15 style='margin-top:10px; margin-left:20px;' border='0'>");
            builder.append("<tr><td align=center>Thank you for signing up! Confirm your account to get started.</td></tr>");
            builder.append("<tr><td align=center><a href='http://cpay.store/checkemail?email="+email+"'>CONFIRM</a></td></tr></table>");

            mailService.send(email, email, "Registration completed", builder.toString());

            return modelAndView;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ModelAndView check() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("check");
        return modelAndView;
    }

    @RequestMapping(value = "/checkemail", method = RequestMethod.GET)
    public ModelAndView checkEmail(@RequestParam String email) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByEmail(email.trim());
        if(user!=null){
            user.setActive(1);
            userService.saveUser(user);
        }
        modelAndView.setViewName("check_completed");
        return modelAndView;
    }

    @RequestMapping(value = "/recovery", method = RequestMethod.GET)
    public ModelAndView recovery() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("recovery");
        return modelAndView;
    }

    @RequestMapping(value = "/recovery", method = RequestMethod.POST)
    public ModelAndView recoveryCompleted(@RequestParam String email) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByEmail(email.trim());

        if (user != null && user.getActive() != 0) {
            StringBuilder builder = new StringBuilder();

            builder.append("<table cellpadding=15 style='margin-top:10px; margin-left:20px;' border='0'>");
            builder.append("<tr><td align=center ><br/><h2>Восстановление пароля</h2>");
            builder.append("<h3>Вы сделали запрос на восстановление пароля к личному кабинету</h3><br/>");
            builder.append("<h3>Для продолжения процедуры <a href='http://cpay.store/new_password?email=" + email.trim()+"'>Перейти</a></h3><br/>");
            builder.append("<h3>Если вы этого неделали, проигнорируйте письмо</h3><br/></td>");
            builder.append("</tr></table>");

            mailService.send(null, user.getEmail(), "Запрос на восстановление пароля", builder.toString());

            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }


        modelAndView.addObject("error", "Email not found!");

        modelAndView.setViewName("recovery");

        return modelAndView;
    }

    @RequestMapping(value = "/new_password", method = RequestMethod.GET)
    public ModelAndView newPassword(@RequestParam String email) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("email", email);
        modelAndView.setViewName("new_password");
        return modelAndView;
    }

    @RequestMapping(value = "/new_password", method = RequestMethod.POST)
    public ModelAndView newPasswordCompleted(@RequestParam String email, @RequestParam String password, @RequestParam String password2) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByEmail(email.trim());
        boolean flagError = false;
        if (email.trim().isEmpty()) {
            modelAndView.addObject("error", "Empty email");
            flagError = true;
        } else if(user==null){
            modelAndView.addObject("error", "User mail not already exists");
            flagError = true;
        } else if (password.isEmpty()) {
            modelAndView.addObject("error", "Check password");
            modelAndView.addObject("email",email.trim());
            flagError = true;
        } else if (!password.equals(password2)) {
            modelAndView.addObject("error", "Repeat passwords");
            modelAndView.addObject("email",email.trim());
            flagError = true;
        }

        if (!flagError) {
            user.setPassword(password.trim());
            userService.saveNewUser(user);
            modelAndView.setViewName("login");

            return modelAndView;
        }

        modelAndView.addObject("email", email);
        modelAndView.setViewName("new_password");
        return modelAndView;
    }
}
