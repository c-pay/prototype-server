package com.prototype.server.prototypeserver.controller.rest;

import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.Item;
import com.prototype.server.prototypeserver.entity.Role;
import com.prototype.server.prototypeserver.entity.User;
import com.prototype.server.prototypeserver.service.AdvertService;
import com.prototype.server.prototypeserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping
@SessionAttributes("user")
public class WebController {


    @Autowired
    private AdvertService advertService;
    @Autowired
    private UserService userService;

    private User getPrincipalUser() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUserByEmail(principal.getUsername());
    }


    @RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") int imageId) throws IOException {
        byte[] imageContent = null;

        imageContent = advertService.findAdvertById(imageId).getPic();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = {"/", "/index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        advertService.findByAdvert(1);
        modelAndView.setViewName("index");
        modelAndView.addObject("adverts", advertService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/download")
    public ModelAndView download() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("download");
        return modelAndView;
    }

    @RequestMapping(value = "/shop/{id}", method = RequestMethod.GET)
    public ModelAndView shop(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView();
        Advert advert = advertService.findAdvertById(id);
        List<Item> items = advertService.findByAdvert(advert.getId());
        modelAndView.addObject("advert", advert);
        modelAndView.addObject("items", items);
        modelAndView.setViewName("shop");
        return modelAndView;
    }


    @RequestMapping(value = "/list_shop", method = RequestMethod.GET)
    public ModelAndView listShop(Authentication authentication) {

        User user = getPrincipalUser();
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        List<Advert> adverts = new ArrayList<>();
        if (admin) {
            adverts = advertService.findAll();
        } else {
            adverts = advertService.findAllByUser(user);
        }
        if (adverts == null) adverts = new ArrayList<>();
        modelAndView.addObject("adverts", adverts);
        modelAndView.setViewName("list_shop");
        return modelAndView;
    }

    @RequestMapping(value = "/edit_shop/{id}", method = RequestMethod.GET)
    public ModelAndView editShop(@SessionAttribute User user, @PathVariable long id) {

        ModelAndView modelAndView = new ModelAndView();
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        Advert advert = advertService.findAdvertById(id);
        if (advert == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        if (!admin && !advert.getUser().getEmail().equals(user.getEmail())) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        List<Item> items = advertService.findByAdvert(advert.getId());
        modelAndView.addObject("advert", advert);
        modelAndView.addObject("items", items);
        modelAndView.setViewName("edit_shop");
        return modelAndView;
    }


    @RequestMapping(value = "/add_shop", method = RequestMethod.GET)
    public ModelAndView addShop() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add_shop");
        return modelAndView;
    }

    @RequestMapping(value = "/add_shop", method = RequestMethod.POST)
    public ModelAndView addShop(@SessionAttribute User user, @RequestParam String title, @RequestParam String description, @RequestParam String wallet
            ,@RequestParam String address, @RequestParam String addaddress, @RequestParam Double latitude, @RequestParam Double longitude) {
        Advert advert = new Advert();
        advert.setTitle(title);
        advert.setDescription(description);
        advert.setWallet(wallet);
        advert.setUser(user);
        advert.setAddress(address);
        advert.setAddAddress(addaddress);
        advert.setLatitude(latitude);
        advert.setLongitude(longitude);
        Advert save = advertService.saveAdvert(advert);
        ModelAndView modelAndView = new ModelAndView();
        if (save != null) {
            modelAndView.addObject("advert", save);
            modelAndView.setViewName("load_image");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }


    @RequestMapping(value = "/add_item/{id}/{id_item}", method = RequestMethod.GET)
    public ModelAndView addItem(@SessionAttribute User user, @PathVariable long id, @PathVariable long id_item) {

        Item item = advertService.findItemById(id_item);

        if (item == null) item = new Item();
        ModelAndView modelAndView = new ModelAndView();
        if (advertService.findAdvertById(id) == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.addObject("id", id);
        modelAndView.addObject("item", item);
        modelAndView.setViewName("add_item");
        return modelAndView;
    }

    @RequestMapping(value = "/add_item", method = RequestMethod.POST)
    public String addItem(@SessionAttribute User user, @RequestParam String title, @RequestParam String price, @RequestParam long id, @RequestParam long id_item) {
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        Advert advert = advertService.findAdvertById(id);
        if (advert == null) return "redirect:/";
        if (!advert.getUser().getEmail().equals(user.getEmail()) && !admin) return "redirect:/";
        Item item = advertService.findItemById(id_item);
        if (item == null) item = new Item();
        item.setAdvert(advert);
        item.setTitle(title);
        item.setPrice(Float.parseFloat(price));
        advertService.saveItem(item);
        return "redirect:/edit_shop/" + id;
    }

    @RequestMapping(value = "/remove_shop/{id}", method = RequestMethod.GET)
    public String removeItem(@SessionAttribute User user, @PathVariable long id) {
        Advert advert = advertService.findAdvertById(id);
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (advert == null) return "redirect:/";

        if (!admin && !advert.getUser().getEmail().equals(user.getEmail())) return "redirect:/";

        advertService.removeAdvert(advert);
        return "redirect:/list_shop";
    }

    @RequestMapping(value = "/remove_item/{id}/{id_item}", method = RequestMethod.GET)
    public String removeItem(@SessionAttribute User user, @PathVariable long id, @PathVariable long id_item) {
        Item item = advertService.findItemById(id_item);
        if (item == null) return "redirect:/";
        Advert advert = advertService.findAdvertById(item.getAdvert().getId());
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (advert == null) return "redirect:/";

        if (!admin && !advert.getUser().getEmail().equals(user.getEmail())) return "redirect:/";

        advertService.removeItem(item);
        return "redirect:/edit_shop/" + id;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("id") int id) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Advert advert = advertService.findAdvertById(id);

                advert.setPic(bytes);


                advertService.saveAdvert(advert);

            } catch (Exception e) {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/update_shop", method = RequestMethod.POST)
    public ModelAndView updateShop(@SessionAttribute User user, @RequestParam long id, @RequestParam String title, @RequestParam String description, @RequestParam String wallet
    ,@RequestParam String address, @RequestParam String addaddress, @RequestParam Double latitude, @RequestParam Double longitude) {
        Advert advert = advertService.findAdvertById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/edit_shop/" + id);
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (advert.getUser().getEmail().equals(user.getEmail()) || admin) {
            advert.setTitle(title);
            advert.setDescription(description);
            advert.setWallet(wallet);
            advert.setAddress(address);
            advert.setAddAddress(addaddress);
            advert.setLatitude(latitude);
            advert.setLongitude(longitude);
            advertService.saveAdvert(advert);
        }
        return modelAndView;
    }


}
