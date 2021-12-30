package market.assets.marketIndex;

import market.assets.Asset;
import market.traders.Company;
import java.util.ArrayList;


public class StockMarketIndex extends Asset{
    private String name;
    private ArrayList<Company> companies;

    public StockMarketIndex(String name, String type, float amountInCirculation) {
        super(name, type, amountInCirculation);
        //TODO Auto-generated constructor stub
    }

    public void addToIndex(Company company){

    }
    public void removeFromIndex(Company company){

    }
    public float getIndexValue() {

        return (float) 0.1;
    }
    @Override
    public float calculateThisToMain(float amount) {
        // TODO We calculate value of the Index by summing all Companies shares values
        return 0;
    }
    @Override
    public float calculateMainToThis(float amount) {
        // TODO We calculate this by dividing amount by This To Main.
        return 0;
    }
}
