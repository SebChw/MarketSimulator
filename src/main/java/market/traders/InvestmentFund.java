package market.traders;

import java.util.HashMap;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import market.gui.MainPanelController;
import market.interfaces.Dealer;
import market.transactions.StrictTransactionSystem;
import market.transactions.TransactionSystem;
import market.world.Constants;
import market.world.World;
import market.assets.Asset;
import market.assets.Currency;
import market.assets.InvestmentFundUnit;

/**
 * Class representing Investment Fund -> which can issue some units that can be
 * bought by traders so we can also trade with this class members
 * If we are human Investors
 */
public class InvestmentFund extends Trader implements Dealer {
    private String menagerFirstName;
    private String menagerLastName;
    private Currency registeredCurrency;
    private int issuedFundsAmount = 0;
    private float fundPercentageProfit = 0;
    private HashMap<String, Asset> issuedFundsUnit = new HashMap<String, Asset>();
    TransactionSystem transactionSystem = new StrictTransactionSystem();

    private String[] moreDetails = { "Menager first name: ", "Menager last name: ", "Registered currency: " };
    private transient MainPanelController controller;

    /**
     * 
     * 
     * @param menagerFirstName
     * @param menagerLastName
     * @param registeredCurrency needed to calculate prices for units and
     *                           transactions
     * @param controller         issuing funds leads to a change in controller
     */
    public InvestmentFund(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name,
            String menagerFirstName, String menagerLastName, Currency registeredCurrency, boolean isBear, World world,
            MainPanelController controller, float fundPercentageProfit) {
        super(tradingIdentifier, investmentBudget, name, Constants.investmentFundType, isBear, world);

        this.menagerFirstName = menagerFirstName;
        this.menagerLastName = menagerLastName;
        this.registeredCurrency = registeredCurrency;
        this.controller = controller;
        this.fundPercentageProfit = fundPercentageProfit;

        issueFundUnit();
    }

    /**
     * Function running buyOperation on the transactionSystem
     * 
     * @param trader     trader wanted to trade here
     * @param wantedUnit wanted unit
     * @param amount     how many units they want
     */
    public void buy(Trader trader, Asset wantedUnit, float amount) {
        transactionSystem.buyOperation(this, trader, wantedUnit, amount, super.getWorld());
    }

    /**
     * Function running sellOperation on the transactionSystem
     * 
     * @param trader
     * @param soldUnit
     * @param amount
     */
    public void sell(Trader trader, Asset soldUnit, float amount) {
        transactionSystem.sellOperation(this, trader, soldUnit, amount, super.getWorld());
    }

    /**
     * @return Currency
     */
    public Currency getRegisteredCurrency() {
        return this.registeredCurrency;
    }

    /**
     * Function that will generate new Fund Unit and add it to the world
     */
    public void issueFundUnit() {
        issuedFundsAmount += 1;
        System.out.println("Fund: " + this.getName() + "Issue new unit");
        InvestmentFundUnit unit = super.getWorld().getObjectsAdder().addNewInvestmentFundUnit(this);
        issuedFundsUnit.put(unit.getName(), unit);
    }

    /**
     * @return int
     */
    public int getIssuedFundsAmount() {
        return issuedFundsAmount;
    }

    /**
     * Function filling gridPane with additional details specific to InvestmentFund
     * 
     * @param traderDetails
     */
    public void fillGridPane(GridPane traderDetails) {
        super.fillGridPane(traderDetails);
        String[] filledDetails = { menagerFirstName, menagerLastName, registeredCurrency.getName() };
        super.addLabelsToPane(traderDetails, moreDetails, filledDetails, 4);
    }

    /**
     * Since this class by issuing Funds Unit may change counter this is needed
     * 
     * @return MainPanelController
     */
    public MainPanelController getController() {
        return controller;
    }

    public void setController(MainPanelController controller) {
        this.controller = controller;
    }

    @Override
    public void operation() {
        super.operation();
    }

    /**
     * @return ArrayList<InvestmentFundUnit>
     */
    public ArrayList<Asset> getAvailableAssets() {
        return new ArrayList<Asset>(issuedFundsUnit.values());
    }

    @Override
    public String getSearchString() {
        return super.getSearchString() + menagerFirstName;
    }

    @Override
    public HashMap<String, Asset> getAvailableAssetsHashMap() {
        return issuedFundsUnit;
    }

    @Override
    public float getPercentageOperationCost() {
        return fundPercentageProfit;
    }

    @Override
    public Currency getTradingCurrency() {
        return registeredCurrency;
    }
}
