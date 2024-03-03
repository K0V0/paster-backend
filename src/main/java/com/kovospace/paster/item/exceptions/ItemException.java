package com.kovospace.paster.item.exceptions;

import com.kovospace.paster.base.exceptions.BaseException;

public abstract class ItemException extends BaseException {
  public ItemException() {}
  public ItemException(final String code) { super(code); }
  public ItemException(final String code, final String message) { super(code, message); }
}
