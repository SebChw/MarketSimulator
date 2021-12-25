package market.marketRules;

import market.assets.Asset;

public class ShareMarketPriceRule implements MarketPriceRule {

    @Override
    public float adjustPrice(int hypeLevel, int amountOfOwners) {
        return (float)0.01;
    }
    
}
