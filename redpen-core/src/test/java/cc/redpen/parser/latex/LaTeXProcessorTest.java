/**
 * redpen: a text inspection tool
 * Copyright (c) 2014-2015 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen.parser.latex;

import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayDeque;

import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static cc.redpen.parser.latex.Tools.*;
import static cc.redpen.parser.latex.Assert.*;

import org.pegdown.ast.*;
import org.apache.commons.lang3.StringUtils;

public class LaTeXProcessorTest {
    @Test
    public void testParse() {
        final List<Token> bucket = new ArrayList<>();
        final String code =
              "\\documentclass[a4paper]{jsarticle}\n"
            + "\\begin{document}\n"
            + "\\section{First Section}\n"
            + "Test text.\n"
            + "\\end{document}";

        assertEquals(
            "(RootNode (HeaderNode#3 (TextNode 'First Section') (ParaNode (TextNode 'Test text.') (SimpleNode#Linebreak))))",
            summary(
                new LaTeXProcessor().parse(code.toCharArray())
            )
        );
    }

    @Test
    public void testParseCommentDoesNotBreakOngoingParagraph() {
        final String code = ""
            + "\\documentclass[a4paper]{jsarticle}\n"
            + "\\begin{document}\n"
            + "There are various tests.\n"
            + "% The following should be exmples\n"
            + "Most common one is unit test.\n"
            + "Integration test is also common.\n"
            + "\\end{document}\n";
        assertEquals(
            "(RootNode (ParaNode (TextNode 'There are various tests.') (SimpleNode#Linebreak) (TextNode 'Most common one is unit test.') (SimpleNode#Linebreak) (TextNode 'Integration test is also common.') (SimpleNode#Linebreak) (SimpleNode#Linebreak)))",
            summary(
                new LaTeXProcessor().parse(code.toCharArray())
            )
        );
    }

    @Test
    public void testWalk() {
        assertEquals(
            "(RootNode (HeaderNode#3 (TextNode 'First Section') (ParaNode (TextNode 'Test text.'))))",
            summary(
                LaTeXProcessor.P.walk(
                    Arrays.asList(
                        token("SECTION", "First Section"),
                        token("TEXTILE", "Test text.")
                    )
                )
            )
        );
    }

}
