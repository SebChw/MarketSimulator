package market.assets.marketIndex;

import market.assets.Asset;
import market.entityCreator.SemiRandomValuesGenerator;
import market.traders.Company;
import java.util.ArrayList;

/**
 * Class representing Market Index in the real world
 */
public class MarketIndex extends Asset {
    private ArrayList<Company> companies;

    /**
     * 
     * @param name         name of the index
     * @param companies    companies that are in the index
     * @param backingAsset in which Asset it is backed
     * @param startingRate what rate it has at the beginning
     */
    public MarketIndex(String name, ArrayList<Company> companies, String backingAsset) {
        super(name, "Market Index", 0, backingAsset, 0);
        this.companies = companies;

        getMainBankRates().removeLastRate(); // This is done artificially to start with some rate calculated after
        updateRate();
    }

    protected MarketIndex(String name, String type, ArrayList<Company> companies, String backingAsset) {
        super(name, type, 0, backingAsset, 0);

        this.companies = companies;

        getMainBankRates().removeLastRate(); // This is done artificially to start with some rate calculated after
        updateRate();
    }

    /**
     * @return String
     */
    public String toString() {
        ArrayList<String> companyNames = new ArrayList<String>(this.companies.size());
        for (Company company : this.companies)
            companyNames.add(company.getName());
        return super.toString() + "\n companies in Index: " + companyNames;
    }

    /**
     * @param company
     */
    public void addToIndex(Company company) {

    }

    /**
     * @param company
     */
    public void removeFromIndex(Company company) {

    }

    /**
     * @return float
     */
    public float getIndexValue() {

        return (float) 0.1;
    }

    /**
     * @return ArrayList<Company>
     */
    public ArrayList<Company> getCompanies() {
        return this.companies;
    }

    /**
     * @param companies
     */
    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    /**
     * Just sums up all companies in the index values
     * 
     * @return float
     */
    public float calculateRatio() {
        float ratio = 0;
        for (Company company : companies) {
            ratio += 1 / company.getShareValue();
        }
        return 1 / ratio;
    }

    /**
     * Updates the rate of the asset
     */
    public void updateRate() {
        this.getMainBankRates().updateRate(calculateRatio());
    }

    /**
     * get how much of market index we can buy
     * 
     * @return float
     */
    public float getPossibleAmount() {
        return 1;
    }

    /**
     * @param amount
     * @return float
     */
    public float getPossibleAmount(float amount) {
        if (amount < 1) {
            System.out.println("User ERROR! with index: " + getName() + " " + amount);
            return 0;
        }
        return SemiRandomValuesGenerator.getRandomIntNumber((int) (amount));
    }

    /**
     * This function try to buy given amount of each share that is contained in that
     * asset. However in any time some different guy may
     * buy all available share that we did not check. So if this operation fails we
     * unfreeze all Shares that we tried to buy
     * 
     * @param amount
     * @return boolean
     */
    @Override
    public synchronized boolean canBeBought(float amount) {
        ArrayList<Company> checkedCompanies = new ArrayList<Company>(companies.size());
        for (Company company : companies) {
            if (!company.getShare().canBeBought(amount)) {
                for (Company unfreezedCompany : checkedCompanies) {
                    unfreezedCompany.getShare().unfreeze(amount);
                }
                return false;
            } else
                checkedCompanies.add(company);
        }
        System.out.println(
                "You can't buy this Index as this would lead to Bigger amountInCirculation of Share than available");
        return true;
    }

    /**
     * @param amount
     * @return float
     */
    public float calculateMainToThis(float amount) {
        return (int) super.calculateMainToThis(amount);
    }

}
