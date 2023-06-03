package com.gii.netology;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    static Basket basket;
    static String fileToLoadName;
    static String fileToSaveName;
    static String fileToLogName;
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String[] productName = {"Bread", "Apple", "Milk", "Pineapple", "Heroin"};
        int[] price = {48, 77, 89, 350, 500};
        Basket basket1 = new Basket(productName, price);
        ClientLog clientLog = new ClientLog();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("shop.xml"));
        NodeList nodeList = document.getChildNodes().item(0).getChildNodes();
        String methodToLoad = null;
        String methodToSave = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                if (element.getTagName().equals("load")) {
                    if (element.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                        fileToLoadName = element.getElementsByTagName("fileName").item(0).getTextContent();
                        if (element.getElementsByTagName("format").item(0).getTextContent().equals("json")) {
                            methodToLoad = "json";
                        }
                        if (element.getElementsByTagName("format").item(0).getTextContent().equals("txt")) {
                            methodToLoad = "txt";
                        }
                    }
                }
                if (element.getTagName().equals("save")) {
                    if (element.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                        fileToSaveName = element.getElementsByTagName("fileName").item(0).getTextContent();
                        if (element.getElementsByTagName("format").item(0).getTextContent().equals("json")) {
                            methodToSave = "json";
                        }
                        if (element.getElementsByTagName("format").item(0).getTextContent().equals("txt")) {
                            methodToSave = "txt";
                        }
                    }
                }
                if (element.getTagName().equals("log")) {
                    if (element.getElementsByTagName("enabled").item(0).getTextContent().equals("true")) {
                        fileToLogName = element.getElementsByTagName("fileName").item(0).getTextContent();
                    }
                }
            }
        }

        if (Objects.equals(methodToLoad, "json")) {
            loadFromJson(productName, price);
        }
        if (Objects.equals(methodToLoad, "txt")) {
            loadFromJson(productName, price);
        } else {
            basket = new Basket(productName, price);
        }

        while (true) {
            System.out.println("List of possible items to buy");
            basket.getPrice();
            showStoreProducts(productName, price);

            System.out.println("\nSelect a product or enter `end`");
            String productNumber = reader.readLine();
            if (productNumber.equals("end")) {
                basket1.printCart();
                break;
            }

            System.out.println("\nEnter item quantity or enter `end`");
            String productCounter = reader.readLine();
            if (productCounter.equals("end")) {
                basket1.printCart();
                break;
            }
            clientLog.log(productNumber, productCounter);
            basket1.addToCart(Integer.parseInt(productNumber)-1, Integer.parseInt(productCounter));
        }

        File theFile = new File("log.cvs");
        clientLog.exportAsCVS(theFile);
    }

    private static void showStoreProducts(String[] strings, int[] price) {
        int x = 1;
        for (String str : strings) {
            System.out.println(x + ". " + str + " " + price[x-1] + "$/PC.");
            x++;
        }
    }

    public static void saveToJson() throws IOException {
        JSONObject basketJson = new JSONObject();

        JSONArray productsJson = new JSONArray();
        productsJson.addAll(List.of(basket.getProductName()));
        basketJson.put("products", productsJson);

        JSONArray pricesJson = new JSONArray();
        String[] stringPricesJson = Arrays.stream(basket.getPrice()).mapToObj(String::valueOf).toArray(String[]::new);
        pricesJson.addAll(List.of(stringPricesJson));
        basketJson.put("prices", pricesJson);

        JSONArray counterItemJson = new JSONArray();
        String[] stringAmountJson = Arrays.stream(basket.getCounterOfEachItem()).mapToObj(String::valueOf).toArray(String[]::new);
        counterItemJson.addAll(List.of(stringAmountJson));
        basketJson.put("amount", counterItemJson);

        try (FileWriter fileWriter = new FileWriter("basket.json")) {
            fileWriter.write(basketJson.toJSONString());
            fileWriter.flush();
        }
    }

    public static void loadFromJson(String[] products, int[] prices) throws IOException, ParseException {
        File jsonFile = new File(fileToLoadName);
        if (jsonFile.exists()) {
            basket = new Basket(products, prices);
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader(fileToLoadName));
                JSONObject basketParsedJson = (JSONObject) obj;

                JSONArray productsJson = (JSONArray) basketParsedJson.get("products");
                String[] productsFromJson = new String[productsJson.size()];
                for (int i = 0; i < productsJson.size(); i++) {
                    productsFromJson[i] = (String) productsJson.get(i);
                }
                basket.setProductName(productsFromJson);

                JSONArray pricesJson = (JSONArray) basketParsedJson.get("prices");
                String[] pricesFromJson = new String[pricesJson.size()];
                for (int i = 0; i < pricesJson.size(); i++) {
                    pricesFromJson[i] = (String) pricesJson.get(i);
                }
                int[] intPricesFromJson = new int[pricesFromJson.length];
                for (int i = 0; i < pricesFromJson.length; i++) {
                    intPricesFromJson[i] = Integer.parseInt(pricesFromJson[i]);
                }
                basket.setPrice(intPricesFromJson);

                JSONArray counterItemFromJSON = (JSONArray) basketParsedJson.get("amount");
                String[] amountFromJson = new String[counterItemFromJSON.size()];
                for (int i = 0; i < counterItemFromJSON.size(); i++) {
                    amountFromJson[i] = (String) counterItemFromJSON.get(i);
                }
                int[] intAmountFromJson = new int[amountFromJson.length];
                for (int i = 0; i < amountFromJson.length; i++) {
                    intAmountFromJson[i] = Integer.parseInt(amountFromJson[i]);
                }
                basket.setCounterOfEachItem(intAmountFromJson);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            basket = new Basket(products, prices);
        }

    }
}
