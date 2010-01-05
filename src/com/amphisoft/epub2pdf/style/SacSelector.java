/*
epub2pdf, version 0.1 - Copyright 2010 Brendan C. LeFebvre

This file is part of epub2pdf.

epub2pdf is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

epub2pdf is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with epub2pdf.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.amphisoft.epub2pdf.style;

import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

/**
 * Provides a more friendly reference to the CSS selector types defined in SAC.
 * In addition to the short values found in the original API, each selector type
 * has a human-readable string associated with it.
 * @author brendanl
 *
 */
public enum SacSelector {
    /* simple selectors */

    /**
     * This is a conditional selector.
     * example:
     * <pre class="example">
     *   simple[role="private"]
     *   .part1
     *   H1#myId
     *   P:lang(fr).p1
     * </pre>
     *
     * @see ConditionalSelector
     */
    SAC_CONDITIONAL_SELECTOR((short) 0, "conditional selector"),

    /**
     * This selector matches any node.
     * @see SimpleSelector
     */
    SAC_ANY_NODE_SELECTOR((short) 1, "any node"),

    /**
     * This selector matches the root node.
     * @see SimpleSelector
     */
    SAC_ROOT_NODE_SELECTOR((short) 2, "root node"),

    /**
     * This selector matches only node that are different from a specified one.
     * @see NegativeSelector
     */
    SAC_NEGATIVE_SELECTOR((short) 3, "negative selector"),

    /**
     * This selector matches only element node.
     * example:
     * <pre class="example">
     *   H1
     *   animate
     * </pre>
     * @see ElementSelector
     */
    SAC_ELEMENT_NODE_SELECTOR((short) 4, "element node"),

    /**
     * This selector matches only text node.
     * @see CharacterDataSelector
     */
    SAC_TEXT_NODE_SELECTOR((short) 5, "text node"),

    /**
     * This selector matches only cdata node.
     * @see CharacterDataSelector
     */
    SAC_CDATA_SECTION_NODE_SELECTOR((short) 6, "cdata node"),

    /**
     * This selector matches only processing instruction node.
     * @see ProcessingInstructionSelector
     */
    SAC_PROCESSING_INSTRUCTION_NODE_SELECTOR((short) 7, "processing instruction"),

    /**
     * This selector matches only comment node.
     * @see CharacterDataSelector
     */
    SAC_COMMENT_NODE_SELECTOR((short) 8, "comment node"),

    /**
     * This selector matches the 'first line' pseudo element.
     * example:
     * <pre class="example">
     *   :first-line
     * </pre>
     * @see ElementSelector
     */
    SAC_PSEUDO_ELEMENT_SELECTOR((short) 9, "pseudo element"),

    /* combinator selectors */

    /**
     * This selector matches an arbitrary descendant of some ancestor element.
     * example:
     * <pre class="example">
     *   E F
     * </pre>
     * @see DescendantSelector
     */
    SAC_DESCENDANT_SELECTOR((short) 10, "descendant selector"),

    /**
     * This selector matches a childhood relationship between two elements.
     * example:
     * <pre class="example">
     *   E > F
     * </pre>
     * @see DescendantSelector
     */
    SAC_CHILD_SELECTOR((short) 11, "child selector"),

    /**
     * This selector matches two selectors who shared the same parent in the
     * document tree and the element represented by the first sequence
     * immediately precedes the element represented by the second one.
     * example:
     * <pre class="example">
     *   E + F
     * </pre>
     * @see SiblingSelector
     */
    SAC_DIRECT_ADJACENT_SELECTOR((short) 12, "direct adjacent selector");


    public final short id;
    public final String name;

    SacSelector(short i, String n) {
        id = i;
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }

    public static String lookupName(short idQuery) {
        for (SacSelector s : SacSelector.values()) {
            if (s.id == idQuery) {
                return s.name;
            }
        }
        return null;
    }
}
