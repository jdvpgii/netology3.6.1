package com.gii.netology;

import java.io.*;

public class Basket {
    private String[] productName;
    private int[] price;
    private int[] counterOfEachItem;
    int productSum = 0;
    public Basket(String[] productName, int[] price) {
        this.productName = productName;
        this.price = price;
        counterOfEachItem = new int[price.length];
    }

    public Basket(String[] productName, int[] price, int[] counterOfEachItem) {
        this.productName = productName;
        this.price = price;
        this.counterOfEachItem = counterOfEachItem;
    }

    public String[] getProductName() {
        return productName;
    }

    public void setProductName(String[] productName) {
        this.productName = productName;
    }

    public int[] getPrice() {
        return price;
    }

    public void setPrice(int[] price) {
        this.price = price;
    }

    public int[] getCounterOfEachItem() {
        return counterOfEachItem;
    }

    public void setCounterOfEachItem(int[] counterOfEachItem) {
        this.counterOfEachItem = counterOfEachItem;
    }

    protected void addToCart(int productNum, int amount) {
        counterOfEachItem[productNum] += amount;
    }

    protected void printCart() {

        System.out.println("Your shopping cart:");
        // TODO 1.1 как устранить это повторение
        for (int i = 0; i < productName.length; i++) {
            if (counterOfEachItem[i] != 0) {
                System.out.println(productName[i] + " " + price[i] + " " + counterOfEachItem[i]);
            }
            productSum += price[i] * counterOfEachItem[i];
        }
        System.out.println(productSum);
    }

    protected void saveTxt( File textFile) {
        try (FileWriter fileWriter = new FileWriter(textFile);
             PrintWriter printWriter = new PrintWriter(fileWriter)){
            // TODO 1.2 как устранить это потоврение
            for (int i = 0; i < productName.length; i++) {
                printWriter.println(productName[i] + " " + price[i] + " " + counterOfEachItem[i]);
            }
            printWriter.print(productSum);
            System.out.println();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    protected static Basket loadFromTxtFile(File textFile) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(textFile))) {
            String[] products = in.readLine().split(" ");
            String[] price = in.readLine().split(" ");

            int[] priceTxt = new int[price.length];
            for (int i = 0; i < price.length; i++) {
                priceTxt[i] = Integer.parseInt(price[i]);
            }
            String[] counter = in.readLine().split(" ");
            int[] counterFromTxt = new int[counter.length];
            for (int j = 0; j < counter.length; j++) {
                counterFromTxt[j] = Integer.parseInt(counter[j]);
            }
            Basket basketFromTxt = new Basket(products, priceTxt);
            return basketFromTxt;
        }

    }

    static void saveBin (File file) throws IOException{
        try (FileOutputStream fileOutputStream = new FileOutputStream("basket.bin");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){
            objectOutputStream.writeObject(Main.basket);
        }
    }

    static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (Basket) ois.readObject();
        }
    }
}