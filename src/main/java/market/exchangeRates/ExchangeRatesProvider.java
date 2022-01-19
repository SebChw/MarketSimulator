package market.exchangeRates;

import java.io.Serializable;

import market.assets.Asset;
import market.priceRules.*;

import java.util.LinkedList;

import javafx.scene.chart.XYChart;

/**
 * Class containing all information about the rates of particular asset <b>The
 * ratio here is how many of considered asset we can have for one unit of
 * backing asset</b>
 */
public class ExchangeRatesProvider implements Serializable {

    private String nameOfBackingAsset;
    private AssetPriceRule assetPriceRule = new BasicAssetPriceRule();
    private float accumulatedRateChange = 0;
    private int numberOfStoredRates = 0;
    private float minPrice = 1000000;
    private float maxPrice = -5;
    private LinkedList<Float> rates = new LinkedList<Float>(); // This provides exchange rates of backing asset to the
                                                               // particular one. So the smaller it is the more valuable
                                                               // the asset
    private Asset asset;

    public ExchangeRatesProvider(String nameOfBackingAsset, float startingPrice, Asset asset) {
        this.nameOfBackingAsset = nameOfBackingAsset; // Main asset that Backs everything in our system
        this.asset = asset;
    }

    /**
     * Function updating rate and min/max Prices
     * 
     * @param rate
     */
    public void updateRate(Float rate) {
        this.rates.addFirst(rate); // Consider here inserting new rate at the begining so taking it will be O(1).
        float currentRatioToMainAsset = asset.calculateThisToMain(1);
        if (minPrice > currentRatioToMainAsset) {
            minPrice = currentRatioToMainAsset;
        }
        if (maxPrice < currentRatioToMainAsset) {
            maxPrice = currentRatioToMainAsset;
        }
        numberOfStoredRates += 1;
    }

    /**
     * Function updating rate taking into account accumulated Rate Change
     */
    public void updateRate() {

        float currentRate = getRate();
        float updated = 0;
        // If rate is below zero and above we treat it a little bit differently
        if (currentRate < 1 && accumulatedRateChange != 0) {
            updated = currentRate + accumulatedRateChange;
        } else {
            updated = currentRate - accumulatedRateChange;
        }

        // There is possibility that accumulated Rate Change will be so big that current
        // rate will be negative
        // We then neglect such change (not the best option I know)
        // if (asset.getType().equals("Share"))
        // System.out.println(updated);
        if (updated > 0) {
            updateRate(updated);
        } else {
            updateRate(currentRate);
        }
        accumulatedRateChange = 0;
    }

    /**
     * Returns current rate
     * 
     * @return float
     */
    public float getRate() {
        return this.rates.getFirst();
    }

    /**
     * Allows to get rate in the particular moment in time
     * 
     * @param which
     * @return
     */
    public float getRate(int which) {
        return this.rates.get(which);
    }

    /**
     * Remove last rate. May be usefull if we want to somehow restrict size of
     * rates.
     */
    public void removeLastRate() {
        this.rates.removeLast();
        numberOfStoredRates -= 1;
    }

    /**
     * This function accumulates Rate change
     * 
     * @param change difference in some parameter before and after transaction
     * @param wrt    can be amount, hype, owners
     */
    public void updateWRT(float change, String wrt) {
        // acumulate
        float ratioDifference;
        if (wrt.equals("amount"))
            ratioDifference = assetPriceRule.updateWRTAmountInCirculation(change);
        else if (wrt.equals("hype"))
            ratioDifference = assetPriceRule.updateWRTHype(change);
        else
            ratioDifference = assetPriceRule.updateWRTAmountOfOwners(change);
        // System.out.println(ratioDifference);
        this.accumulatedRateChange += ratioDifference;

    }

    /**
     * This function calculate ratio of this particular asset to the Main asset from
     * the beginning as asset was created
     * and add them to the series.
     * 
     * @param series      series to which add the values
     * @param scale       normal or percentage
     * @param longestPlot used to set the offset
     */
    public void fillAssetSeries(XYChart.Series<Number, Number> series, String scale, int longestPlot) {

        int difference = longestPlot - numberOfStoredRates;
        if (scale.equals("normal")) {
            for (int i = 0; i < numberOfStoredRates; i++) {
                series.getData().add(
                        new XYChart.Data<Number, Number>(numberOfStoredRates - i - 1 + difference,
                                asset.calculateThisToMain(1, i)));
            }
        } else {
            float startingPoint = asset.calculateThisToMain(1, numberOfStoredRates - 1);
            for (int i = 0; i < numberOfStoredRates; i++) {
                series.getData().add(
                        new XYChart.Data<Number, Number>(numberOfStoredRates - i - 1 + difference,
                                asset.calculateThisToMain(1, i) / startingPoint));
            }
        }

    }

    /**
     * @return int
     */
    public int getNumberOfStoredRates() {
        return this.numberOfStoredRates;
    }

    /**
     * @return float
     */
    public float getMinPrice() {
        return minPrice;
    }

    /**
     * @return float
     */
    public float getMaxPrice() {
        return maxPrice;
    }

    /**
     * @return String
     */
    public String getNameOfBackingAsset() {
        return nameOfBackingAsset;
    }

    public void setAssetPriceRule(AssetPriceRule assetPriceRule) {
        this.assetPriceRule = assetPriceRule;
    }

}