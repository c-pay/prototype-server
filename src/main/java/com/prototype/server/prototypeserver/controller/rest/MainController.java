package com.prototype.server.prototypeserver.controller.rest;

import com.prototype.server.prototypeserver.controller.rest.dto.AdvertDTO;
import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.Transaction;
import com.prototype.server.prototypeserver.service.AdvertService;
import com.prototype.server.prototypeserver.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/savetransaction/{blockNumber}/{transactionIndex}/{from}/{to}/{value}/{hashTx}", method = RequestMethod.GET)
//    @RequestMapping(value = "/savetransaction/{blockNumber}", method = RequestMethod.GET)
    public void saveTransaction(@PathVariable long blockNumber
            , @PathVariable long transactionIndex
            , @PathVariable String from
            , @PathVariable String to
            , @PathVariable String value
            , @PathVariable String hashTx) {
        Transaction transaction = new Transaction();
        transaction.setBlockNumber(blockNumber);
        transaction.setTransactionIndex(transactionIndex);
        transaction.setFromAddress(from);
        transaction.setToAddress(to);
        transaction.setValue(value);
        transaction.setHashTx(hashTx);
        transactionService.saveTransaction(transaction);

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
