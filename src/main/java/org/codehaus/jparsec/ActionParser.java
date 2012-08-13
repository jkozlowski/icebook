/*
 * Copyright 2012 Jakub Dominik Kozlowski <mail@jakub-kozlowski.com>
 * Distributed under the The MIT License.
 * (See accompanying file LICENSE)
 */
package org.codehaus.jparsec;

final class ActionParser extends Parser<Object> {
  private final Runnable action;
  
  ActionParser(Runnable action) {
    this.action = action;
  }
  
  @Override boolean apply(ParseContext ctxt) {
    action.run();
    return true;
  }
  
  @Override public String toString() {
    return action.toString();
  }
}
