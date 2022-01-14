package market.gui;

import java.util.function.Function;

import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;

/**
 * Extra class for menaging Tooltips and making them refreshable
 */
public class TooltipTableRow<T> extends TableRow<T> {

  private Function<T, String> toolTipStringFunction;

  /**
   * 
   * @param toolTipStringFunction This function will be aplied to elements during
   *                              updating text of the Tooltip
   */
  public TooltipTableRow(Function<T, String> toolTipStringFunction) {
    this.toolTipStringFunction = toolTipStringFunction;
  }

  /**
   * Function adding new tooltip to element;
   * 
   * @param item  object about which we want to show the information
   * @param empty
   */
  @Override
  protected void updateItem(T item, boolean empty) {
    super.updateItem(item, empty);
    if (item == null) {
      setTooltip(null);
    } else {
      Tooltip tooltip = new Tooltip(toolTipStringFunction.apply(item));
      setTooltip(tooltip);
    }
  }

  /**
   * Function for updating tooltip once it's created
   * 
   * @param item object about which we want to show the information
   */
  protected void setTooltipText(T item) {
    if (item == null)
      return;
    Tooltip tooltip = new Tooltip(toolTipStringFunction.apply(item));
    setTooltip(tooltip);
  }
}
