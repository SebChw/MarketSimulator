package market.entityCreator;

import market.traders.*;
import market.world.World;
import market.assets.Currency;

import java.io.Serializable;
import java.util.*;
import market.gui.MainPanelController;
import market.assets.marketIndex.*;

/**
 * Function which we can use to create new Traders
 */
public class TraderFactory implements Serializable {
    private SemiRandomValuesGenerator attributesGenerator;

    private World world;

    public TraderFactory(World world) {
        this.world = world;
        this.attributesGenerator = new SemiRandomValuesGenerator(world);
    }

    /**
     * @param currenciesByNow to fill investment budget with something
     * @param dynamicIndices  this is needed so that Indices are notified that new
     *                        company has came in
     * @return Company
     */
    public Company createCompany(ArrayList<Currency> currenciesByNow, ArrayList<DynamicMarketIndex> dynamicIndices) {
        HashMap<String, String> company = this.attributesGenerator.getRandomCompanyData();
        String tradingIdentifier = company.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
        String name = company.get("name");
        String ipoDate = company.get("ipoDate");
        Float ipoShareValue = SemiRandomValuesGenerator.getRandomFloatNumber(100, 0.1f) + 100;
        float openingPrice = SemiRandomValuesGenerator.getRandomFloatNumber(10000, 0.1f) + 100;
        float profit = SemiRandomValuesGenerator.getRandomFloatNumber(10000, 0.1f) + 100;
        float revenue = SemiRandomValuesGenerator.getRandomFloatNumber(5000, 0.1f) + 100;
        boolean isBear = attributesGenerator.getBearIndicator();
        Currency registeredCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        Company companyCreated = new Company(tradingIdentifier, investmentBudget, name, ipoDate, ipoShareValue,
                openingPrice, profit, revenue, registeredCurrency, isBear, this.world);

        for (DynamicMarketIndex dynamicMarketIndex : dynamicIndices) {
            companyCreated.registerObserver(dynamicMarketIndex);
        }

        return companyCreated;
    }

    /**
     * @param currenciesByNow to fill investment budget with something
     * @return HumanInvestor
     */
    public HumanInvestor createHumanInvestor(ArrayList<Currency> currenciesByNow) {
        HashMap<String, String> human = this.attributesGenerator.getRandomHumanInvestorData();
        String tradingIdentifier = human.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
        String name = human.get("name");
        String lastName = human.get("lastName");
        boolean isBear = attributesGenerator.getBearIndicator();

        return new HumanInvestor(tradingIdentifier, investmentBudget, name, lastName, isBear, this.world);
    }

    /**
     * @param currenciesByNow to fill investment budget with something
     * @param controller      investment fund by issuing Units may change the
     *                        controller. That is why it is needed
     * @return InvestmentFund
     */
    public InvestmentFund createInvestmentFund(ArrayList<Currency> currenciesByNow, MainPanelController controller) {
        HashMap<String, String> investmentFund = this.attributesGenerator.getRandomInvestmentFundData();
        String tradingIdentifier = investmentFund.get("tradingIdentifier");
        HashMap<String, Float> investmentBudget = new HashMap<String, Float>();
        String name = investmentFund.get("name");
        String menagerFirstName = investmentFund.get("menagerFirstName");
        String menagerLastName = investmentFund.get("menagerLastName");
        boolean isBear = attributesGenerator.getBearIndicator();
        Currency registeredCurrency = this.attributesGenerator.getRandomCurrency(currenciesByNow);

        float fundPercentageProfit = SemiRandomValuesGenerator.getRandomFloatNumber(0.1f);

        return new InvestmentFund(tradingIdentifier, investmentBudget, name, menagerFirstName, menagerLastName,
                registeredCurrency, isBear, this.world, controller, fundPercentageProfit);
    }

    /**
     * @param currenciesByNow to fill investment budget with something
     * @param trader          who will get budget filled
     */
    public void fillInitialBudgetRandomly(ArrayList<Currency> currenciesByNow, Trader trader) {
        for (Currency currency : currenciesByNow) {
            float amount = SemiRandomValuesGenerator.getRandomFloatNumber(1000, 0.1f);
            trader.addBudget(currency, amount);
        }
    }
}
