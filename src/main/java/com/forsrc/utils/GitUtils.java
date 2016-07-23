package com.forsrc.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitUtils {

    public static Repository getRepository(File path) throws IOException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.setGitDir(path);
        return repositoryBuilder.build();
    }

    public static Git getGit(File path) throws IOException {
        return Git.open(path);
    }
}
