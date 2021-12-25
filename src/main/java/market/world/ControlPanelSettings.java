package market.world;

public class ControlPanelSettings {
    private ControlPanelSettings settings;

    private int numberOfTransactionsPerMinute;
    private float bearToBullRatio;

    private ControlPanelSettings(){

    }
    public ControlPanelSettings getSettings(){
        if (this.settings == null){
            this.settings = new ControlPanelSettings();
        }

        return this.settings;
    }


    
}
