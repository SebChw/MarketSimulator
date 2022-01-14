package market.transactions;

import market.interfaces.Dealer;
import market.traders.Trader;
import market.world.World;
import market.assets.Asset;

public interface TransactionSystem {
    public void buyOperation(Dealer dealer, Trader trader, Asset wantedAsset, float amount,
            World world);

    public void sellOperation(Dealer dealer, Trader trader, Asset soldAsset, float amount, World world);

}
