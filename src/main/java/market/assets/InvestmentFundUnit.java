package market.assets;

import market.entityCreator.SemiRandomValuesGenerator;
import market.traders.InvestmentFund;
import market.world.Constants;
import market.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Class representing Investment Fund Unit in our World */

public class InvestmentFundUnit extends AssetBackedByCurrency {
    private InvestmentFund issuedBy;
    private HashMap<String, Float> boughtAssets; // User will get some proportional amount of earned money by that unit.
    private World world;

    public InvestmentFundUnit(String name, InvestmentFund issuedBy,
            HashMap<String, Float> boughtAssets, World world) {
        super(name, Constants.investmentFundUnitType, 0, issuedBy.getRegisteredCurrency(), 0f);
        this.issuedBy = issuedBy;
        this.boughtAssets = boughtAssets;
        this.world = world;

        updateRate();

    }

    /**
     * @param amount
     * @return float
     */
    public float getPossibleAmount(float amount) {
        return SemiRandomValuesGenerator.getRandomIntNumber((int) amount);
    }

    /**
     * @param amount
     * @return float
     */
    public float getPossibleAmount() {
        return SemiRandomValuesGenerator.getRandomIntNumber(5);
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return super.toString() + "\nIssued By: " + issuedBy.getName() + "\nAssets in Unit: " + boughtAssets;
    }

    /**
     * Here we calculate Ratio as the sum of all ratios of assets contained in that
     * particular unit
     * 
     * @return float
     */
    public float calculateRatio() {
        float ratio = 0;
        Iterator<Map.Entry<String, Float>> it = boughtAssets.entrySet().iterator();
        Map.Entry<String, Float> asset = null;
        while (it.hasNext()) {
            asset = it.next();
            ratio += world.getParticularAsset(asset.getKey()).calculateThisToDifferent(issuedBy.getRegisteredCurrency(),
                    asset.getValue());
        }
        return 1 / ratio;
    }

    public void updateRate() {
        this.getMainBankRates().updateRate(calculateRatio());
    }
}
