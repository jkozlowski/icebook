/*****************************************************************************
 * Copyright (C) Codehaus.org                                                *
 * ------------------------------------------------------------------------- *
 * Licensed under the Apache License, Version 2.0 (the "License");           *
 * you may not use this file except in compliance with the License.          *
 * You may obtain a copy of the License at                                   *
 *                                                                           *
 * http://www.apache.org/licenses/LICENSE-2.0                                *
 *                                                                           *
 * Unless required by applicable law or agreed to in writing, software       *
 * distributed under the License is distributed on an "AS IS" BASIS,         *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 * See the License for the specific language governing permissions and       *
 * limitations under the License.                                            *
 *****************************************************************************/
package org.codehaus.jparsec;

import org.codehaus.jparsec.Tokens.Fragment;

/**
 * Returns the fragment text if the token is an instance of {@Link Fragment} with expected tag.
 *
 * @author Ben Yu
 */
abstract class IsFragment implements TokenMap<String> {
  
  public String map(final Token token) {
    final Object val = token.value();
    if (val instanceof Fragment) {
      Fragment c = (Fragment) val;
      if (!isExpectedTag(c.tag())) return null;
      return c.text();
    }
    else return null;
  }
  
  /** Is {@code type} expected? */
  abstract boolean isExpectedTag(Object type);
}