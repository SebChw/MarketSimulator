package market.assets;

import market.world.Constants;

/**
 * Class representing commodity In the world.
 */
public class Commodity extends AssetBackedByCurrency {
    private String tradingUnit;

    public Commodity(String name, String tradingUnit, Currency tradingCurrency, Float startingRate) {
        // At the beginning there is no commopdities in circulation everything start
        // when some user buy it.
        super(name, Constants.commodityType, 0, tradingCurrency, startingRate);
        this.tradingUnit = tradingUnit;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return super.toString() + "\ntradingUnit:" + this.tradingUnit;
    }

}
