package market.assets;

import market.traders.Company;
import market.exchangeRates.ExchangeRatesProvider;
import market.entityCreator.SemiRandomValuesGenerator;

public class Share extends AssetBackedByCurrency{
    //! Generally commodities and Currencies rather doesn't have limits. However Company issue only some shares and only issued can be
    //! Bough no more
    private Company company;
    private volatile float availableShares;
    private float issuedShares;
    

    public Share(String name, float availableShares, Company company, Float startingRate) {
        super(name, "Share", 0, company.getRegisteredCurrency(), startingRate);
        this.company = company;
        this.issuedShares = availableShares;
        this.availableShares = availableShares;
        
    }
    
    public void increaseShares(float amount){
        this.availableShares += amount;
        this.issuedShares += amount;
    }

    @Override
    public float getPossibleAmount(){
        //! Here if there is no more Shares to buy we must be carefull as we will have 0 available shares
        if(this.availableShares < 1){
            System.out.println(this.company.getName() + " " + this.availableShares);
            return 0;
        }
        return SemiRandomValuesGenerator.getRandomIntNumber((int)this.availableShares) + 1;
    }
    public float getPossibleAmount(float amount){
        if(amount < 1){
            System.out.println("User ERROR! with company: " + this.company.getName() + " " + amount);
            return 0;
        }
        return SemiRandomValuesGenerator.getRandomIntNumber((int)(amount));
    }
    public float getAvailableShares(){
        return this.availableShares;
    }
    @Override
    public boolean canBeBought(float amount){
        if ((int)amount != amount){
            System.out.println(amount + " " + (int)amount);
        }
        synchronized(this){
        if (availableShares < amount) {
            System.out.println("You Can't buy this share as this would lead to amount In Circulation > IssuedShares!");
            return false;
        }
            availableShares -= amount; //! Im Freezing this shares!
        }
        
        return true;
    }
    @Override 
    public void unfreeze(float amount){
        synchronized(this){
            availableShares += amount; //! Im Unfreezing this shares!
            if(this.availableShares < 0){
                System.out.println("After freezing shares are negative!");
            }
        }
        
    }
    public synchronized void changeAmountInCirculation(float amount){
        //BE VERY CAREFULL HERE!
        super.changeAmountInCirculation(amount);
        if (amount < 0){
            //I NEED TO SUBTRACT AS WHEN IM SELLING THEN amount is negative!
            //! This was the biggest bug I've made and it was hard to find itp
            this.availableShares -= amount; //! DONT NEED TO DO THIS IM DOING IT DURING UNFREEZING ILL SHOULD DO THIS ONLY WHEN IM SELLING
        }
           
        if(this.availableShares < 0){
            System.out.println("After chaning amount shares are negative!");
        }
    }

    @Override
    public String toString(){
        return super.toString() + "\nIssued by: " + company.getName();
    }

    public float calculateMainToThis(float amount) {
        return (int)super.calculateMainToThis(amount);
    }
}
