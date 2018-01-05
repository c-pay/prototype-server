package com.prototype.server.prototypeserver.service;

import javafx.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class CryptoService {

    public Pair<String, Boolean> getBitcoinCurrency(String coin) {

        String result = " 0.0EUR";
        Boolean rise = true;
        try {
            URL url = null;
            if ("BTC".equals(coin)) {
                url = new URL("https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=EUR");
            } else if ("ETH".equals(coin)) {
                url = new URL("https://api.coinmarketcap.com/v1/ticker/ethereum/?convert=EUR");
            } else {
                return new Pair<>((coin + result), rise);
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode());
                return new Pair<>((coin + result), rise);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                if (output.indexOf("price_eur") > 0) {
                    result = output.substring(output.indexOf(":") + 2);
                    result = String.format("%.2f", Double.parseDouble(result.substring(1, result.length() - 3))) + "EUR";
                }
                if (output.indexOf("percent_change_24h") > 0) {
                    String res = output.substring(output.indexOf(":") + 2);
                    result = result + " (" + res.substring(1, res.length() - 3) + "%)";
                }
                if (output.indexOf("percent_change_24h") > 0) {
                    rise = (output.indexOf("-") > 0 ? false : true);
                }
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>((coin + " " + result), rise);
    }


    public Double getCurrency(String coin) {

        double result = 0.0d;

        try {
            URL url = null;
            if ("BTC".equals(coin)) {
                url = new URL("https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=EUR");
            } else if ("ETH".equals(coin)) {
                url = new URL("https://api.coinmarketcap.com/v1/ticker/ethereum/?convert=EUR");
            } else {
                return result;
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode());
                return result;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                if (output.indexOf("price_eur") > 0) {
                    if (output.indexOf(".") > 0) {
                        result = Double.parseDouble(output.substring(output.indexOf(":") + 3, output.indexOf(".") + 3));
                    }else if (output.indexOf(",") > 0) {
                        result = Double.parseDouble(output.substring(output.indexOf(":") + 3, output.indexOf(",") + 3));
                    }
                }

            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public ModelAndView getCurrencyForPage(ModelAndView modelAndView) {
        Pair<String, Boolean> btc = getBitcoinCurrency("BTC");
        modelAndView.getModel().put("btc", btc.getKey());
        modelAndView.getModel().put("btc_rise", btc.getValue());
        Pair<String, Boolean> eth = getBitcoinCurrency("ETH");
        modelAndView.getModel().put("eth", eth.getKey());
        modelAndView.getModel().put("eth_rise", eth.getValue());
        return modelAndView;
    }
}
