package com.forsrc.lucene;


import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type My simple fs directory.
 */
public class MySimpleFSDirectory extends SimpleFSDirectory {


    /**
     * Instantiates a new My simple fs directory.
     *
     * @param path        the path
     * @param lockFactory the lock factory
     * @throws IOException the io exception
     */
    public MySimpleFSDirectory(Path path, LockFactory lockFactory) throws IOException {
        super(path, lockFactory);
    }

    /**
     * Instantiates a new My simple fs directory.
     *
     * @param path the path
     * @throws IOException the io exception
     */
    public MySimpleFSDirectory(Path path) throws IOException {
        super(path);
    }

    /**
     * Instantiates a new My simple fs directory.
     *
     * @param path the path
     * @throws IOException the io exception
     */
    public MySimpleFSDirectory(String path) throws IOException {
        super(Paths.get(path));
    }
}
