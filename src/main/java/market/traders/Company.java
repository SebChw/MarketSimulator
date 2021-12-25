package market.traders;

import market.observers.*;
import market.assets.Currency;
import java.util.HashMap;
import java.util.ArrayList;

public class Company extends Trader implements AssetSubject{
    private String ipoDate;
    private float ipoShareValue;
    private ArrayList<Float> valueOverTime = new ArrayList<Float>();
    private float openingPrice;
    private float currentPrice;
    private float maximumPrice;
    private float profit;
    private float revenue;
    private int tradingVolume;
    private float totalSales;

    public Company(String tradingIdentifier, HashMap<Currency, Float> investmentBudget, String name,
                String ipoDate, float ipoShareValue, float openingPrice, float profit, float revenue){
        super(tradingIdentifier, investmentBudget, name);
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
