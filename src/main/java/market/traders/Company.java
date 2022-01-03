package market.traders;

import market.observers.*;
import market.assets.Currency;
import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import market.assets.Share;
import market.entityCreator.SemiRandomValuesGenerator;
import java.time.LocalDate;
import market.observers.CompanyObserver;
public class Company extends Trader implements CompanySubject{
    private LocalDate ipoDate;
    private Float ipoShareValue; // This is rather attribute of share!
    private ArrayList<Float> valueOverTime = new ArrayList<Float>();
    private Float openingPrice;
    private Float currentPrice;
    private Float maximumPrice;
    private Float profit;
    private Float revenue;
    private Integer tradingVolume;
    private Float totalSales;
    private Currency registeredCurrency;
    private Share share;

    private String [] moreDetails = {"IPO date: ", "IPO share value: ", "Opening price: ", "Current price: ", "Maximum price: ", "Profit: ",
                                    "Revenue: ", "Trading Volume: ", "Total sales: ", "Registered currency: "};

    private ArrayList<CompanyObserver> companyObservers = new ArrayList<CompanyObserver>();
    //!private Share share; // In General I think that share is something that belongs to the company. Have to think about it.
    //Probably it is better to have it only in one place so that consistency is higher. Now issuing shares is no problem.

    public Company(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name,
                String ipoDate, float ipoShareValue, float openingPrice, float profit, float revenue, Currency registeredCurrency){
        super(tradingIdentifier, investmentBudget, name, "Company");
        this.ipoDate = LocalDate.parse(ipoDate);
        this.openingPrice = openingPrice;
        this.currentPrice = openingPrice;
        this.maximumPrice = openingPrice;
        this.valueOverTime.add(openingPrice);
        this.profit = profit;
        this.revenue = revenue;
        this.tradingVolume = 0;
        this.totalSales = (float)0.0;
        this.registeredCurrency = registeredCurrency;
        this.share = new Share(name, SemiRandomValuesGenerator.getRandomIntNumber(50), this, 1/ipoShareValue);

    }

    public Share getShare(){
        return this.share;
    }
    public float getShareValue(){
        return this.share.getCurrentRate();
    }
    public float getCurrentPrice(){
        return this.currentPrice;
    }
    public Currency getRegisteredCurrency() {
        return this.registeredCurrency;
    }

    public void increaseShares(int amount){
        this.share.increaseShares(amount);
    }
    
    public LocalDate getIpoDate(){
        return this.ipoDate;
    }

    public void generateRevenue(){

    }

    public void generateProfit(){

    }

    public void issueShares(){

    }

    @Override
    public void notifyObservers() {
        for (CompanyObserver companyObserver : companyObservers) {
            companyObserver.update(this);
        }
    }

    @Override
    public void registerObserver(CompanyObserver observer) {
        this.companyObservers.add(observer);
        
    }

    @Override
    public void removeObserver(CompanyObserver observer) {
        this.companyObservers.remove(observer);
        
    }


    @Override
    public void fillGridPane(GridPane traderDetails){
        super.fillGridPane(traderDetails);
        Float ipoValue = 1/share.getCurrentRate();
        String [] filledDetails = {ipoDate.toString(), ipoValue.toString(), openingPrice.toString(), currentPrice.toString(), maximumPrice.toString(),
                                    profit.toString(), revenue.toString(), tradingVolume.toString(), totalSales.toString(), registeredCurrency.getName()};
        for (int i = 0; i < filledDetails.length; i++) {
            Label l = new Label();
            l.setText(moreDetails[i] + filledDetails[i]);
            traderDetails.add(l, 0, i + 3);
        }
        
    }



    
}
