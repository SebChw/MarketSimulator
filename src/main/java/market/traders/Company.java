package market.traders;

import market.assets.Currency;
import java.util.HashMap;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import market.assets.Share;
import market.entityCreator.SemiRandomValuesGenerator;
import market.interfaces.CompanyObserver;
import market.interfaces.CompanySubject;

import java.time.LocalDate;

import market.world.World;

/**
 * Class representing real company that issue shares and generates some profit
 * etc.
 */
public class Company extends Trader implements CompanySubject {
    private LocalDate ipoDate;
    private ArrayList<Float> valueOverTime = new ArrayList<Float>();
    private Float openingPrice;
    private Float profit;
    private Float revenue;
    private Integer tradingVolume;
    private Float totalSales;
    private Currency registeredCurrency;
    private Share share;

    private String[] moreDetails = { "IPO date: ", "IPO share value: ", "Opening price: ", "Current price: ",
            "Maximum price: ", "Profit: ",
            "Revenue: ", "Trading Volume: ", "Total sales: ", "Registered currency: " };

    private ArrayList<CompanyObserver> companyObservers = new ArrayList<CompanyObserver>();
    // !private Share share; // In General I think that share is something that
    // belongs to the company. Have to think about it.
    // Probably it is better to have it only in one place so that consistency is
    // higher. Now issuing shares is no problem.

    public Company(String tradingIdentifier, HashMap<String, Float> investmentBudget, String name,
            String ipoDate, float ipoShareValue, float openingPrice, float profit, float revenue,
            Currency registeredCurrency, boolean isBear, World world) {
        super(tradingIdentifier, investmentBudget, name, "Company", isBear, world);
        this.ipoDate = LocalDate.parse(ipoDate);
        this.openingPrice = openingPrice;
        this.valueOverTime.add(openingPrice);
        this.profit = profit;
        this.revenue = revenue;
        this.tradingVolume = 0;
        this.totalSales = (float) 0.0;
        this.registeredCurrency = registeredCurrency;
        this.share = new Share(name, SemiRandomValuesGenerator.getRandomIntNumber(50), this, 1 / ipoShareValue);

    }

    /**
     * @return Share
     */
    public Share getShare() {
        return this.share;
    }

    /**
     * @return float
     */
    public float getShareValue() {
        return this.share.getCurrentRate();
    }

    /**
     * @return float
     */
    public float getCurrentPrice() {
        return 1 / this.share.getCurrentRate();
    }

    public float getMaximumPrice() {
        return this.share.getMaximumRate();
    }

    /**
     * @return Currency
     */
    public Currency getRegisteredCurrency() {
        return this.registeredCurrency;
    }

    /**
     * Company in any moment can increase available shares amount
     * 
     * @param amount
     */
    public void increaseShares(int amount) {
        this.share.increaseShares(amount);
    }

    /**
     * In any moment company can remove not bought shares from the market
     * 
     * @param amount
     */
    public void decreaseShares(int amount) {
        this.share.increaseShares(-amount);
    }

    /**
     * @return LocalDate
     */
    public LocalDate getIpoDate() {
        return this.ipoDate;
    }

    /**
     * Just increases revenue by random amount
     */
    public void generateRevenue() {
        revenue += SemiRandomValuesGenerator.getRandomFloatNumber(1000, 100);
    }

    /**
     * Just increases profit by random amount
     */
    public void generateProfit() {
        profit += SemiRandomValuesGenerator.getRandomFloatNumber(1000, 100);
    }

    /**
     * Just issue random amount of shares
     */
    public void issueShares() {
        share.increaseShares(SemiRandomValuesGenerator.getRandomIntNumber(20));
    }

    /**
     * In that way company informs all interested parts that company values has
     * changed
     */
    @Override
    public void notifyObservers() {
        for (CompanyObserver companyObserver : companyObservers) {
            companyObserver.update(this);
        }
    }

    /**
     * @param observer
     */
    @Override
    public void registerObserver(CompanyObserver observer) {
        this.companyObservers.add(observer);

    }

    /**
     * @param observer
     */
    @Override
    public void removeObserver(CompanyObserver observer) {
        this.companyObservers.remove(observer);

    }

    /**
     * @param traderDetails
     */
    @Override
    public void fillGridPane(GridPane traderDetails) {
        super.fillGridPane(traderDetails);
        Float ipoValue = 1 / share.getCurrentRate();
        String[] filledDetails = { ipoDate.toString(), ipoValue.toString(), openingPrice.toString(),
                String.valueOf(getCurrentPrice()), String.valueOf(getMaximumPrice()),
                profit.toString(), revenue.toString(), tradingVolume.toString(), totalSales.toString(),
                registeredCurrency.getName() };
        super.addLabelsToPane(traderDetails, moreDetails, filledDetails, 4);

    }

    /**
     * Additionally to typical trader it also generates revenue, profit and issue
     * Shares
     */
    @Override
    public void operation() {
        super.operation();
        generateRevenue();
        generateProfit();
        issueShares();
    }

}
