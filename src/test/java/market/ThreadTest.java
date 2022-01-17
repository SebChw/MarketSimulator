package market;

import static org.junit.jupiter.api.Assertions.assertEquals;

import market.traders.Company;
import market.traders.HumanInvestor;
import market.traders.Trader;
import market.world.World;
import market.assets.*;
import market.assets.marketIndex.MarketIndex;
import market.markets.Market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ThreadTest {
    // In that test I create some currencies/commodities/shares/marketindices and
    // run some threads to buy this goods randomly
    // After that I change whether everything is consistent
    static void testThreads() throws InterruptedException {
        World world = new World("nice");

        Currency cur1 = new Currency("zloty", null, "pieczarki", (float) 2);
        world.getWorldContainer().addNewCurrency(cur1);

        Commodity com1 = new Commodity("zloto", null, cur1, (float) 5);
        world.getWorldContainer().addNewCommodity(com1);

        Company company = new Company("hello", null, "company", "2019-10-10", 20, 0, 0, 0,
                new Currency("USD", null, null, 0.5f), false, null);
        company.increaseShares(10000);
        Share share = company.getShare();

        Company company2 = new Company("hello2", null, "company", "2019-10-10", 20, 0, 0, 0,
                new Currency("USD", null, null, 0.5f), false, null);
        company2.increaseShares(10000);
        Share share2 = company2.getShare();

        Company company3 = new Company("hello3", null, "company", "2019-10-10", 20, 0, 0, 0,
                new Currency("USD", null, null, 0.5f), false, null);
        company3.increaseShares(10000);
        Share share3 = company3.getShare();

        ArrayList<Company> companies = new ArrayList<Company>();
        companies.add(company2);
        companies.add(company3);
        companies.add(company);

        MarketIndex index = new MarketIndex("index", companies, "gold");

        world.getWorldContainer().addNewShare(share);
        world.getWorldContainer().addNewShare(share2);
        world.getWorldContainer().addNewShare(share3);
        world.getWorldContainer().addNewMarketIndex(index);

        HashMap<String, Asset> availableCurrencies = new HashMap<String, Asset>();
        HashMap<String, Asset> availableCommodities = new HashMap<String, Asset>();
        HashMap<String, Asset> availableShares = new HashMap<String, Asset>();

        availableCurrencies.put(cur1.getName(), cur1);
        availableCommodities.put(com1.getName(), com1);
        availableShares.put(share.getName(), share);
        availableShares.put(index.getName(), index);

        Market market = new Market(null, null, null, null, (float) 0.1, cur1, availableCommodities, world);
        Market market2 = new Market(null, null, null, null, (float) 0.1, cur1, availableShares, world);

        ArrayList<TestThread> threads = new ArrayList<TestThread>();
        int numberOfThread = 100;
        int valueBoughtAtOneIteration = 100;

        for (int i = 0; i < numberOfThread; i++) {
            HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
            investmentBudget.put("zloty", (float) 10000000);
            Trader trader = new HumanInvestor(String.valueOf(i), investmentBudget, null, null, true, world);
            threads.add(new TestThread(market, trader, com1, valueBoughtAtOneIteration, market2, share, index));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println(
                com1.amountInCirculationProperty().getValue() + " " +
                        valueBoughtAtOneIteration * numberOfThread);
        System.out.println(com1.amountOfOwnersProperty().getValue() + " " +
                numberOfThread);

        assertEquals(com1.amountInCirculationProperty().getValue(),
                valueBoughtAtOneIteration * numberOfThread);
        assertEquals(com1.amountOfOwnersProperty().getValue(), numberOfThread);

        System.out.println("Available shares after that time: " + share.getAvailableShares());
        System.out.println("Shares in circulation: " + share.amountInCirculationProperty().floatValue());
        System.out.println("Issued Shared: " + share.getIssuedShares());

        System.out.println("Indices in circulation: " + index.amountInCirculationProperty().floatValue());
        assertEquals(share.getAvailableShares() +
                share.amountInCirculationProperty().floatValue(),
                share.getIssuedShares());

        assertEquals(share2.getAvailableShares() +
                share2.amountInCirculationProperty().floatValue(),
                share2.getIssuedShares());

        assertEquals(share3.getAvailableShares() +
                share3.amountInCirculationProperty().floatValue(),
                share3.getIssuedShares());

    }

    public static void main(String[] args) throws Exception {
        testThreads();
    }
}

class TestThread extends Thread {

    private Market m;
    private Market shareMarket;
    private Share share;
    private Trader t;
    private Asset a;
    private int value;
    private MarketIndex index;

    public TestThread(Market m, Trader t, Asset a, int value, Market shareMarket, Share share, MarketIndex index) {
        this.m = m;
        this.t = t;
        this.a = a;
        this.value = value;
        this.shareMarket = shareMarket;
        this.share = share;
        this.index = index;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            m.buy(t, a, value);
            m.sell(t, a, value);

            if (!Objects.isNull(t.getInvestmentBudget().get(share.getName())))
                shareMarket.sell(t, share, share.getPossibleAmount(t.getInvestmentBudget().get(share.getName())));

            if (!Objects.isNull(t.getInvestmentBudget().get(index.getName())))
                shareMarket.sell(t, index, t.getInvestmentBudget().get(index.getName()));

            shareMarket.buy(t, index, index.getPossibleAmount());
            shareMarket.buy(t, share, share.getPossibleAmount());

        }
        if (!Objects.isNull(t.getInvestmentBudget().get(share.getName())))
            shareMarket.sell(t, share,
                    share.getPossibleAmount(t.getInvestmentBudget().get(share.getName())));

        m.buy(t, a, value);
    }

}
