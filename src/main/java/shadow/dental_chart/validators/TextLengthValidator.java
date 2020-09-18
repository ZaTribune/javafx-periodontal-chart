/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package shadow.dental_chart.validators;

import com.jfoenix.validation.base.ValidatorBase;
import static com.jfoenix.validation.base.ValidatorBase.PSEUDO_CLASS_ERROR;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

/**
 *
 * @author Muhammad
 */
public class TextLengthValidator extends ValidatorBase {

    private final int from;
    private final int to;
    private static String MESSAGE = "input is from %d to %d characters";
    ;
    private Tooltip tooltip = null;
    private final Tooltip errorTooltip = new Tooltip();
    private final List<Node> errorLabelList = new ArrayList<>();

    public TextLengthValidator(TextInputControl src, int from, int to) {
        super.setSrcControl(src);
        this.from = from;
        this.to = to;
        src.textProperty().addListener((ov, oldString, newString) -> {
            validate();
            if (!src.getText().matches("[1-5](\\.[0-9]{1,2}){0,1}|9(\\.0{1,2}){0,1}")) {
                src.setText(oldString);
            }
        });

    }

    @Override
    protected void eval() {//when we call validate() -->eval() is called first before onEval() however evalTextInputField() is called after oneval()
        if (srcControl.get() instanceof TextInputControl) {
            errorLabelList.addAll(srcControl.getValue().lookupAll(".error-label"));//brougt from ValidationPane.class source
            evalTextInputField();
        }
    }

    public void evalTextInputField() {
        TextInputControl textField = (TextInputControl) srcControl.get();
        if (textField.getText().length() < from || textField.getText().length() > to) {
            ((Label) errorLabelList.get(0)).setText(String.format(MESSAGE, from, to));
            errorTooltip.setText(String.format(MESSAGE, from, to));
            hasErrors.set(true);
        } else
            hasErrors.set(false);

    }

    @Override
    protected void onEval() {
        Node control = getSrcControl();
        if (hasErrors.get()) {
            control.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, true);
            if (control instanceof Control) {
                Tooltip controlTooltip = ((Control) control).getTooltip();
                if (controlTooltip != null && !controlTooltip.getStyleClass().contains("error-tooltip"))
                    tooltip = ((Control) control).getTooltip();
                //we overrided this method to remove the behaviour of calling getMessage() here 
                //errorTooltip.setText(getMessage());
                ((Control) control).setTooltip(errorTooltip);
            }
        } else {
            if (control instanceof Control) {
                Tooltip controlTooltip = ((Control) control).getTooltip();
                if ((controlTooltip != null && controlTooltip.getStyleClass().contains("error-tooltip"))
                        || (controlTooltip == null && tooltip != null))
                    ((Control) control).setTooltip(tooltip);
                tooltip = null;
            }
            control.pseudoClassStateChanged(PSEUDO_CLASS_ERROR, false);
        }
    }

    //override these methods and remove any usages for them in the child-extended class
    @Override
    public void setMessage(String MESSAGE) {
        TextLengthValidator.MESSAGE = MESSAGE;
    }

    @Override
    public String getMessage() {
        return String.format(MESSAGE, from, to);
    }

}
