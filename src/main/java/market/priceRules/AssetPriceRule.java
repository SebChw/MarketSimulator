package market.priceRules;

import market.assets.Asset;

public interface AssetPriceRule {
    abstract float updateWRTAmountInCirculation(float PercentageDifferenceInAmountInCircularion);
    abstract float updateWRTHype(float PercentageDifferenceInHype);
    abstract float updateWRTAmountOfOwners(float PercentageDifferenceInAmountOfOwners);
}
