package market.world;

import market.traders.*;
import market.assets.*;
import market.markets.*;
import java.util.ArrayList;
import market.entityCreator.EntityFactory;
import market.exchangeRates.ExchangeRatesProvider;

public class World {
    private ArrayList<Company> companies = new ArrayList<Company>();
    private ArrayList<HumanInvestor> humanInvestors = new ArrayList<HumanInvestor>();
    private ArrayList<InvestmentFund> investmentFunds = new ArrayList<InvestmentFund>();
    //private ArrayList<Market> markets;
    //private ArrayList<Asset> assets;
    private ArrayList<Currency> currencies = new ArrayList<Currency>();
    private ArrayList<Commodity> commodities = new ArrayList<Commodity>();
    private ArrayList<ExchangeRatesProvider> exchangeRates = new ArrayList<ExchangeRatesProvider>();;
    private EntityFactory entityFactory = new EntityFactory();

    public World(){
    }

    public void addNewCurrency(){
        this.currencies.add(this.entityFactory.createCurrency(this.exchangeRates));
    }

    public void addNewCommodity(){
        this.commodities.add(this.entityFactory.createCommodity(this.currencies));
    }

    public void addNewCompany(){
        this.companies.add(this.entityFactory.createCompany(this.currencies));
    }

    public void addNewHumanInvestor(){
        this.humanInvestors.add(this.entityFactory.createHumanInvestor(this.currencies));
    }

    public void addNewInvestmendFund(){
        this.investmentFunds.add(this.entityFactory.createInvestmentFund(this.currencies));
    }

    
}
