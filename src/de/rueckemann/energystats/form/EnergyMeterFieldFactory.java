package de.rueckemann.energystats.form;

import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
class EnergyMeterFieldFactory extends DefaultFieldFactory {
    static final String COMMON_FIELD_WIDTH = "12em";

    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        Field f;
        if("type".equals(propertyId)) {
        	f = new Select ("Type");
        } else {
            f = super.createField(item, propertyId, uiContext);
        }

        if ("label".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setNullRepresentation("");
            tf.setRequired(true);
            tf.setRequiredError("Please enter a label for this Energy Meter");
            tf.setWidth(COMMON_FIELD_WIDTH);
            tf.addValidator(new StringLengthValidator(
                    "Label must be 3-25 characters", 3, 25, false));
        } else if ("type".equals(propertyId)) {
        	Select select = (Select)f;
        	select.setNullSelectionAllowed(false);
        	select.addItem("Gas");
        	select.addItem("Electricity");
        	select.addItem("Water");
        	select.setRequired(true);
        	select.setRequiredError("Please specify the type");
        } else if ("manufacturer".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setNullRepresentation("");
            tf.setNullSettingAllowed(true);
            tf.setWidth(COMMON_FIELD_WIDTH);
            tf.addValidator(new StringLengthValidator(
                    "Manufacturer must not be longer than 40 characters", 0, 40, false));
        } else if ("location".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setNullRepresentation("");
            tf.setNullSettingAllowed(true);
            tf.setWidth(COMMON_FIELD_WIDTH);
            tf.addValidator(new StringLengthValidator(
                    "Location must not be longer than 40 characters", 0, 40, false));
        } 
        else if ("_id".equals(propertyId)) {
            TextField tf = (TextField) f;
            tf.setNullRepresentation("");
            tf.setReadOnly(true);
            tf.setWidth("20em");
        }

        return f;
    }
}