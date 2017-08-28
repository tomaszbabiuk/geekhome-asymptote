package eu.geekhome.asymptote.validation;

import java.io.InvalidClassException;

public interface Validator {
    boolean validate() throws InvalidClassException;
}