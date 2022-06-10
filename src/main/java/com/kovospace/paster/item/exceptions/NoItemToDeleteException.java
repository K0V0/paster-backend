package com.kovospace.paster.item.exceptions;

public class NoItemToDeleteException extends ItemException {
    public NoItemToDeleteException() {
        super("item.response.delete.unnecessary");
    }
}
