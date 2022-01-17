package market.exchangeRates;

import java.io.Serializable;
import java.util.ArrayList;

import market.priceRules.*;

import java.util.LinkedList;

import javafx.scene.chart.XYChart;

public class ExchangeRatesProvider implements Serializable {

    private String nameOfBackingAsset;
    private AssetPriceRule assetPriceRule = new BasicAssetPriceRule();
    private float accumulatedRateChange = 0;
    private int numberOfStoredRates = 0;
    private float minPrice = -5;
    private float maxPrice = 10000;
    private LinkedList<Float> rates = new LinkedList<Float>(); // This provides exchange rates of one asset to all other

    public ExchangeRatesProvider(String nameOfBackingAsset, float startingPrice) {
        this.nameOfBackingAsset = nameOfBackingAsset; // Main asset that Backs everything in our system

        if (startingPrice != 0)
            this.updateRate(startingPrice);
    }

    /**
     * @param rate
     */
    public void updateRate(Float rate) {
        this.rates.addFirst(rate); // Consider here inserting new rate at the begining so taking it will be O(1).
        if (minPrice < rate) {
            minPrice = rate;
        }
        if (maxPrice > rate) {
            maxPrice = rate;
        }
        numberOfStoredRates += 1;
    }

    public void updateRate() {
        // ! DEFINE RULES OF UPDATING MARKET INDICES and INVESTMENT FUNDS UNITS!
        float currentRate = getRate();
        float updated = 0;
        if (currentRate < 1 && accumulatedRateChange != 0) {
            updated = 1 / (1 / currentRate - 1 / accumulatedRateChange);
        } else {
            updated = currentRate + accumulatedRateChange;
        }

        if (updated > 0) {
            updateRate(updated);
        }
        accumulatedRateChange = 0;
    }

    /**
     * @return float
     */
    public float getRate() {
        return this.rates.getFirst(); // !Consider using GetFirst here and appending new raters at the beginning!
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
        ArrayList<Float> rates = new ArrayList<Float>(this.rates);
        int difference = longestPlot - numberOfStoredRates;
        if (scale.equals("normal")) {
            for (int i = 0; i < numberOfStoredRates; i++) {
                series.getData().add(
                        new XYChart.Data<Number, Number>(numberOfStoredRates - i - 1 + difference, 1 / rates.get(i)));
            }
        } else {
            float startingPoint = 1 / rates.get(numberOfStoredRates - 1);
            for (int i = 0; i < numberOfStoredRates; i++) {
                series.getData().add(
                        new XYChart.Data<Number, Number>(numberOfStoredRates - i - 1 + difference,
                                1 / rates.get(i) / startingPoint));
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
        return 1 / minPrice;
    }

    /**
     * @return float
     */
    public float getMaxPrice() {
        return 1 / maxPrice;
    }

    /**
     * @return float
     */
    public float getCurrentPrice() {
        return 1 / getRate();
    }

    /**
     * @return String
     */
    public String getNameOfBackingAsset() {
        return nameOfBackingAsset;
    }

}