package market.gui;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;

public class TooltipTableRow<T> extends TableRow<T> {

  private Function<T, String> toolTipStringFunction;

  public TooltipTableRow(Function<T, String> toolTipStringFunction) {
    //System.out.println("RUNNING CONSTRUCTOR!");
    this.toolTipStringFunction = toolTipStringFunction;
  }

  @Override
  protected void updateItem(T item, boolean empty) {
    super.updateItem(item, empty); // Nothing more is runned there :(
    if(item == null) { 
      setTooltip(null);
    } else {
      Tooltip tooltip = new Tooltip(toolTipStringFunction.apply(item));
      setTooltip(tooltip);
    }
  }

  protected void setTooltipText(T item){
      if(item == null) return;
      Tooltip tooltip = new Tooltip(toolTipStringFunction.apply(item));
      setTooltip(tooltip);
  }
}
