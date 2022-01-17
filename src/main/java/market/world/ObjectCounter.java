package market.world;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Class that takes care of counting amounts of each type of object in the world
 * 
 */
public class ObjectCounter implements Serializable {
    private Integer numberOfCompanies = 0; // number of companies == number of shares!
    private Integer numberOfHumanInvestors = 0;
    private Integer numberOfInvestmentFunds = 0;
    private Integer numberOfMarkets = 0;
    private Integer numberOfCurrencies = 0;
    private Integer numberOfCommodities = 0;
    private Integer numberOfIndices = 0;
    private Integer numberOfInvestmentFundUnits = 0;
    private Integer numberOfDynamicIndices = 0;

    private transient ArrayList<Label> labels;

    private String[] details = { "Companies: ", "Human Investors: ", "Investment Funds: ", "Markets: ", "Currencies: ",
            "Commodities: ", "Investments Fund Units: ", "Static Indices: ", "Dynamic Indices: " };

    /**
     * @return int
     */
    public int getNumberOfCompanies() {
        return this.numberOfCompanies;

    }

    /**
     * @return int
     */
    public int getNumberOfHumanInvestors() {
        return this.numberOfHumanInvestors;
    }

    /**
     * @return int
     */
    public int getNumberOfInvestmentFunds() {
        return this.numberOfInvestmentFunds;
    }

    /**
     * @return int
     */
    public int getNumberOfMarkets() {
        return this.numberOfMarkets;
    }

    /**
     * @return int
     */
    public int getNumberOfCurrencies() {
        return this.numberOfCurrencies;
    }

    /**
     * @return int
     */
    public int getNumberOfCommodities() {
        return this.numberOfCommodities;
    }

    /**
     * @return int
     */
    public int getNumberOfIndices() {
        return this.numberOfIndices;
    }

    /**
     * @return int
     */
    public int getNumberOfDynamicIndices() {
        return this.numberOfDynamicIndices;
    }

    /**
     * @param numberOfCompanies
     */
    public void changeNumberOfCompanies(int numberOfCompanies) {
        this.numberOfCompanies += numberOfCompanies;
        updateGridPane();
    }

    /**
     * @param numberOfHumanInvestors
     */
    public void changeNumberOfHumanInvestors(int numberOfHumanInvestors) {
        this.numberOfHumanInvestors += numberOfHumanInvestors;
        updateGridPane();
    }

    /**
     * @param numberOfInvestmentFunds
     */
    public void changeNumberOfInvestmentFunds(int numberOfInvestmentFunds) {
        this.numberOfInvestmentFunds += numberOfInvestmentFunds;
        updateGridPane();
    }

    /**
     * @param numberOfMarkets
     */
    public void changeNumberOfMarkets(int numberOfMarkets) {
        this.numberOfMarkets += numberOfMarkets;
        updateGridPane();
    }

    /**
     * @param numberOfCurrencies
     */
    public void changeNumberOfCurrencies(int numberOfCurrencies) {
        this.numberOfCurrencies += numberOfCurrencies;
        updateGridPane();
    }

    /**
     * @param numberOfCommodities
     */
    public void changeNumberOfCommodities(int numberOfCommodities) {
        this.numberOfCommodities += numberOfCommodities;
        updateGridPane();
    }

    /**
     * @param numberOfIndices
     */
    public void changeNumberOfIndices(int numberOfIndices) {
        this.numberOfIndices += numberOfIndices;
        updateGridPane();
    }

    /**
     * @return int
     */
    public int getNumberOfInvestmentFundUnits() {
        return this.numberOfInvestmentFundUnits;
    }

    /**
     * @param numberOfInvestmentFundUnits
     */
    public void changeNumberOfInvestmentFundUnits(int numberOfInvestmentFundUnits) {
        this.numberOfInvestmentFundUnits += numberOfInvestmentFundUnits;
        updateGridPane();
    }

    /**
     * @param numberOfDynamicIndices
     */
    public void changeNumberOfDynamicIndices(int numberOfDynamicIndices) {
        this.numberOfDynamicIndices += numberOfDynamicIndices;
        updateGridPane();
    }

    /**
     * @return int
     */
    public int getAmountOfAssets() {
        return numberOfCurrencies + numberOfCommodities + numberOfCompanies;
    }

    /**
     * @return int
     */
    public int getAmountOfInvestors() {
        return numberOfHumanInvestors + numberOfInvestmentFunds;
    }

    /**
     * Function that fills given Grid Pane with Labels containing information about
     * amount of all objects
     * 
     * @param worldDetails
     */
    public void fillGridPane(GridPane worldDetails) {
        worldDetails.getChildren().clear();
        labels = new ArrayList<Label>();
        String[] filledDetails = { numberOfCompanies.toString(), numberOfHumanInvestors.toString(),
                numberOfInvestmentFunds.toString(),
                numberOfMarkets.toString(), numberOfCurrencies.toString(), numberOfCommodities.toString(),
                numberOfInvestmentFundUnits.toString(), numberOfIndices.toString(), numberOfDynamicIndices.toString() };
        for (int i = 0; i < details.length; i++) {
            Label l = new Label();
            l.setText(details[i] + filledDetails[i]);
            labels.add(l);
            worldDetails.add(l, 0, i);
        }
    }

    /**
     * Function which just updates all labels it has created
     */
    public void updateGridPane() {
        String[] filledDetails = { numberOfCompanies.toString(), numberOfHumanInvestors.toString(),
                numberOfInvestmentFunds.toString(),
                numberOfMarkets.toString(), numberOfCurrencies.toString(), numberOfCommodities.toString(),
                numberOfInvestmentFundUnits.toString(), numberOfIndices.toString(), numberOfDynamicIndices.toString() };

        for (int i = 0; i < details.length; i++) {
            labels.get(i).setText(details[i] + filledDetails[i]);
        }
    }
}
