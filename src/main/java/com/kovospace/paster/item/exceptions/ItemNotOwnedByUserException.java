package com.kovospace.paster.item.exceptions;

public class ItemNotOwnedByUserException extends ItemException {
    public ItemNotOwnedByUserException() { super("item.response.access.denied"); }
}
