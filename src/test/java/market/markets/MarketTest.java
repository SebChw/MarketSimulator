package market.markets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import market.traders.HumanInvestor;
import market.traders.Trader;
import market.world.World;
import market.assets.*;
import java.util.HashMap;

public class MarketTest {
    @Test
    // IF THIS TEST IS PASSED THEN MECHANICS OF SELLING AND BUYING TRADES WORKS WELL
    // IN ALL SCENARIOS I COULD IMAGINE!
    // And check if amount in Circulation updates correctly
    void testBuyAndSell() {
        World world = new World("nice");
        Currency cur1 = new Currency("zloty", null, "pieczarki", (float) 2);
        Currency cur2 = new Currency("yang", null, "pieczarki", (float) 0.5);
        Commodity com1 = new Commodity("zloto", null, cur2, (float) 1 / 5);
        Commodity com2 = new Commodity("srebro", null, cur1, (float) 1 / 2);
        HashMap<String, Asset> availableCurrencies = new HashMap<String, Asset>();
        HashMap<String, Asset> availableCommodities = new HashMap<String, Asset>();
        availableCurrencies.put(cur1.getName(), cur1);
        availableCurrencies.put(cur2.getName(), cur2);
        availableCommodities.put(com1.getName(), com1);
        availableCommodities.put(com2.getName(), com2);
        world.getWorldContainer().addNewCurrency(cur1); // This is so that market can infer it's Trading Asset type!
        world.getWorldContainer().addNewCurrency(cur2); // This is so that market can infer it's Trading Asset type!
        world.getWorldContainer().addNewCommodity(com1);
        world.getWorldContainer().addNewCommodity(com2);
        Market market = new Market(null, null, null, null, (float) 0.1, cur1, availableCurrencies, world);
        Market market2 = new Market(null, null, null, null, (float) 0.1, cur2, availableCommodities, world);
        HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
        // investmentBudget.put("zloty", (float)10);
        // investmentBudget.put("yang", (float)10);
        // investmentBudget.put("zloto", (float)10);
        // investmentBudget.put("srebro", (float)10);

        Trader trader = new HumanInvestor("123123", investmentBudget, null, null, true, world);
        trader.addBudget(cur1, 10f);
        trader.addBudget(cur2, 10f);
        trader.addBudget(com1, 10f);
        trader.addBudget(com2, 10f);

        assertEquals(cur1.amountInCirculationProperty().getValue(), 10);
        assertEquals(cur2.amountInCirculationProperty().getValue(), 10);
        assertEquals(com1.amountInCirculationProperty().getValue(), 10);
        assertEquals(com2.amountInCirculationProperty().getValue(), 10);

        market.buy(trader, cur2, 1); // I want to get 1 yang from this market. In Market Trading Currency is zloty so
        // The cost of this operation will be 4 zloty and + 0.1 provision = 4.4 zlotych
        // So ill be left with 5,6 zloty and 11 yangs

        assertEquals((float) 5.6, investmentBudget.get("zloty"));
        assertEquals((float) 11.0, investmentBudget.get("yang"));

        assertEquals(cur1.amountInCirculationProperty().getValue(), 5.6f);
        assertEquals(cur2.amountInCirculationProperty().getValue(), 11.0f);

        // Now let's try to exchange one commodity on another with mix of trading
        // Currencies
        market2.buy(trader, com2, 1); // I want to get 1 silver from this market I need to exchange silver to the
                                      // currency. That is used on that market
        // so Ill get 2 zloty for srebro, then get 1 of main currency, then 0.5 yang. So
        // I must pay 0.55 of yangs.

        assertEquals((float) 10.45, investmentBudget.get("yang"));
        assertEquals((float) 11.0, investmentBudget.get("srebro"));

        assertEquals(cur2.amountInCirculationProperty().getValue(), 10.45f);
        assertEquals(com2.amountInCirculationProperty().getValue(), 11.0f);

        // Everything seems fine!! Now lets test situation when I don't have trading
        // currency on that market.
        trader.subtractBudget(cur2, investmentBudget.get("yang").floatValue());
        trader.subtractBudget(com1, investmentBudget.get("zloto").floatValue());
        trader.subtractBudget(com2, investmentBudget.get("srebro").floatValue());
        assertEquals(null, investmentBudget.get("yang"));
        assertEquals(null, investmentBudget.get("zloto"));
        assertEquals(null, investmentBudget.get("srebro"));

        trader.addBudget(cur1, 16.4f);
        // Unfortunatelly I don;t know the order of iterations within map So I need to
        // leave only one thing here
        // Okay So I want to buy in the market where trading Currency is yang but I only
        // have zloty.
        // And I want 1 zloto so I need 5 yangs + provision -> 5,5 So I need 11 zloty So
        // I should have 0 zloty and It should dissapear from the budget!

        market2.buy(trader, com1, 1);

        assertEquals(null, investmentBudget.get("zloty"));
        assertEquals(1, investmentBudget.get("zloto"));
        assertEquals(cur1.amountInCirculationProperty().getValue(), 0f);

        // Now last possible situation. I have not enough money at all to buy something.
        trader.addBudget(com2, (float) 1); // I have already 1 zloto

        // Okay So I want to buy yangs And I have only zloto and srebro Ill recalculate
        // it into zloty and then buy yangs.
        // overall this operation costs 110 zloty
        // For 1 zloto I have 5 yangs so 20 zloty
        // For 1 srebro I have 2 zloty.
        // So I have 22 zloty to spend So I can get 20 zloty for that since provision
        // then will be 2 zloty
        // So after all I have no zloty and srebro and 5 yangs
        market.buy(trader, cur2, 25);

        assertEquals(null, investmentBudget.get("zloto"));
        assertEquals(null, investmentBudget.get("zloty"));
        assertEquals(null, investmentBudget.get("srebro"));
        assertEquals((float) 5, investmentBudget.get("yang"));

        assertEquals(com1.amountInCirculationProperty().getValue(), 0f);
        assertEquals(com2.amountInCirculationProperty().getValue(), 0f);
        assertEquals(cur2.amountInCirculationProperty().getValue(), 5f);

        // Now situation when I don't have currency But I got more than of another.
        // I have 5 yangs and I want to buy 3 yangs. On that market I cant buy with
        // zlotys.
        // Cost of getting 3 yangs is 12 zloty + provision = 13.2 .
        // So I exchange 5 yangs -> 20 zloty So I should be left with 6.8 zloty and 3
        // yangs
        market.buy(trader, cur2, 3);
        assertEquals((float) 3, investmentBudget.get("yang"));
        assertEquals((float) 6.8, investmentBudget.get("zloty"));

        assertEquals(cur2.amountInCirculationProperty().getValue(), 3f);
        assertEquals(cur1.amountInCirculationProperty().getValue(), 6.8f);

        // And Last possible Situation We have How many is needed
        // I want to buy 0,8545454545454545 gold. Having 3 yangs and 6.8 zlotys and the
        // currency is yang.
        // So for 3 yangs I get 3 yangs and for 6.8 zloty I get 1.7 yangs so I have 4.7
        // yangs. So by subtracting operation cost
        // I get 4,272727272727273 of yangs available.
        market2.buy(trader, com1, (float) 0.8545454545454545);

        assertEquals((float) 85, Math.round(investmentBudget.get("zloto") * 100)); // 0.85 since floating point
                                                                                   // precision may mess up now
        assertEquals(null, investmentBudget.get("yang"));
        assertEquals(null, investmentBudget.get("zloty"));

        // Now lets test easier sell operation.
        investmentBudget.put("yang", (float) 1);
        market.sell(trader, cur2, (float) 0.5); // I should get 2 zloty minus percentageOperationCost so 1.8181818

        assertEquals((float) 1.8181818, investmentBudget.get("zloty"));
        assertEquals((float) 0.5, investmentBudget.get("yang"));

        market.sell(trader, com2, (float) 0.5); // Nothing should change as I can'y buy commodities on that market
        assertEquals((float) 0.5, investmentBudget.get("yang"));
        assertEquals(null, investmentBudget.get("srebro"));

        market.sell(trader, cur2, (float) 1); // Nothing should change as I don't have enough yangs to sell
        assertEquals((float) 0.5, investmentBudget.get("yang"));
        assertEquals((float) 1.8181818, investmentBudget.get("zloty"));
    }
}
