package market.exchangeRates;

import java.io.Serializable;

import market.assets.Asset;
import market.priceRules.*;

import java.util.LinkedList;

import javafx.scene.chart.XYChart;

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

    public void updateRate() {
        // ! DEFINE RULES OF UPDATING MARKET INDICES and INVESTMENT FUNDS UNITS!
        float currentRate = getRate();
        float updated = 0;
        if (currentRate < 1 && accumulatedRateChange != 0) {
            updated = 1 / (1 / currentRate + accumulatedRateChange);
        } else {
            updated = currentRate - accumulatedRateChange;
        }

        if (updated > 0) {
            updateRate(updated);
        } else {
            updateRate(currentRate);
        }
        accumulatedRateChange = 0;
    }

    /**
     * @return float
     */
    public float getRate() {
        return this.rates.getFirst(); // !Consider using GetFirst here and appending new raters at the beginning!
    }

    public float getRate(int which) {
        return this.rates.get(which); // !Consider using GetFirst here and appending new raters at the beginning!
    }

    public void removeLastRate() {
        this.rates.removeLast();
        numberOfStoredRates -= 1;
    }

    /**
     * @param change
     * @param wrt
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
     * @param series
     * @param scale
     * @param longestPlot
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