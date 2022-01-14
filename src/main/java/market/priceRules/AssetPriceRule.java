package market.priceRules;

/**
 * Interface defining what operations should any object responsible for
 * regulating the Prices for Assets implement
 */
public interface AssetPriceRule {
    abstract float updateWRTAmountInCirculation(float DifferenceInAmountInCircularion);

    abstract float updateWRTHype(float DifferenceInHype);

    abstract float updateWRTAmountOfOwners(float DifferenceInAmountOfOwners);
}
