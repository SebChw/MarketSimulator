package market.exchangeRates;

import java.util.HashMap;
import java.util.ArrayList;

import market.priceRules.*;

import java.util.LinkedList;

import javafx.scene.chart.XYChart;

public class ExchangeRatesProvider {

    private String nameOfBackingAsset;
    private AssetPriceRule assetPriceRule = new BasicAssetPriceRule();
    private float accumulatedRateChange = 0;
    private int numberOfStoredRates = 0;
    private LinkedList<Float> rates = new LinkedList<Float>(); // This provides exchange rates of one asset to all other


    public ExchangeRatesProvider(String nameOfBackingAsset, float startingPrice){
        this.nameOfBackingAsset = nameOfBackingAsset; // Main asset that Backs everything in our system
        this.updateRate(startingPrice);
    }

    public void updateRate(Float rate){
        this.rates.addFirst(rate); // Consider here inserting new rate at the begining so taking it will be O(1). //!Add FIRST METHOD
        numberOfStoredRates += 1;
    }
    public void updateRate(){
        //! DEFINE RULES OF UPDATING MARKET INDICES and INVESTMENT FUNDS UNITS!
        float currentRate = getRate();
        float updated = 0;
        if (currentRate < 1 && accumulatedRateChange != 0){
            updated = 1/(1/currentRate - 1/accumulatedRateChange);
        }
        else{
            updated = currentRate + accumulatedRateChange;
        }

        if (updated > 0){
            updateRate(updated);
        }
        accumulatedRateChange = 0;
    }
    
    public float getRate(){
        return this.rates.getFirst(); //!Consider using GetFirst here and appending new raters at the beginning!
    }

    public void removeLastRate(){
        this.rates.removeLast();
        numberOfStoredRates -= 1;
    }

    public void updateWRT(float change, String wrt){
        //acumulate 
        float ratioDifference;
        if (wrt.equals("amount")) ratioDifference = assetPriceRule.updateWRTAmountInCirculation(change);
        else if (wrt.equals("hype")) ratioDifference = assetPriceRule.updateWRTHype(change);
        else ratioDifference = assetPriceRule.updateWRTAmountOfOwners(change);
        //System.out.println(ratioDifference);
        this.accumulatedRateChange += ratioDifference;
        
    }

    public void fillAssetSeries(XYChart.Series series, String scale, int longestPlot){
        ArrayList<Float> rates = new ArrayList<Float>(this.rates);
        int difference = longestPlot - numberOfStoredRates;
        if (scale.equals("normal")){
            for(int i = 0; i < numberOfStoredRates; i++){
                series.getData().add(new XYChart.Data(numberOfStoredRates - i - 1 + difference, 1/rates.get(i)));
            }
        }
        else {
            float startingPoint = 1/rates.get(numberOfStoredRates-1);
            for(int i = 0; i < numberOfStoredRates; i++){
                series.getData().add(new XYChart.Data(numberOfStoredRates - i - 1 + difference, 1/rates.get(i)/startingPoint));
            }
        }
        
    }

    public int getNumberOfStoredRates() {
        return this.numberOfStoredRates;
    }
    
}