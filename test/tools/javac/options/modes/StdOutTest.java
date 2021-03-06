/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8044859
 * @summary test support for  -Xstdout file  and  -XDstdout
 * @build Tester
 * @run main StdOutTest
 */

import com.sun.tools.javac.main.Main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StdOutTest extends Tester {
    public static void main(String... args) throws Exception {
        StdOutTest t = new StdOutTest();
        t.writeFile("src/Bad.java", "class Bad");
        t.runTests();
    }

    @Test
    void testXstdout() throws IOException {
        String[] args = { "-Xstdout", "out.txt" };
        String[] files = { "src/Bad.java" };

        // verify messages get written to the specified file
        runMain(args, files)
                .checkResult(Main.Result.ERROR.exitCode);
        if (!Files.exists(Paths.get("out.txt"))) {
            error("expected file not found");
        }

        runCall(args, files)
                .checkIllegalArgumentException();

        runParse(args, files)
                .checkIllegalArgumentException();
    }

    @Test
    void testXDstdout() throws IOException {
        String[] args = { "-XDstdout" };
        String[] files = { "src/Bad.java" };

        runMain(args, files)
                .checkLog(Log.STDOUT, "Bad");

        runCall(args, files)
                .checkLog(Log.DIRECT, "Bad");

        runParse(args, files)
                .checkLog(Log.DIRECT, "Bad");
    }
}
