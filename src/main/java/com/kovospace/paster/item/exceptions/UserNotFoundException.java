package com.kovospace.paster.item.exceptions;

public class UserNotFoundException extends ItemException {
    public UserNotFoundException() { super("User not found or You have no right to access these items."); }
}
