package market;

import static org.junit.jupiter.api.Assertions.assertEquals;

import market.traders.Company;
import market.traders.HumanInvestor;
import market.traders.Trader;
import market.world.World;
import market.assets.*;
import market.markets.Market;

import java.util.ArrayList;
import java.util.HashMap;

public class ThreadTest {

    static void testThreads() throws InterruptedException {
        System.out.println("SIEMA!");
        World world = new World("nice");
        Currency cur1 = new Currency("zloty", null, "pieczarki", (float) 2);
        world.getWorldContainer().addNewCurrency(cur1);
        Commodity com1 = new Commodity("zloto", null, cur1, (float) 5);
        world.getWorldContainer().addNewCommodity(com1);
        Company company = new Company("hello", null, "company", "2019-10-10", 20, 0, 0, 0,
                new Currency("USD", null, null, 0.5f), false, null);
        Share share = company.getShare();
        world.getWorldContainer().addNewShare(share);

        HashMap<String, Asset> availableCurrencies = new HashMap<String, Asset>();
        HashMap<String, Asset> availableCommodities = new HashMap<String, Asset>();
        HashMap<String, Asset> availableShares = new HashMap<String, Asset>();

        availableCurrencies.put(cur1.getName(), cur1);
        availableCommodities.put(com1.getName(), com1);
        availableShares.put(share.getName(), share);

        Market market = new Market(null, null, null, null, (float) 0.1, cur1, availableCommodities, world);
        Market market2 = new Market(null, null, null, null, (float) 0.1, cur1, availableShares, world);

        ArrayList<TestThread> threads = new ArrayList<TestThread>();
        int numberOfThread = 100;
        int valueBoughtAtOneIteration = 100;
        float availableSharesAmount = share.getAvailableShares();
        for (int i = 0; i < numberOfThread; i++) {
            HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
            investmentBudget.put("zloty", (float) 100000);
            Trader trader = new HumanInvestor(String.valueOf(i), investmentBudget, null, null, true, world);
            threads.add(new TestThread(market, trader, com1, valueBoughtAtOneIteration, market2, share));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(
                com1.amountInCirculationProperty().getValue() + " " + valueBoughtAtOneIteration * numberOfThread);
        System.out.println(com1.amountOfOwnersProperty().getValue() + " " + numberOfThread);

        assertEquals(com1.amountInCirculationProperty().getValue(), valueBoughtAtOneIteration * numberOfThread);
        assertEquals(com1.amountOfOwnersProperty().getValue(), numberOfThread);

        System.out.println(share.getAvailableShares() + " " + availableSharesAmount);
        assertEquals(share.getAvailableShares(), availableSharesAmount);

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

    public TestThread(Market m, Trader t, Asset a, int value, Market shareMarket, Share share) {
        this.m = m;
        this.t = t;
        this.a = a;
        this.value = value;
        this.shareMarket = shareMarket;
        this.share = share;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            m.buy(t, a, value);
            m.sell(t, a, value);

            float availableShares = share.getAvailableShares();
            shareMarket.buy(t, share, availableShares);
            shareMarket.sell(t, share, availableShares);
        }
        // System.out.println("finished!");
        m.buy(t, a, value);
    }

}
