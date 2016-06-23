package com.forsrc.lucene;


import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MySimpleFSDirectory extends SimpleFSDirectory {


    public MySimpleFSDirectory(Path path, LockFactory lockFactory) throws IOException {
        super(path, lockFactory);
    }

    public MySimpleFSDirectory(Path path) throws IOException {
        super(path);
    }

    public MySimpleFSDirectory(String path) throws IOException {
        super(Paths.get(path));
    }
}
