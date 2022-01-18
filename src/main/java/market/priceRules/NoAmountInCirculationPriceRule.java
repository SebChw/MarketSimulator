package market.priceRules;

import java.io.Serializable;

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
        return DifferenceInHype / 1000;
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
