package market.priceRules;

import market.entityCreator.SemiRandomValuesGenerator;
public class BasicAssetPriceRule implements AssetPriceRule {

    @Override
    public float updateWRTAmountInCirculation(float DifferenceInAmountInCircularion) {
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5){
            return  - DifferenceInAmountInCircularion / 10000;
        }
        else return 0;
    }

    @Override
    public float updateWRTHype(float DifferenceInHype) {
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5){
            return - DifferenceInHype/1000;
        }
        else return 0;
    }

    @Override
    public float updateWRTAmountOfOwners(float DifferenceInAmountOfOwners) {
        if (SemiRandomValuesGenerator.getRandomFloatNumber(1) < 0.5){
            return - DifferenceInAmountOfOwners/100;
        }
        else return 0;
    }
    
}
