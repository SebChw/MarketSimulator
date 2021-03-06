package market.priceRules;

import java.io.Serializable;

import market.entityCreator.SemiRandomValuesGenerator;

/**
 * Function that given some change in parameters with 50% chance changes the
 * ratio
 */
public class BasicAssetPriceRule implements AssetPriceRule, Serializable {

    /**
     * @param DifferenceInAmountInCircularion amount in circulation before - now
     * @return float change by which price should be updated
     */
    @Override
    public float updateWRTAmountInCirculation(float DifferenceInAmountInCircularion) {
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.05)
            return SemiRandomValuesGenerator.getFromNormal(0, 0.2f);
        return DifferenceInAmountInCircularion / 10000;

    }

    /**
     * @param DifferenceInHype //hype before - hype now
     * @return float change by which price should be updated
     */
    @Override
    public float updateWRTHype(float DifferenceInHype) {
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5) {
            return DifferenceInHype / 1000;
        } else
            return 0;
    }

    /**
     * @param DifferenceInAmountOfOwners Owners before - owners now
     * @return float change by which price should be updated
     */
    @Override
    public float updateWRTAmountOfOwners(float DifferenceInAmountOfOwners) {
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.8) {
            return DifferenceInAmountOfOwners / 1000;
        } else
            return 0;
    }

}
