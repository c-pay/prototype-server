package com.prototype.server.prototypeserver.service;

import com.prototype.server.prototypeserver.entity.Advert;
import com.prototype.server.prototypeserver.repository.AdvertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.List;

@Service("advertService")
public class AdvertService {

    @Autowired
    private AdvertRepository advertRepository;

    public List<Advert> findAll(){
        return advertRepository.findAll();
    }

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
            Credentials credentials =
                    WalletUtils.loadCredentials(
                            "123123123",
                            new File("C:/key"));//TODO заменить на внутренний файл-ключ
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
