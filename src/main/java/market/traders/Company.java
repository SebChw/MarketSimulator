package market.traders;

import market.observers.*;
import market.assets.Currency;
import java.util.HashMap;
import java.util.ArrayList;
import market.assets.Share;

public class Company extends Trader implements AssetSubject{
    private String ipoDate;
    private Float ipoShareValue; // This is rather attribute of share!
    private ArrayList<Float> valueOverTime = new ArrayList<Float>();
    private float openingPrice;
    private float currentPrice;
    private float maximumPrice;
    private float profit;
    private float revenue;
    private int tradingVolume;
    private float totalSales;
    private Currency registeredCurrency;
    //!private Share share; // In General I think that share is something that belongs to the company. Have to think about it.
    //Probably it is better to have it only in one place so that consistency is higher. Now issuing shares is no problem.

    public Company(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name,
                String ipoDate, float ipoShareValue, float openingPrice, float profit, float revenue, Currency registeredCurrency){
        super(tradingIdentifier, investmentBudget, name, "Company");
        this.ipoDate = ipoDate;
        this.ipoShareValue = ipoShareValue;
        this.openingPrice = openingPrice;
        this.currentPrice = openingPrice;
        this.maximumPrice = openingPrice;
        this.valueOverTime.add(openingPrice);
        this.profit = profit;
        this.revenue = revenue;
        this.tradingVolume = 0;
        this.totalSales = 0;
        this.registeredCurrency = registeredCurrency;

    }

    public Float getIpoShareValue(){
        return this.ipoShareValue;
    }
    public Currency getRegisteredCurrency() {
        return this.registeredCurrency;
    }

    public void generateRevenue(){

    }

    public void generateProfit(){

    }

    public void issueShares(){

    }

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void registerObserver(AssetObserver observer) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeObserver(AssetObserver observer) {
        // TODO Auto-generated method stub
        
    }


    
}
