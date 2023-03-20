package hu.logcontrol.wasteprogram.interfaces;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.enums.ViewButtons;

public interface IRawMaterialCreationView {
//    void settingTextBoxes();
//    void settingConstraintLayouts();
    void settingLayoutsButtons(ViewButtons viewButton, boolean buttonState, int buttonBackground);
}
