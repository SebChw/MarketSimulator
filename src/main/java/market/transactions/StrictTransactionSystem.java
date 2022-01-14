package market.transactions;

import java.util.HashMap;

import market.assets.Asset;
import market.interfaces.Dealer;
import market.traders.Trader;
import market.world.World;

public class StrictTransactionSystem implements TransactionSystem {

    /**
     * Before every transaction we nned to check if trader is a human Investor and
     * if we have Unit that he wants
     * 
     * @param trader      trader trying to perform transaction
     * @param wantedAsset unit they want
     * @return boolean decision whether to allow trader for transaction
     */
    public boolean checkConstraints(Trader trader, Asset wantedAsset, HashMap<String, Asset> availableAssets) {

        if (!trader.getType().equals("human Investor")) {
            System.out.println("Only Human Investors Can buy here!!");
            return false;
        }

        if (!availableAssets.containsKey(wantedAsset.getName())) {
            System.out.println("We don't have such asset here!");
            return false;
        }

        return true;
    }

    /**
     * Fund unit is more restricted than Market. We can buy only if we have needed
     * currency and needed amount.
     * Probably I could use strategy design pattern here
     * 
     * @param trader     trader wanted to trade here
     * @param wantedUnit wanted unit
     * @param amount     how many units they want
     */
    @Override
    public void buyOperation(Dealer dealer, Trader trader, Asset wantedAsset, float amount, World world) {
        if (!checkConstraints(trader, wantedAsset, dealer.getAvailableAssetsHashMap()))
            return;

        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        String registeredCurrencyName = dealer.getTradingCurrency().getName();
        float cost = dealer.getTradingCurrency().calculateDifferentToThis(wantedAsset, amount);

        if (traderInvestmentBudget.containsKey(registeredCurrencyName)
                && traderInvestmentBudget.get(registeredCurrencyName) >= cost) {
            trader.addBudget(wantedAsset, amount);
            trader.subtractBudget(dealer.getTradingCurrency(), cost);
        } else {
            System.out.println("Not enough money to buy on investment fund! Or you just don't have our currency!");
        }

    }

    @Override
    public void sellOperation(Dealer dealer, Trader trader, Asset soldAsset, float amount, World world) {
        if (!checkConstraints(trader, soldAsset, dealer.getAvailableAssetsHashMap()))
            return;

        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        if (traderInvestmentBudget.containsKey(soldAsset.getName())) {
            float income = dealer.getTradingCurrency().calculateDifferentToThis(soldAsset, amount);
            trader.addBudget(dealer.getTradingCurrency(), income);
            trader.subtractBudget(soldAsset, amount);
        }

    }

}
