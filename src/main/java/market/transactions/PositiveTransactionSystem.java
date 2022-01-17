package market.transactions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import market.assets.Asset;
import market.interfaces.Dealer;
import market.traders.Trader;
import market.world.World;

public class PositiveTransactionSystem implements TransactionSystem, Serializable {
    /**
     * This function at first check if Trader has enough of Trading Tcurrency to buy
     * wanted Asset -> If not It exchanges traders assets for trading currency until
     * he has enough of tradingCurrency
     * If it is still not enough it sell to the trader that amount on what they have
     * enough money.
     * 
     * @param trader
     * @param wantedAsset
     * @param amount
     */
    @Override
    public void buyOperation(Dealer dealer, Trader trader, Asset wantedAsset, float amount,
            World world) {
        if (!dealer.getAvailableAssetsHashMap().containsKey(wantedAsset.getName())) {
            System.out.println("We don't have such asset here!");
            return;
        }
        if (!wantedAsset.canBeBought(amount)) {
            return;
        }
        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        String tradingCurrencyName = dealer.getTradingCurrency().getName();
        // how much we must pay of trading Currency for wanted Asset
        float cost = dealer.getTradingCurrency().calculateDifferentToThis(wantedAsset, amount);
        cost += cost * dealer.getPercentageOperationCost(); // Increased bye percentage Operation Cost

        if (traderInvestmentBudget.containsKey(tradingCurrencyName)
                && traderInvestmentBudget.get(tradingCurrencyName) >= cost) {
            trader.addBudget(wantedAsset, amount);
            trader.subtractBudget(dealer.getTradingCurrency(), cost);
        } else {
            float tradingCurrencyAmount = 0;
            HashMap<String, Float> traderInvestmentBudgetCopy = new HashMap<String, Float>(
                    trader.getInvestmentBudget());
            // I Need copy as I will change values within loop!
            Iterator<Map.Entry<String, Float>> it = traderInvestmentBudgetCopy.entrySet().iterator();
            Map.Entry<String, Float> budget = null;
            while (tradingCurrencyAmount < cost && it.hasNext()) {
                budget = it.next();
                // !These two must be in that order
                tradingCurrencyAmount += dealer.getTradingCurrency()
                        .calculateDifferentToThis(world.getParticularAsset(budget.getKey()), budget.getValue());
                trader.subtractBudget(world.getParticularAsset(budget.getKey()), budget.getValue());
            }

            // After leaving this loop we still has 3 different options
            if (tradingCurrencyAmount > cost) {
                // Enough Money!
                tradingCurrencyAmount -= cost;
                trader.addBudget(wantedAsset, amount);
                trader.addBudget(dealer.getTradingCurrency(), tradingCurrencyAmount); // returning what is left back
            } else if (tradingCurrencyAmount < cost) {
                // Now I treat tradingCurrencyAmount as cost + provision. So I need to get cost
                // back. And this is how much trader has
                tradingCurrencyAmount = tradingCurrencyAmount / (1 + dealer.getPercentageOperationCost());

                float newAmount = dealer.getTradingCurrency().calculateThisToDifferent(wantedAsset,
                        tradingCurrencyAmount);
                wantedAsset.unfreeze(amount - newAmount); // ! For sure at this moment amount > newAmount
                // ! In case of Integer based Assets this may lead to some problem e.g we have
                // money for 0.5 Share but we can't buy that amount! so that we may end with 0
                // and should get return of money!
                cost = dealer.getTradingCurrency().calculateDifferentToThis(wantedAsset, newAmount);

                trader.addBudget(wantedAsset, newAmount);
                trader.addBudget(dealer.getTradingCurrency(), tradingCurrencyAmount - cost);
            } else {
                // If they are equal I just add wantedAsset -> Enough money
                trader.addBudget(wantedAsset, amount);
            }
        }

    }

    @Override
    public void sellOperation(Dealer dealer, Trader trader, Asset soldAsset, float amount, World world) {
        HashMap<String, Float> traderInvestmentBudget = trader.getInvestmentBudget();
        // That asset must be available in both Market and in Trader and trader must
        // have enough of that asset!
        if (traderInvestmentBudget.containsKey(soldAsset.getName())
                && traderInvestmentBudget.get(soldAsset.getName()) >= amount
                && dealer.getAvailableAssetsHashMap().containsKey(soldAsset.getName())) {
            float income = dealer.getTradingCurrency().calculateDifferentToThis(soldAsset, amount);
            income = income / (1 + dealer.getPercentageOperationCost());
            trader.addBudget(dealer.getTradingCurrency(), income);
            trader.subtractBudget(soldAsset, amount);
        } else {
            System.out.println("Either you don't have asset to be sold or this Market does not support it!");
        }

    }
}
