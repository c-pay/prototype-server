package com.prototype.server.prototypeserver.service;

import com.prototype.server.prototypeserver.entity.*;
import com.prototype.server.prototypeserver.repository.AdvertRepository;
import com.prototype.server.prototypeserver.repository.ItemRepository;
import com.prototype.server.prototypeserver.repository.TransactionRepository;
import com.prototype.server.prototypeserver.repository.TypeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service("advertService")
public class AdvertService {

    @Autowired
    private AdvertRepository advertRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TypeItemRepository typeItemRepository;

    public List<Advert> findAll(){
        return advertRepository.findAll();
    }

    public List<TypeItem> findAllTypeItem(){
        return typeItemRepository.findAll();
    }

    public TypeItem findTypeItemByTitle(String title){
        return typeItemRepository.findByTitle(title);
    }

    public TypeItem findTypeItemById(long id){
        return typeItemRepository.findById(id);
    }

    public TypeItem saveTypeItem(TypeItem typeItem){
        return typeItemRepository.save(typeItem);
    }

    public void removeItem(Item item){
        itemRepository.delete(item);
    }

    public void removeAdvert(Advert advert){
       advertRepository.delete(advert);
    }

    private  ResourceLoader resourceLoader;
    public void getTestEth(String address){
        try {
            // We start by creating a new web3j instance to connect to remote nodes on the network.
            Web3j web3j = Web3j.build(new HttpService(
                    "https://rinkeby.infura.io/oShbYdHLGQhi0rn1audL"));  // FIXME: Enter your Infura token here;
            System.out.println("Connected to Ethereum client version: "
                    + web3j.web3ClientVersion().send().getWeb3ClientVersion());
            System.out.println("connect");
            // We then need to load our Ethereum wallet file

            // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
//            Resource resource = resourceLoader.getResource("classpath:/path/to/key");
//            File dbAsFile = resource.getFile();
//            File file =  new ClassPathResource("static/key").getFile();
//            File file = resourceLoader.getResource("classpath:key").getFile();
            File homedir = new File(System.getProperty("user.home"));
            Credentials credentials =
                    WalletUtils.loadCredentials(
                            "123123123",
                            new File(homedir, "key"));//TODO заменить на внутренний файл-ключ
            System.out.println("Credentials loaded");

            // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
//        log.info("Sending 1 Wei ("
//                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
            System.out.println("Sending 1 ETH ("
                    + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
            TransactionReceipt transferReceipt = Transfer.sendFunds(
                    web3j, credentials,
                    address,  // you can put any address here
                    BigDecimal.ONE, Convert.Unit.ETHER)  // 1 wei = 10^-18 Ether
                    .send();
            System.out.println("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                    + transferReceipt.getTransactionHash());

            Transaction transaction = new Transaction();
            transaction.setFromAddress(transferReceipt.getFrom());
            transaction.setToAddress(transferReceipt.getTo());
            transaction.setValue("1000000000000000000");
            transaction.setHashTx(transferReceipt.getTransactionHash());
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            transaction.setDateTx(formatter1.format(new Date()));
            transactionRepository.save(transaction);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Advert findAdvertById(long id){
        return advertRepository.findOne(id);

    }

    public Item findItemById(long id){
        return itemRepository.findOne(id);

    }
    public List<Advert> findAllByUser(User user){
        return advertRepository.findAllByUser(user);

    }

    public Advert findFirstByWallet(String wallet){
        return advertRepository.findFirstByWallet(wallet.trim());

    }

    public Advert saveAdvert(Advert advert){
        return advertRepository.saveAndFlush(advert);
    }

    public Item saveItem(Item item){
        return itemRepository.saveAndFlush(item);
    }
    public List<Item> findByAdvert(long id){
        return itemRepository.findByAdvert(id);
    }
    public List<Item> findByAdvertLight(long id){
        return itemRepository.findByAdvertLight(id);
    }
}
