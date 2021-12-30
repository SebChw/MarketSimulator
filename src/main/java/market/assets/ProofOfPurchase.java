package market.assets;

import market.traders.Trader;
import java.time.LocalDate;
public class ProofOfPurchase {
    private Asset boughtAsset;
    private LocalDate date;
    private float amount;
    //private Trader whoBought;

    public ProofOfPurchase(Asset boughtAsset, LocalDate date, float amount){
        this.boughtAsset = boughtAsset;
        this.date = date;
        this.amount = amount;
    }

    public Asset getBoughtAsset() {
        return this.boughtAsset;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public float getAmount() {
        return this.amount;
    }

}
