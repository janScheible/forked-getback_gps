/**
 * Collection of useful methods.
 *
 * Copyright (C) 2014 Dieter Adriaenssens
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @package com.github.ruleant.getback_gps.lib
 * @author  Dieter Adriaenssens <ruleant@users.sourceforge.net>
 */
package com.github.ruleant.getback_gps.lib;

/**
 * Collection of useful methods.
 *
 * @author  Dieter Adriaenssens <ruleant@users.sourceforge.net>
 */
public class Tools {
    /**
     * Hidden constructor, to prevent instantiating.
     */
    protected Tools() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * Return biggest number.
     *
     * @param value1 one value
     * @param value2 another value
     * @return biggest number
     */
    public static long getMax(final long value1, final long value2) {
        if (value1 > value2) {
            return value1;
        } else {
            return value2;
        }
    }
}