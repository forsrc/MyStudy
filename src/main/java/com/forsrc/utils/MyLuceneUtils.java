package com.forsrc.utils;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 * The type My lucene utils.
 */
public class MyLuceneUtils {
    private static ThreadLocal<String> _indexDir = new ThreadLocal<String>();
    private static ThreadLocal<String> _simpleHTMLFormatterPreTag = new ThreadLocal<String>();
    private static ThreadLocal<String> _simpleHTMLFormatterPostTag = new ThreadLocal<String>();
    private static ThreadLocal<Analyzer> _analyzer = new ThreadLocal<Analyzer>();
    private static ThreadLocal<IndexWriter> _indexWriter = new ThreadLocal<IndexWriter>();

    private MyLuceneUtils() {
    }

    private static class MyLuceneUtilsStaticClass {
        private static final MyLuceneUtils INSTANCE = new MyLuceneUtils();
    }

    /**
     * Create my lucene utils.
     *
     * @return the my lucene utils
     */
    public static MyLuceneUtils create() {
        return MyLuceneUtilsStaticClass.INSTANCE;
    }

    /**
     * Create my lucene utils.
     *
     * @param indexWriter the index writer
     * @return the my lucene utils
     */
    public static MyLuceneUtils create(IndexWriter indexWriter) {
        MyLuceneUtils myLuceneUtils = MyLuceneUtilsStaticClass.INSTANCE;
        myLuceneUtils.setIndexWriter(indexWriter);
        return myLuceneUtils;
    }

    /**
     * Create my lucene utils.
     *
     * @param indexWriter the index writer
     * @param analyzer    the analyzer
     * @return the my lucene utils
     */
    public static MyLuceneUtils create(IndexWriter indexWriter, Analyzer analyzer) {
        MyLuceneUtils myLuceneUtils = MyLuceneUtilsStaticClass.INSTANCE;
        myLuceneUtils.setIndexWriter(indexWriter);
        myLuceneUtils.setAnalyzer(analyzer);
        return myLuceneUtils;
    }

    /**
     * Create my lucene utils.
     *
     * @param indexWriter the index writer
     * @param analyzer    the analyzer
     * @param indexDir    the index dir
     * @return the my lucene utils
     */
    public static MyLuceneUtils create(IndexWriter indexWriter, Analyzer analyzer, String indexDir) {
        MyLuceneUtils myLuceneUtils = MyLuceneUtilsStaticClass.INSTANCE;
        myLuceneUtils.setIndexWriter(indexWriter);
        myLuceneUtils.setAnalyzer(analyzer);
        myLuceneUtils.setIndexDir(indexDir);
        return myLuceneUtils;
    }

    /**
     * Close my lucene utils.
     *
     * @return the my lucene utils
     * @throws IOException the io exception
     */
    public MyLuceneUtils close() throws IOException {
        _indexDir.remove();
        _simpleHTMLFormatterPostTag.remove();
        _simpleHTMLFormatterPostTag.remove();
        Analyzer analyzer = _analyzer.get();
        if (analyzer != null) {
            analyzer.close();
            _analyzer.remove();
        }

        IndexWriter indexWriter = _indexWriter.get();
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException e) {
                throw e;
            } finally {
                indexWriter = null;
                _indexDir.remove();
            }
        }
        return this;
    }

    /**
     * Gets index dir.
     *
     * @return the index dir
     */
    public static String getIndexDir() {
        return _indexDir.get();
    }

    /**
     * Sets index dir.
     *
     * @param indexDir the index dir
     * @return the index dir
     */
    public MyLuceneUtils setIndexDir(String indexDir) {
        _indexDir.set(indexDir);
        return this;
    }

    /**
     * Gets simple html formatter pre tag.
     *
     * @return the simple html formatter pre tag
     */
    public String getSimpleHTMLFormatterPreTag() {
        return _simpleHTMLFormatterPreTag.get();
    }

    /**
     * Sets simple html formatter pre tag.
     *
     * @param simpleHTMLFormatterPreTag the simple html formatter pre tag
     * @return the simple html formatter pre tag
     */
    public MyLuceneUtils setSimpleHTMLFormatterPreTag(String simpleHTMLFormatterPreTag) {
        _simpleHTMLFormatterPreTag.set(simpleHTMLFormatterPreTag);
        return this;
    }

    /**
     * Gets simple html formatter post tag.
     *
     * @return the simple html formatter post tag
     */
    public String getSimpleHTMLFormatterPostTag() {
        return _simpleHTMLFormatterPostTag.get();
    }

    /**
     * Sets simple html formatter post tag.
     *
     * @param simpleHTMLFormatterPostTag the simple html formatter post tag
     * @return the simple html formatter post tag
     */
    public MyLuceneUtils setSimpleHTMLFormatterPostTag(String simpleHTMLFormatterPostTag) {
        _simpleHTMLFormatterPostTag.set(simpleHTMLFormatterPostTag);
        return this;
    }

    /**
     * Gets analyzer.
     *
     * @return the analyzer
     */
    public Analyzer getAnalyzer() {
        return _analyzer.get();
    }

    /**
     * Sets analyzer.
     *
     * @param analyzer the analyzer
     * @return the analyzer
     */
    public MyLuceneUtils setAnalyzer(Analyzer analyzer) {
        _analyzer.set(analyzer);
        return this;
    }

    /**
     * Gets index writer.
     *
     * @return the index writer
     */
    public IndexWriter getIndexWriter() {
        return _indexWriter.get();
    }

    /**
     * Sets index writer.
     *
     * @param indexWriter the index writer
     * @return the index writer
     */
    public MyLuceneUtils setIndexWriter(IndexWriter indexWriter) {
        _indexWriter.set(indexWriter);
        return this;
    }
}
