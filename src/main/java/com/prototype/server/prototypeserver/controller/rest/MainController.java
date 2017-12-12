package com.prototype.server.prototypeserver.controller.rest;

import com.prototype.server.prototypeserver.controller.rest.dto.AdvertDTO;
import com.prototype.server.prototypeserver.controller.rest.dto.ItemDTO;
import com.prototype.server.prototypeserver.controller.rest.dto.TransactionDTO;
import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.Item;
import com.prototype.server.prototypeserver.entity.Transaction;
import com.prototype.server.prototypeserver.service.AdvertService;
import com.prototype.server.prototypeserver.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController("/")
@CrossOrigin
public class MainController {

    @Autowired
    private AdvertService advertService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/findalladvert", method = RequestMethod.GET)
    public AdvertDTO findAllAdvert() {
        List<Advert> all = advertService.findAll();
        return new AdvertDTO(all);
    }

    @RequestMapping(value = "/getstatus/{address}", method = RequestMethod.GET)
    public String getStatus(String address) {
        return "ok -" + address;
    }

    @RequestMapping(value = "/gettesteth/{address}", method = RequestMethod.GET)
    public void getTestEth(@PathVariable String address) {
        if (!address.isEmpty())
            advertService.getTestEth(address);
    }

    @RequestMapping(value = "/savetransaction/{from}/{to}/{value}/{hashTx}", method = RequestMethod.GET)
    public void saveTransaction(@PathVariable String from
            , @PathVariable String to
            , @PathVariable String value
            , @PathVariable String hashTx) {
        Transaction transaction = new Transaction();
        transaction.setFromAddress(from);
        transaction.setToAddress(to);
        transaction.setValue(value);
        transaction.setHashTx(hashTx);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        transaction.setDateTx(formatter1.format(new Date()));
        transactionService.saveTransaction(transaction);

    }


    @RequestMapping(value = "/findalltransactionbyaddress/{address}", method = RequestMethod.GET)
    public TransactionDTO findalltransactionbyaddress(@PathVariable String address) {
        if (!address.isEmpty()) {
            return new TransactionDTO(transactionService.findAllTransactionByAddress(address));
        }
        return null;
    }

    @RequestMapping(value = "/findallitembyid/{id}", method = RequestMethod.GET)
    public ItemDTO findallitembyid(@PathVariable long id) {
        List<Item> items = advertService.findByAdvert(id);

        return new ItemDTO(items);
    }
//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    public List<AdvertDTO> test() {
//        ArrayList<AdvertDTO> list = new ArrayList<>();
//        list.add(new AdvertDTO("HELLO WORLD"));
//        list.add(new AdvertDTO("HELLO WORLD 2"));
//        list.add(new AdvertDTO("test"));
//        return list;
//    }
}
