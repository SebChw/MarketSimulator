package market.assets;

/**
 * In general if something is Backed by some particular currency. We can buy it
 * only for that currency.
 * So Calculating ratio works a little bit differently
 */
public abstract class AssetBackedByCurrency extends Asset {
    private Currency tradingCurrency;

    public AssetBackedByCurrency(String name, String type, float amountInCirculation, Currency tradingCurrency,
            Float startingRate) {
        super(name, type, amountInCirculation, tradingCurrency.getName(), startingRate);
        this.tradingCurrency = tradingCurrency;

    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return super.toString() + "\ntradingCurrency:" + this.tradingCurrency.getName();
    }

    /**
     * @param amount
     * @return float
     */
    @Override
    public float calculateThisToMain(float amount) {
        // Here we must do This asset -> this Asset's Currency -> Main Backing Currency
        float thisAssetToThisCurrency = amount * this.getMainBankRates().getRate();
        return this.tradingCurrency.calculateThisToMain(thisAssetToThisCurrency);
    }

    /**
     * @param amount
     * @return float
     */
    @Override
    public float calculateMainToThis(float amount) {
        // Here we must do Main -> this Asset's Currency -> This asset
        float mainToThisCurrency = this.tradingCurrency.calculateMainToThis(amount);
        return mainToThisCurrency * 1 / this.getMainBankRates().getRate();
    }
}
