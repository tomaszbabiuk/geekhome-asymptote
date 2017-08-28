package eu.geekhome.asymptote.validation;

public class MinLengthParams {
    private String _validationMessage;
    private int _minLength;

    public MinLengthParams(String validationMessage, int minLength) {

        _validationMessage = validationMessage;
        _minLength = minLength;
    }

    public String getValidationMessage() {
        return _validationMessage;
    }

    public int getMinLength() {
        return _minLength;
    }

    public static MinLengthParams create(String validationMessage, int minLength) {
        return new MinLengthParams(validationMessage, minLength);
    }
}
