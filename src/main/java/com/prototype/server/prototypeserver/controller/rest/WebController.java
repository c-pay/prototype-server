package com.prototype.server.prototypeserver.controller.rest;

import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.Item;
import com.prototype.server.prototypeserver.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping
public class WebController {


    @Autowired
    private AdvertService advertService;

    @RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image_id") int imageId) throws IOException {
        byte[] imageContent = null;

        imageContent = advertService.findById(imageId).getPic();

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

    @RequestMapping(value = "/shop/{id}", method = RequestMethod.GET)
    public ModelAndView shop(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView();
        Advert advert = advertService.findById(id);
        List<Item> items = advertService.findByAdvert(advert.getId());
        modelAndView.addObject("advert", advert);
        modelAndView.addObject("items", items);
        modelAndView.setViewName("shop");
        return modelAndView;
    }


    @RequestMapping(value = "/add_shop", method = RequestMethod.GET)
    public ModelAndView addShop() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add_shop");
        return modelAndView;
    }

    @RequestMapping(value = "/add_item/{id}", method = RequestMethod.GET)
    public ModelAndView addItem(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("id", id);
        modelAndView.setViewName("add_Item");
        return modelAndView;
    }

    @RequestMapping(value = "/add_shop", method = RequestMethod.POST)
    public ModelAndView addShop(@RequestParam String title, @RequestParam String description) {
        Advert advert = new Advert();
        advert.setTitle(title);
        advert.setDescription(description);
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

    @RequestMapping(value = "/add_item", method = RequestMethod.POST)
    public String addShop(@RequestParam String title, @RequestParam long price, @RequestParam long id) {
        ModelAndView modelAndView = new ModelAndView();
        Item item = new Item();
        item.setAdvert(advertService.findById(id));
        item.setTitle(title);
        item.setPrice(price);
        advertService.saveItem(item);

        return "redirect:/shop/"+id;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("id") int id) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Advert advert = advertService.findById(id);

                advert.setPic(bytes);


                advertService.saveAdvert(advert);

            } catch (Exception e) {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }
}
