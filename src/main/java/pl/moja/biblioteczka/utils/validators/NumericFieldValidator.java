package pl.moja.biblioteczka.utils.validators;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumericFieldValidator {


    public static boolean validate(TextField textField) {
        Pattern pattern = Pattern.compile("^[0-9]{1,3}(,[0-9]{0,5})?");
        if (textField != null) {
            if (null == textField.getText() || textField.getText().isEmpty()) {
                return false;
            } else {
                Matcher matcher = pattern.matcher(textField.getText());
                return matcher.find();
            }
        }
        return false;
    }


    public static boolean validate(String text){
        Pattern pattern = Pattern.compile("^[0-9]{1,3}(,[0-9]{0,5})?");
        if(text != null && !text.isEmpty()){
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
        return true;
    }

    public static TextFormatter getNumericFieldFormatter() {
        return new TextFormatter<>(change ->
                (change.getControlNewText().matches("[0-9]{1,3}(,[0-9]{0,5})?")) ? change : null);
    }
}
