package com.prototype.server.prototypeserver.controller.rest;

import com.prototype.server.prototypeserver.controller.rest.dto.AdvertDTO;
import com.prototype.server.prototypeserver.controller.rest.dto.ItemDTO;
import com.prototype.server.prototypeserver.controller.rest.dto.TransactionDTO;
import com.prototype.server.prototypeserver.controller.rest.dto.TypeItemDTO;
import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.entity.Item;
import com.prototype.server.prototypeserver.entity.Transaction;
import com.prototype.server.prototypeserver.entity.TypeItem;
import com.prototype.server.prototypeserver.service.AdvertService;
import com.prototype.server.prototypeserver.service.CryptoService;
import com.prototype.server.prototypeserver.service.MailService;
import com.prototype.server.prototypeserver.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.web3j.utils.Convert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.bouncycastle.asn1.iana.IANAObjectIdentifiers.mail;

@RestController("/")
@CrossOrigin
public class MainController {

    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private AdvertService advertService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    MailService mailService;

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

    @RequestMapping(value = "/findalltype", method = RequestMethod.GET)
    public TypeItemDTO getTestEth() {
        List<TypeItem> types = advertService.findAllTypeItem();
        return new TypeItemDTO(types);
    }

    @RequestMapping(value = "/getethprice", method = RequestMethod.GET)
    public double getEthPrice() {
        return cryptoService.getCurrency("ETH");
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

        Advert advertByWallet = advertService.findFirstByWallet(to);

        Double rate_eth = cryptoService.getCurrency("ETH");
        if (advertByWallet != null && advertByWallet.getUser() != null && advertByWallet.getUser().getEmail()!= null && !advertByWallet.getUser().getEmail().equals("")) {
            StringBuilder builder = new StringBuilder();
            String email = advertByWallet.getUser().getEmail();
            builder.append("<table cellpadding=15 style='margin-top:10px; margin-left:20px;' border='1'>");
            builder.append("<tr><td align=center  colspan='2'><b>PAID</b></td></tr>");
            builder.append("<tr><td>FROM</td><td>" + from + "</td></tr>");
            builder.append("<tr><td>TO</td><td>" + to + "</td></tr>");
            double result = 0d;
            if(rate_eth!=0d) {
                result = Double.parseDouble(Convert.fromWei(value, Convert.Unit.ETHER).toPlainString())* rate_eth;
            }
            builder.append("<tr><td>VALUE</td><td>" + String.format("%.2f",result).toString() + " EURO</td></tr>");
            builder.append("<tr><td>VALUE ETH</td><td>" + Convert.fromWei(value, Convert.Unit.ETHER).toPlainString() + " Ether</td></tr>");
            builder.append("<tr><td>TX INFO</td><td><a href='https://etherscan.io/tx/" + hashTx + "' >" + hashTx + "</a></td></tr>");
            builder.append("</table>");


            mailService.send("", email, "PAID Tx ", builder.toString());
            mailService.send("", "income@cpay.click", "PAID Tx ", builder.toString());
        }

    }

    @RequestMapping(value = "/savepaid/{from}/{to}/{value}", method = RequestMethod.GET)
    public void savePaid(@PathVariable String from
            , @PathVariable String to
            , @PathVariable String value) {
//        Transaction transaction = new Transaction();
//        transaction.setFromAddress(from);
//        transaction.setToAddress(to);
//        transaction.setValue(value);
//        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
//        transaction.setDateTx(formatter1.format(new Date()));
//        transactionService.saveTransaction(transaction);
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test() {

        System.out.println("ok");
    }

}
