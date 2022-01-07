package market;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import market.traders.Company;
import market.traders.HumanInvestor;
import market.traders.Trader;
import market.assets.Share;
import market.assets.Currency;
public class ShareTest {

    @Test
    void testApp() {
        //THis works as expected so maybe threads are messing around?
        Company company =  new Company("hello",null,"company","2019-10-10",20,0,0,0, new Currency("USD",null,null ,0.5f),false,null); 
        Share share = company.getShare();
        Trader trader = new HumanInvestor("123123", new HashMap<String,Float>(),null,null,true, null);
        float availableShares = share.getAvailableShares();
        assertEquals(true, share.canBeBought(availableShares)); //During checking it is subtracted!
        share.unfreeze(availableShares);
        assertEquals(false, share.canBeBought(availableShares+1));

        share.canBeBought(availableShares);
        trader.addBudget(share, availableShares);
        
        assertEquals(0, share.getAvailableShares());

        trader.subtractBudget(share, availableShares);

        assertEquals(availableShares, share.getAvailableShares());
        assertEquals(null, trader.getInvestmentBudget().get("hello"));
    }
}
