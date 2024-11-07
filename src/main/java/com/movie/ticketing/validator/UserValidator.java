package com.movie.ticketing.validator;

import java.util.logging.Logger;

public abstract class UserValidator {
    protected static final Logger logger = Logger.getLogger(UserValidator.class.getName());
    public abstract boolean validateUser();
}
