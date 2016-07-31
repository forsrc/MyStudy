package com.forsrc.utils;

import java.util.Scanner;

/**
 * The interface File read line scanner adapter.
 */
public interface FileReadLineScannerAdapter extends FileReadLineAdapter {
    /**
     * Init.
     *
     * @param scanner the scanner
     */
    public void init(Scanner scanner);
}
