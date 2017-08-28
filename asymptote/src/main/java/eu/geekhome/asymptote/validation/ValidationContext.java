package eu.geekhome.asymptote.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InvalidClassException;
import java.util.ArrayList;

public class ValidationContext {
    private static final Logger _logger = LoggerFactory.getLogger(ValidationContext.class);
    private ArrayList<Validator> _validators = new ArrayList<>();

    public void addValidator(Validator validator) {
        _validators.add(validator);
    }

    public ArrayList<Validator> getValidators() {
        return _validators;
    }

    public boolean validate() {
        boolean hasValidationErrors = false;
        for (Validator validator : _validators) {
            try {
                boolean validationResult = validator.validate();
                if (!validationResult) {
                    hasValidationErrors = true;
                }
            }
            catch (InvalidClassException ex) {
                _logger.error("Validation error", ex);
                return false;
            }
        }

        return !hasValidationErrors;
    }
}
