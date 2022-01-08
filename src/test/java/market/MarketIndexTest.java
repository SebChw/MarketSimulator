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
import market.assets.Asset;
import market.assets.Currency;

import market.assets.marketIndex.*;
import market.markets.*;

public class MarketIndexTest {

    @Test
    void testApp() {

    World world = new World("nice");
    Currency cur1 = new Currency("zloty", null, "pieczarki", (float)2);
    world.addNewCurrency(cur1);

    Company company =  new Company("hello",null,"company","2019-10-10",20,0,0,0, new Currency("USD",null,null ,0.5f),false,null); 
    Share share = company.getShare();
    float availableShare1 = share.getAvailableShares();

    Company company2 =  new Company("hello2",null,"company","2019-10-10",20,0,0,0, new Currency("USD",null,null ,0.5f),false,null); 
    Share share2 = company.getShare();
    float availableShare2 = share2.getAvailableShares();

    ArrayList<Company> companies = new ArrayList<Company>();
    companies.add(company);
    companies.add(company2);

    world.addNewCurrency(cur1);
    world.addNewShare(share);
    world.addNewShare(share2);
    
    

    MarketIndex marketIndex = new MarketIndex("S&P500", "market index", companies, "gold", 1f/100f);

    Trader trader = new HumanInvestor("123123", new HashMap<String,Float>(),null,null,true, null);

    HashMap<String,Asset> availableStocks = new HashMap<String,Asset>();
    availableStocks.put(share.getName(), share);
    
    Market marketWithIndices = new MarketWithIndices(null,null,null,null, (float)0.1,cur1, availableStocks, world);

    marketWithIndices.addNewAsset(marketIndex);

    trader.addBudget(cur1, 100000);

    marketWithIndices.buy(trader, marketIndex, marketIndex.getPossibleAmount());
    marketWithIndices.buy(trader, marketIndex, marketIndex.getPossibleAmount());
    marketWithIndices.buy(trader, marketIndex, marketIndex.getPossibleAmount());

    assertEquals(3, trader.getInvestmentBudget().get("S&P500"));
    assertEquals(3, marketIndex.amountInCirculationProperty().getValue());
    assertEquals(availableShare1 - 3, share.getAvailableShares());
    assertEquals(availableShare2 - 3, share2.getAvailableShares());
    }
}
