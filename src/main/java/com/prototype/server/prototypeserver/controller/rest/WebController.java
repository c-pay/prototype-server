package com.prototype.server.prototypeserver.controller.rest;

import com.prototype.server.prototypeserver.entity.*;
import com.prototype.server.prototypeserver.service.AdvertService;
import com.prototype.server.prototypeserver.service.CryptoService;
import com.prototype.server.prototypeserver.service.UserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Controller
@RequestMapping
@SessionAttributes("user")
public class WebController {

    @Autowired
    private CryptoService cryptoService;
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
        modelAndView = cryptoService.getCurrencyForPage(modelAndView);
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
        Set<Section> sections = new TreeSet<>(new Comparator<Section>() {
            @Override
            public int compare(Section o1, Section o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        for (Item item : items) {
            if(item.getSection()!=null)
            sections.add(item.getSection());
        }

        modelAndView.addObject("advert", advert);
        modelAndView.addObject("sections", sections);
        modelAndView.addObject("items", items);
        modelAndView.addObject("eth", cryptoService.getCurrency("ETH"));
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
        List<TypeItem> types = advertService.findAllTypeItem();

        Set<Section> sections = new TreeSet<>(new Comparator<Section>() {
            @Override
            public int compare(Section o1, Section o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        for (Item item : items) {
            if(item.getSection()!=null)
                sections.add(item.getSection());
        }

        modelAndView.addObject("types", types);
        modelAndView.addObject("advert", advert);
        modelAndView.addObject("items", items);
        modelAndView.addObject("sections", sections);
        modelAndView.setViewName("edit_shop");
        return modelAndView;
    }


    @RequestMapping(value = "/add_shop", method = RequestMethod.GET)
    public ModelAndView addShop() {
        ModelAndView modelAndView = new ModelAndView();
        List<TypeItem> types = advertService.findAllTypeItem();
        modelAndView.addObject("types", types);
        modelAndView.setViewName("add_shop");

        return modelAndView;
    }

    @RequestMapping(value = "/add_shop", method = RequestMethod.POST)
    public ModelAndView addShop(@SessionAttribute User user, @RequestParam String title, @RequestParam String description, @RequestParam String wallet
            , @RequestParam String address, @RequestParam String addaddress
            , @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam String typeItem,
                                @RequestParam String tel, @RequestParam String site, @RequestParam String email) {
        Advert advert = new Advert();
        TypeItem type = advertService.findTypeItemByTitle(typeItem);
        advert.setTypeItem(type);
        advert.setTitle(title);
        advert.setDescription(description);
        advert.setWallet(wallet);
        advert.setUser(user);
        advert.setAddress(address);
        advert.setAddAddress(addaddress);
        advert.setLatitude(latitude);
        advert.setLongitude(longitude);
        advert.setTel(tel);
        advert.setSite(site);
        advert.setEmail(email);
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
        modelAndView.addObject("sections", advertService.findAllSection());
        modelAndView.addObject("eth", cryptoService.getCurrency("ETH"));
        modelAndView.setViewName("add_item");
        return modelAndView;
    }

    @RequestMapping(value = "/add_item", method = RequestMethod.POST)
    public String addItem(@SessionAttribute User user, @RequestParam String title, @RequestParam int section,
                          @RequestParam String price, @RequestParam String priceCurrency, @RequestParam long id, @RequestParam long id_item) {
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        Advert advert = advertService.findAdvertById(id);
        if (advert == null) return "redirect:/";
        if (!advert.getUser().getEmail().equals(user.getEmail()) && !admin) return "redirect:/";
        Item item = advertService.findItemById(id_item);
        if (item == null) item = new Item();
        Section sectionById = advertService.findSectionById(section);

        item.setAdvert(advert);
        item.setTitle(title);
        item.setSection(sectionById);
        item.setPriceCurrency(Float.parseFloat(priceCurrency));
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
            , @RequestParam String address, @RequestParam String addaddress, @RequestParam Double latitude,
                                   @RequestParam Double longitude, @RequestParam String typeItem,
                                   @RequestParam String tel, @RequestParam String site, @RequestParam String email) {
        Advert advert = advertService.findAdvertById(id);
        TypeItem type = advertService.findTypeItemById(Long.parseLong(typeItem));

        ModelAndView modelAndView = new ModelAndView("redirect:/edit_shop/" + id);
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (advert.getUser().getEmail().equals(user.getEmail()) || admin) {
            advert.setTitle(title);
            advert.setTypeItem(type);
            advert.setDescription(description);
            advert.setWallet(wallet);
            advert.setAddress(address);
            advert.setAddAddress(addaddress);
            advert.setLatitude(latitude);
            advert.setLongitude(longitude);
            advert.setTel(tel);
            advert.setSite(site);
            advert.setEmail(email);
            advertService.saveAdvert(advert);
        }
        return modelAndView;
    }


    @RequestMapping(value = "/list_type", method = RequestMethod.GET)
    public ModelAndView listType(Authentication authentication) {

        User user = getPrincipalUser();
        ModelAndView modelAndView = new ModelAndView();
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (!admin) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.addObject("types", advertService.findAllTypeItem());
        modelAndView.setViewName("list_type");
        return modelAndView;
    }

    @RequestMapping(value = "/add_type", method = RequestMethod.GET)
    public ModelAndView addType() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add_type");
        return modelAndView;
    }

    @RequestMapping(value = "/add_type", method = RequestMethod.POST)
    public String addItem(@SessionAttribute User user, @RequestParam String title) {
        boolean admin = user.getRoles().contains(new Role("ADMIN"));

        if (!admin) return "redirect:/";
        TypeItem type = advertService.findTypeItemByTitle(title);
        if (type != null) return "redirect:/list_type";
        advertService.saveTypeItem(new TypeItem(title));
        return "redirect:/list_type";
    }

    @RequestMapping(value = "/edit_type/{id}", method = RequestMethod.GET)
    public ModelAndView editType(@SessionAttribute User user, @PathVariable long id) {

        ModelAndView modelAndView = new ModelAndView();
        boolean admin = user.getRoles().contains(new Role("ADMIN"));

        if (!admin) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        modelAndView.addObject("type", advertService.findTypeItemById(id));
        modelAndView.setViewName("edit_type");
        return modelAndView;
    }

    @RequestMapping(value = "/update_type", method = RequestMethod.POST)
    public ModelAndView updateType(@SessionAttribute User user, @RequestParam long id, @RequestParam String title) {

        ModelAndView modelAndView = new ModelAndView("redirect:/list_type");
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (!admin) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        TypeItem type = advertService.findTypeItemById(id);
        type.setTitle(title);
        advertService.saveTypeItem(type);
        return modelAndView;
    }


    //TODO: сделать универсальную функцию загрузи и чтения картинок
    @RequestMapping(value = "/upload_type", method = RequestMethod.POST)
    public String handleFileUploadType(@RequestParam("file") MultipartFile file, @RequestParam("id") long id) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                TypeItem type = advertService.findTypeItemById(id);
                type.setPic(bytes);
                advertService.saveTypeItem(type);
            } catch (Exception e) {
                return "redirect:/list_type";
            }
        }
        return "redirect:/list_type";
    }

    @RequestMapping(value = "/image_type/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImageType(@PathVariable("image_id") long imageId) throws IOException {
        byte[] imageContent = null;

        imageContent = advertService.findTypeItemById(imageId).getPic();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public ModelAndView help() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("help");
        return modelAndView;
    }

    @RequestMapping(value = "/landing", method = RequestMethod.GET)
    public ModelAndView landing() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    @RequestMapping(value = "/list_section", method = RequestMethod.GET)
    public ModelAndView addSection() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sections", advertService.findAllSection());
        modelAndView.setViewName("list_section");
        return modelAndView;
    }

    @RequestMapping(value = "/add_section/{id}", method = RequestMethod.GET)
    public ModelAndView addSection(@SessionAttribute User user, @PathVariable int id) {

        ModelAndView modelAndView = new ModelAndView();
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (!admin) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        Section section = new Section();
        if (id != 0) {
            section = advertService.findSectionById(id);
        }

        modelAndView.addObject("section", section);

        modelAndView.setViewName("add_section");
        return modelAndView;
    }

    @RequestMapping(value = "/add_section", method = RequestMethod.POST)
    public ModelAndView addSection(@SessionAttribute User user, @RequestParam int id, @RequestParam String title) {
        ModelAndView modelAndView = new ModelAndView();
        boolean admin = user.getRoles().contains(new Role("ADMIN"));
        if (!admin) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        Section section = advertService.findSectionById(id);
        if (section == null) {
            section = new Section();
        }
        section.setTitle(title);
        advertService.saveSection(section);

        modelAndView.addObject("sections", advertService.findAllSection());
        modelAndView.setViewName("list_section");
        return modelAndView;
    }

    //TODO: сделать универсальную функцию загрузи и чтения картинок
    @RequestMapping(value = "/upload_item", method = RequestMethod.POST)
    public String handleFileUploadItem(@RequestParam("file") MultipartFile file, @RequestParam("id") long id, @RequestParam("advert_id") long advert_id ) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Item item = advertService.findItemById(id);
                item.setPic(bytes);
                advertService.saveItem(item);
            } catch (Exception e) {
                return "redirect:/add_item/"+advert_id+"/"+id;
            }
        }

        return "redirect:/add_item/"+advert_id+"/"+id;
    }

    @RequestMapping(value = "/image_item/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImageItem(@PathVariable("image_id") long imageId) throws IOException {
        byte[] imageContent = null;

        imageContent = advertService.findItemById(imageId).getPic();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }
}
