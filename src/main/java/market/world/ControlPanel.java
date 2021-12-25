package market.world;

import market.observers.AssetObserver;

public class ControlPanel implements AssetObserver{
    private ControlPanelSettings settings;

    @Override
    public void update(String assetName, int hypeLevel, int amountOfOwners, float AmountInCirculation) {
        // TODO Auto-generated method stub
        
    }

    public static void run(){
        System.out.println("IT's RUNNING!");
    }

}
