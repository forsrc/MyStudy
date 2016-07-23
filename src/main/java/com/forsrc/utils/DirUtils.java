/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.forsrc.utils;


import java.io.File;
import java.io.IOException;

public class DirUtils {

    public static void file(final File file, final DirHandler dirHandler, final FileHandler fileHandler) throws HandleException {

        if (file.isFile()) {
            fileHandler.handle(file);
            return;
        }
        dirHandler.handle(file);
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File currentFile : files) {
            if (currentFile.isFile()) {
                fileHandler.handle(currentFile);
                continue;
            }
            if (currentFile.isDirectory()) {
                file(currentFile, dirHandler, fileHandler);
            }
        }

    }

    public static void file(final String fileName, final DirHandler dirHandler, final FileHandler fileHandler) throws HandleException {
        File file = new File(fileName);
        if (file.isFile()) {
            fileHandler.handle(file);
            return;
        }
        boolean isContinue = dirHandler.handle(file);
        if (!isContinue) {
            return;
        }
        String[] files = file.list();
        if (files == null) {
            return;
        }
        for (String currentFileName : files) {
            File currentFile = new File(file.getAbsolutePath() + File.separator + currentFileName);
            if (currentFile.isFile()) {
                isContinue = fileHandler.handle(currentFile);
                if (!isContinue) {
                    return;
                }
                continue;
            }
            if (currentFile.isDirectory()) {
                file(currentFile.getAbsolutePath(), dirHandler, fileHandler);
            }
        }

    }

    public static void dir(final File dir, final DirHandler dirHandler, final FileHandler fileHandler) throws HandleException {
        if (dir.isFile()) {
            fileHandler.handle(dir);
            return;
        }
        boolean isContinue = dirHandler.handle(dir);
        if (!isContinue) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        dir(dir, dirHandler, fileHandler);

    }

    public static void dir(File[] dirList, final DirHandler dirHandler, final FileHandler fileHandler) throws HandleException {
        if (dirList == null) {
            return;
        }

        boolean isContinue = true;

        for (File currentFile : dirList) {
            if (currentFile.isFile()) {
                isContinue = fileHandler.handle(currentFile);
                if (!isContinue) {
                    return;
                }
                continue;
            }
            if (currentFile.isDirectory()) {
                isContinue = dirHandler.handle(currentFile);
                if (!isContinue) {
                    return;
                }
                dir(currentFile.listFiles(), dirHandler, fileHandler);
            }
        }
    }

    public static interface DirHandler {
        public boolean handle(final File dir) throws HandleException;
    }

    public static interface FileHandler {
        public boolean handle(final File file) throws HandleException;
    }

    public static class HandleException extends IOException {

    }

}
