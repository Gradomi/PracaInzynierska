package pl.moja.biblioteczka.utils.validators;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFieldValidator {


    public static boolean validate(TextField textField) {
        Pattern pattern = Pattern.compile("^[a-zA-z0-9 ]{2,254}$");
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
        Pattern pattern = Pattern.compile("^[a-zA-z0-9 ]{2,254}$");
        if(text != null && !text.isEmpty()){
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }
        return true;
    }

    public static TextFormatter getTextFieldFormatter() {
        return new TextFormatter<>(change ->
                (change.getControlNewText().matches("[a-zA-z0-9 ]{2,254}")) ? change : null);
    }
}
