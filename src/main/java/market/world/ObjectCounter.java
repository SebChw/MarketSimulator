package market.world;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ObjectCounter {
    private Integer numberOfCompanies = 0; // number of companies == number of shares!
    private Integer numberOfHumanInvestors = 0;
    private Integer numberOfInvestmentFunds = 0;
    private Integer numberOfMarkets = 0;
    private Integer numberOfCurrencies = 0;
    private Integer numberOfCommodities = 0;
    private Integer numberOfIndices = 0;
    private Integer numberOfInvestmentFundUnits = 0;
    private Integer numberOfDynamicIndices = 0;

    private ArrayList<Label> labels = new ArrayList<Label>();

    private String [] details = {"Companies: ", "Human Investors: ", "Investment Funds: ", "Markets: ", "Currencies: ",
                        "Commodities: ","Investments Fund Units: ", "Static Indices: ", "Dynamic Indices: "};

    public void findAndUpdate(String what){

    }

    public int getNumberOfCompanies() {
        return this.numberOfCompanies;
        
    }

    public int getNumberOfHumanInvestors() {
        return this.numberOfHumanInvestors;
    }

    public int getNumberOfInvestmentFunds() {
        return this.numberOfInvestmentFunds;
    }

    public int getNumberOfMarkets() {
        return this.numberOfMarkets;
    }

    public int getNumberOfCurrencies() {
        return this.numberOfCurrencies;
    }

    public int getNumberOfCommodities() {
        return this.numberOfCommodities;
    }

    public int getNumberOfIndices(){
        return this.numberOfIndices;
    }

    public int getNumberOfDynamicIndices(){
        return this.numberOfDynamicIndices;
    }
    
    public void changeNumberOfCompanies(int numberOfCompanies) {
        this.numberOfCompanies += numberOfCompanies;
        updateGridPane();
    }
    public void changeNumberOfHumanInvestors(int numberOfHumanInvestors) {
        this.numberOfHumanInvestors += numberOfHumanInvestors;
        updateGridPane();
    }
    public void changeNumberOfInvestmentFunds(int numberOfInvestmentFunds) {
        this.numberOfInvestmentFunds += numberOfInvestmentFunds;
        updateGridPane();
    }
    public void changeNumberOfMarkets(int numberOfMarkets) {
        this.numberOfMarkets += numberOfMarkets;
        updateGridPane();
    }
    public void changeNumberOfCurrencies(int numberOfCurrencies) {
        this.numberOfCurrencies += numberOfCurrencies;
        updateGridPane();
    }
    public void changeNumberOfCommodities(int numberOfCommodities) {
        this.numberOfCommodities += numberOfCommodities;
        updateGridPane();
    }
    public void changeNumberOfIndices(int numberOfIndices) {
        this.numberOfIndices += numberOfIndices;
        updateGridPane();
    }

    public int getNumberOfInvestmentFundUnits() {
        return this.numberOfInvestmentFundUnits;
    }

    public void changeNumberOfInvestmentFundUnits(int numberOfInvestmentFundUnits) {
        this.numberOfInvestmentFundUnits += numberOfInvestmentFundUnits;
        updateGridPane();
    }
    public void changeNumberOfDynamicIndices(int numberOfDynamicIndices) {
        this.numberOfDynamicIndices += numberOfDynamicIndices;
        updateGridPane();
    }

    public int getAmountOfAssets(){
        return numberOfCurrencies + numberOfCommodities + numberOfCompanies;
    }
    
    public int getAmountOfInvestors(){
        return numberOfHumanInvestors + numberOfInvestmentFunds;
    }

    public void fillGridPane(GridPane worldDetails){
        String [] filledDetails = {numberOfCompanies.toString(), numberOfHumanInvestors.toString(), numberOfInvestmentFunds.toString(),
                                numberOfMarkets.toString(), numberOfCurrencies.toString(), numberOfCommodities.toString(),
                                numberOfInvestmentFundUnits.toString(), numberOfIndices.toString(), numberOfDynamicIndices.toString()};
        for (int i = 0; i < details.length; i++) {
            Label l = new Label();
            l.setText(details[i] + filledDetails[i]);
            labels.add(l);
            worldDetails.add(l, 0, i);
        }
    }

    public void updateGridPane(){
        String [] filledDetails = {numberOfCompanies.toString(), numberOfHumanInvestors.toString(), numberOfInvestmentFunds.toString(),
            numberOfMarkets.toString(), numberOfCurrencies.toString(), numberOfCommodities.toString(),
            numberOfInvestmentFundUnits.toString(), numberOfIndices.toString(), numberOfDynamicIndices.toString()};

        for (int i = 0; i < details.length; i++) {
            labels.get(i).setText(details[i] + filledDetails[i]);
        }
    }
}
