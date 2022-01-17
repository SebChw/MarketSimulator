package market;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import market.traders.Company;
import market.traders.HumanInvestor;
import market.traders.Trader;
import market.world.World;
import market.assets.Share;
import market.assets.marketIndex.MarketIndex;
import market.markets.MarketWithIndices;
import market.assets.Asset;
import market.assets.Currency;

public class ShareTest {

    @Test
    void testApp() {
        // THis works as expected so maybe threads are messing around?
        Company company = new Company("hello", null, "company", "2019-10-10", 20, 0, 0, 0,
                new Currency("USD", null, null, 0.5f), false, null);
        Share share = company.getShare();
        Trader trader = new HumanInvestor("123123", new HashMap<String, Float>(), null, null, true, null);
        float availableShares = share.getAvailableShares();
        assertEquals(true, share.canBeBought(availableShares)); // During checking it is subtracted!
        share.unfreeze(availableShares);
        assertEquals(false, share.canBeBought(availableShares + 1));

        share.canBeBought(availableShares);
        trader.addBudget(share, availableShares);

        assertEquals(0, share.getAvailableShares());

        trader.subtractBudget(share, availableShares);

        assertEquals(availableShares, share.getAvailableShares());
        assertEquals(null, trader.getInvestmentBudget().get("hello"));

        Company company1 = new Company("hello1", null, "company", "2019-10-10", 20, 0, 0, 0,
                new Currency("USD", null, null, 0.5f), false, null);
        float availableShares1 = company1.getShare().getAvailableShares();

        Company company2 = new Company("hello2", null, "company", "2019-10-10", 20, 0, 0, 0,
                new Currency("USD", null, null, 0.5f), false, null);
        float availableShares2 = company2.getShare().getAvailableShares();

        ArrayList<Company> companyList = new ArrayList<Company>();
        companyList.add(company1);
        companyList.add(company2);

        MarketIndex index = new MarketIndex("index", companyList, "gold");

        Currency cur1 = new Currency("zloty", null, "pieczarki", (float) 2);
        HashMap<String, Asset> availableIndices = new HashMap<String, Asset>();
        World world = new World("spice");
        availableIndices.put(index.getName(), index);
        world.getWorldContainer().addNewMarketIndex(index);
        MarketWithIndices marketWithIndices = new MarketWithIndices(null, null, null, null, (float) 0.1, cur1,
                availableIndices, world);
        trader.addBudget(cur1, 10000000f);

        marketWithIndices.buy(trader, index, index.getPossibleAmount());
        // Now he should be able to buy it. And it works fine
        assertEquals(trader.getInvestmentBudget().get("index"), 1);
        assertEquals(company1.getShare().getAvailableShares(), availableShares1 - 1);
        assertEquals(company2.getShare().getAvailableShares(), availableShares2 - 1);

        company2.increaseShares(-(int) company2.getShare().getAvailableShares()); // Now he shouldn't be able to buy

        marketWithIndices.buy(trader, index, index.getPossibleAmount()); // And it still works fine. So maybe threads
                                                                         // are messing around

        assertEquals(trader.getInvestmentBudget().get("index"), 1);
        assertEquals(company1.getShare().getAvailableShares(), availableShares1 - 1);
        assertEquals(company2.getShare().getAvailableShares(), 0);
    }
}
