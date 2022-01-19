package market.priceRules;

import java.io.Serializable;

import market.entityCreator.SemiRandomValuesGenerator;

/**
 * Price rule that doesn't take amount in circulation into account while
 * updating the rate.
 */
public class NoAmountInCirculationPriceRule implements AssetPriceRule, Serializable {
    /**
     * @param DifferenceInAmountInCircularion amount in circulation before - now
     * @return float change by which price should be updated
     */
    @Override
    public float updateWRTAmountInCirculation(float DifferenceInAmountInCircularion) {
        return 0;
    }

    /**
     * @param DifferenceInHype //hype before - hype now
     * @return float change by which price should be updated
     */
    @Override
    public float updateWRTHype(float DifferenceInHype) {
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5) {
            return DifferenceInHype / 100;
        } else
            return 0;
    }

    /**
     * @param DifferenceInAmountOfOwners Owners before - owners now
     * @return float change by which price should be updated
     */
    @Override
    public float updateWRTAmountOfOwners(float DifferenceInAmountOfOwners) {
        return DifferenceInAmountOfOwners / 1000;
    }
}
