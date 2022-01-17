package market.assets;

import market.traders.Company;
import market.world.Constants;
import market.entityCreator.SemiRandomValuesGenerator;

/** Class representing share in our world */
public class Share extends AssetBackedByCurrency {
    // ! Generally commodities and Currencies rather doesn't have limits. However
    // Company issue only some shares and only issued can be
    // ! Bough no more
    private Company company;
    private volatile float availableShares;
    private float issuedShares;

    public Share(String name, float availableShares, Company company, Float startingRate) {
        super(name, Constants.shareType, 0, company.getRegisteredCurrency(), startingRate);
        this.company = company;
        this.issuedShares = availableShares;
        this.availableShares = availableShares;

    }

    public float getIssuedShares() {
        return issuedShares;
    }

    /**
     * @param amount
     */
    public synchronized void increaseShares(float amount) {
        if (-amount > getAvailableShares()) {
            System.out.println("You can only buy how many shares are available!!");
            return;
        }
        this.availableShares += amount;
        this.issuedShares += amount;
    }

    /**
     * @return float
     */
    @Override
    public float getPossibleAmount() {
        // ! Here if there is no more Shares to buy we must be carefull as we will have
        // 0 available shares
        float available = this.availableShares;
        if (available < 1) {
            // System.out.println(this.company.getName() + " " + this.availableShares);
            return 0;
        }
        return SemiRandomValuesGenerator.getRandomIntNumber((int) available) + 1; // need + 1 as we need
                                                                                  // rande [1, available]
    }

    /**
     * @param amount
     * @return float
     */
    public float getPossibleAmount(float amount) {
        if (amount < 1) {
            System.out.println("User ERROR! with company: " + this.company.getName() + " " + amount);
            return 0;
        }
        return SemiRandomValuesGenerator.getRandomIntNumber((int) (amount));
    }

    /**
     * @return float
     */
    public float getAvailableShares() {
        return this.availableShares;
    }

    /**
     * Function checking whether we can buy new shares, If yes it automatically
     * freezes them
     * 
     * @param amount
     * @return boolean
     */
    @Override
    public boolean canBeBought(float amount) {
        if ((int) amount != amount) {
            System.out.println(amount + " " + (int) amount);
        }
        synchronized (this) {
            if (availableShares < amount) {
                // System.out.println(
                // "You Can't buy this share as this would lead to amount In Circulation >
                // IssuedShares!");
                return false;
            }
            availableShares -= amount; // ! Im Freezing this shares!
        }

        return true;
    }

    /**
     * @param amount
     */
    @Override
    public void unfreeze(float amount) {
        synchronized (this) {
            availableShares += amount; // ! Im Unfreezing this shares!
            if (this.availableShares < 0) {
                System.out.println("After freezing shares are negative!");
            }
        }

    }

    /**
     * @param amount
     */
    public synchronized void changeAmountInCirculation(float amount) {
        // BE VERY CAREFULL HERE!
        super.changeAmountInCirculation(amount);
        if (amount < 0) {
            // I NEED TO SUBTRACT AS WHEN IM SELLING THEN amount is negative!
            // ! This was the biggest bug I've made and it was hard to find itp
            this.availableShares -= amount; // ! DONT NEED TO DO THIS IM DOING IT DURING
            // UNFREEZING ILL SHOULD DO THIS
            // ONLY WHEN IM SELLING. and sellin is represented by negative amount
        }

        // ! Throw exception here!
        if (this.availableShares < 0) {
            System.out.println("After chaning amount shares are negative!");
        }
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return super.toString() + "\nIssued by: " + company.getName() + "\nAvailable Shares: " + availableShares
                + "\nIssued shares: " + issuedShares;
    }

    /**
     * @param amount
     * @return float
     */
    public float calculateMainToThis(float amount) {
        return (int) super.calculateMainToThis(amount);
    }

    public float getMaximumRate() {
        return super.getMainBankRates().getMaxPrice();
    }
}
