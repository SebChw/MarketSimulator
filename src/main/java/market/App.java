package market;

//! Tink about making the market about heroes 3!
import market.world.ControlPanel;
import market.world.World;
import market.entityCreator.*;
public class App {
    public static void main(String[] args) throws Exception {
        ControlPanel.run();

        World world = new World();

        world.addNewCurrency();
        world.addNewCurrency();
        world.addNewCurrency();
        world.addNewCurrency();
        world.addNewCommodity();
        world.addNewCommodity();
        world.addNewCommodity();
        world.addNewCompany();
        world.addNewHumanInvestor();
        world.addNewInvestmendFund();

        System.out.println("JEJ");

    }
}
