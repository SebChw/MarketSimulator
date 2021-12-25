package market.marketRules;

import market.assets.Asset;

public interface MarketPriceRule {
    float adjustPrice(int hypeLevel, int amountOfOwners);
}
