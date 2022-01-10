package market.assets;

import market.entityCreator.SemiRandomValuesGenerator;
import market.traders.InvestmentFund;
import market.traders.Trader;
import market.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InvestmentFundUnit extends AssetBackedByCurrency {
    //Generally one investemnt FundUnit may probably issue a lot of different InvestmentFundUnits
    private InvestmentFund issuedBy;
    private float availableUnits;
    private HashMap<String, Float> boughtAssets; // User will get some proportional amount of earned money by that unit.
    private float fundPercentageProfit;
    private World world;

    //! This probably works the same way as Share that we have some specific amount of it and we can't buy more than that
    public InvestmentFundUnit(String name, float amountInCirculation, InvestmentFund issuedBy, Float startingRate, HashMap<String,Float> boughtAssets, float fundPercentageProfit, World world) {
        super(name, "Investment Fund Unit", 0, issuedBy.getRegisteredCurrency(), startingRate);
        this.issuedBy = issuedBy;
        this.availableUnits = amountInCirculation;
        this.boughtAssets = boughtAssets;
        this.fundPercentageProfit = fundPercentageProfit;
        this.world = world;
        
    }
    public float getPossibleAmount(float amount){
        return SemiRandomValuesGenerator.getRandomIntNumber((int)amount);
    }

    @Override
    public String toString(){
        return super.toString() + "\nIssued By: " + issuedBy.getName() + "\nAssets in Unit: " + boughtAssets;
    }

    public float calculateRatio(){
        float ratio = 0;
        Iterator<Map.Entry<String, Float>> it = boughtAssets.entrySet().iterator(); //!Here I need to copy this as Im changing it within loop
            Map.Entry<String, Float> asset = null; 
            while (it.hasNext()){
                asset = it.next();
                ratio += (1 / world.getParticularAsset(asset.getKey()).getCurrentRate()) * asset.getValue() ; // ratio * amount of asset
            }
        return 1/ratio;
    }

    public void updateRate(){
        this.getMainBankRates().updateRate(calculateRatio());
    }
}
