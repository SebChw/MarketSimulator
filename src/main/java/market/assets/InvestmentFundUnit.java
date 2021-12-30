package market.assets;

import market.traders.InvestmentFund;

import java.util.HashMap;

public class InvestmentFundUnit extends NonCurrencyAsset {
    //Generally one investemnt FundUnit may probably issue a lot of different InvestmentFundUnits
    private InvestmentFund issuedBy;
    private float availableUnits;
    private HashMap<String, Float> boughtAssets; // User will get some proportional amount of earned money by that unit.
    private float fundPercentageProfit;
    

    //! This probably works the same way as Share that we have some specific amount of it and we can't buy more than that
    public InvestmentFundUnit(String name, float amountInCirculation, InvestmentFund issuedBy, Float startingRate, HashMap<String,Float> boughtAssets, float fundPercentageProfit) {
        super(name, "InvestmentFundUnit", amountInCirculation, issuedBy.getRegisteredCurrency(), startingRate);
        this.issuedBy = issuedBy;
        this.availableUnits = amountInCirculation;
        this.boughtAssets = boughtAssets;
        this.fundPercentageProfit = fundPercentageProfit;
        
    }

    @Override
    public String toString(){
        return super.toString() + "\nIssued By: " + issuedBy.getName();
    }

    
}
