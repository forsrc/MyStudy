package com.forsrc.utils;


import java.io.*;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileUtils {
    public static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    public static final Lock READ_LOCK = LOCK.readLock();
    public static final Lock WRITE_LOCK = LOCK.writeLock();
    private static final int BUFFER_SIZE = 1024 * 512;
    private static final Charset CHARSET = Charset.forName("UTF-8");
    public static boolean isDebug = false;

    public static void readLine(File file, FileReadLineAdapter adapter, boolean isUseLock) throws IOException {
        if (!isUseLock) {
            readLine(file, adapter);
            return;
        }
        READ_LOCK.lock();
        try {
            readLine(file, adapter);
        } finally {
            READ_LOCK.unlock();
        }
    }

    public static void readLine(File file, FileReadLineAdapter adapter) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File is not exists: " + file);
        }
        long startTime = System.currentTimeMillis();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    file)), BUFFER_SIZE);
            String line = null;
            long index = 0;
            while ((line = br.readLine()) != null) {
                index++;
                adapter.todo(index, line);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(br);
        }
        if (isDebug) {
            LogUtils.LOGGER.debug(String.format("%s readLine: %.3f s", Thread
                            .currentThread().getName(),
                    (System.currentTimeMillis() - startTime) / 1000f));
        }
    }

    public static void randomAccessFileReadLine(File file,
                                                long start,
                                                long length,
                                                FileReadLineAdapter adapter,
                                                boolean isUseLock) throws IOException {
        if (!isUseLock) {
            randomAccessFileReadLine(file, start, length, adapter);
            return;
        }
        READ_LOCK.lock();
        try {
            randomAccessFileReadLine(file, start, length, adapter);
        } finally {
            READ_LOCK.unlock();
        }
    }

    public static void randomAccessFileReadLine(File file, long start,
                                                long length, FileReadLineAdapter adapter) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File is not exists: " + file);
        }
        long startTime = System.currentTimeMillis();
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");

            String line = null;
            long index = -1;

            while ((line = raf.readLine()) != null) {
                index++;
                if (index < start) {
                    continue;
                }
                if (index - start >= length) {
                    break;
                }
                adapter.todo(index, line);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(raf);
        }
        if (isDebug) {
            LogUtils.LOGGER.debug(String.format("randomAccessFileReadLine: %.3f s",
                    (System.currentTimeMillis() - startTime) / 1000f));
        }
    }

    public static void scannerReadLine(File file, FileReadLineAdapter adapter, boolean isUseLock) throws IOException {
        if (!isUseLock) {
            scannerReadLine(file, adapter);
            return;
        }
        READ_LOCK.lock();
        try {
            scannerReadLine(file, adapter);
        } finally {
            READ_LOCK.unlock();
        }
    }

    public static void scannerReadLine(File file, FileReadLineAdapter adapter) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File is not exists: " + file);
        }
        long startTime = System.currentTimeMillis();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw e;
        }

        long index = 0;
        while (scanner.hasNext()) {
            index++;
            String line = scanner.next();
            adapter.todo(index, line);
        }
        if (scanner != null) {
            scanner.close();
        }
        if (isDebug) {
            LogUtils.LOGGER.debug(String.format("scannerReadLine: %.3f s",
                    (System.currentTimeMillis() - startTime) / 1000f));
        }
    }

    public static void readLine(final File file,
                                final long start,
                                final long length,
                                FileReadLineAdapter adapter,
                                boolean isUseLock) throws IOException {
        if (!isUseLock) {
            readLine(file, start, length, adapter);
            return;
        }
        READ_LOCK.lock();
        try {
            readLine(file, start, length, adapter);
        } finally {
            READ_LOCK.unlock();
        }
    }

    public static void readLine(final File file,
                                final long start,
                                final long length,
                                FileReadLineAdapter adapter) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File is not exists: " + file);
        }
        if (start >= file.length()) {
            throw new IllegalArgumentException(String.format(
                    "The start(%s) >= file(%s) length(%s)", start, file,
                    file.length()));
        }

        long readLength = length <= 0 ? file.length() : length;

        long readStart = start <= 0 ? 0 : start;
        long startTime = System.currentTimeMillis();
        BufferedReader br = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);
            is.getChannel().position(readStart);
            String line = null;
            long index = 0;
            long pos = is.getChannel().position();
            for (long i = pos; i >= 0; i--) {
                is.getChannel().position(i);
                int c = is.read();
                if (c == '\n') {
                    readStart = i + 1;
                    break;
                }
            }
            readLength = readStart + readLength > file.length() ? file.length()
                    - readStart : readLength;
            is.getChannel().position(readStart);
            pos = readStart;
            while ((line = br.readLine()) != null) {
                index++;
                pos += line.getBytes(CHARSET).length + 2;
                if (pos > readStart + readLength) {
                    break;
                }
                if (adapter != null) {
                    adapter.todo(index, line);
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(is);
            closeQuietly(br);
        }
        if (isDebug) {
            LogUtils.LOGGER.debug(String.format("%s readLine: %.3f s", Thread
                            .currentThread().getName(),
                    (System.currentTimeMillis() - startTime) / 1000f));
        }
    }

    public static void rewriteLine(final File file,
                                   final File saveFile,
                                   final long start,
                                   final long length,
                                   FileWriteReadLineAdapter adapter,
                                   boolean isUseLock) throws IOException {
        if (!isUseLock) {
            rewriteLine(file, saveFile, start, length, adapter);
            return;
        }
        WRITE_LOCK.lock();
        try {
            rewriteLine(file, saveFile, start, length, adapter);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    public static void rewriteLine(final File file,
                                   final File saveFile,
                                   final long start,
                                   final long length,
                                   FileWriteReadLineAdapter adapter) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (saveFile == null) {
            throw new IllegalArgumentException("SaveFile is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File is not exists: " + file);
        }
        if (start >= file.length()) {
            LogUtils.LOGGER.debug(MessageFormat.format(
                    "[SKIP] The start({0}) >= file({1}) length({2})", start, file,
                    file.length()));
            return;
        }

        long readLength = length <= 0 ? file.length() : length;
        long readStart = start <= 0 ? 0 : start;
        long startTime = System.currentTimeMillis();
        BufferedReader br = null;
        BufferedWriter bw = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(saveFile, false)), BUFFER_SIZE);
            is.getChannel().position(readStart);
            String line = null;
            long index = 0;
            long pos = is.getChannel().position();
            for (long i = pos; i >= 0; i--) {
                is.getChannel().position(i);
                int c = is.read();
                if (c == '\n') {
                    readStart = i + 1;
                    break;
                }
            }
            readLength = readStart + readLength > file.length() ? file.length()
                    - readStart : readLength;
            is.getChannel().position(readStart);
            pos = readStart;
            while ((line = br.readLine()) != null) {
                index++;
                pos += line.getBytes(CHARSET).length + 2;
                if (pos > readStart + readLength) {
                    break;
                }
                if (adapter != null) {
                    line = adapter.todo(index, line);
                }
                bw.write(line);
                bw.newLine();

            }

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(is);
            closeQuietly(br);
            closeQuietly(bw);
        }
        if (isDebug) {
            LogUtils.LOGGER.debug(String.format(
                    "%s rewriteLine(%s, %s, %s, %s, %s): %.3f s", Thread
                            .currentThread().getName(), file, saveFile, start,
                    length, adapter,
                    (System.currentTimeMillis() - startTime) / 1000f));
        }
    }


    public static void cutFile(File file, long length) throws IOException {
        cutFile(file, length, null);
    }

    public static void cutFile(File file, long length,
                               FileWriteReadLineAdapter adapter) throws IOException {
        check(file);
        if (length >= file.length()) {
            //return;
        }
        if (length < 1024 * 1024 * 10) {
            // return;
        }

        int count = (int) Math.ceil(file.length() / length) + 1;
        long start = 0;
        for (int i = 0; i < count; i++) {
            File saveFile = new File(MessageFormat.format("{0}.{1}", file, i));
            rewriteLine(file, saveFile, start, length, adapter);
            start += length;
        }
        // LogUtils.LOGGER.debug(String.format("%s rewriteLine(%s, %s, %s): %.3f s",
        // Thread.currentThread().getName(), file, length, adapter,
        // (System.currentTimeMillis() - startTime) / 1000f));
    }

    public static void check(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File is not exists: " + file);
        }

    }

    public static void threadReadLine(final File file, final long length,
                                      int thread, final FileReadLineAdapter adapter) throws IOException, InterruptedException {
        check(file);
        if (length >= file.length()) {
            // return;
        }

        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,
                thread >= 3 ? thread : 3, 10, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // ExecutorService threadPool = Executors.newFixedThreadPool(thread);
        int count = (int) Math.ceil(file.length() / length) + 1;
        long start = 0;
        for (int i = 0; i < count; i++) {
            final long s = start;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        readLine(file, s, length, adapter);
                    } catch (IOException e) {
                        LogUtils.LOGGER.error(e.getMessage(), e);
                    }
                }
            });
            start += length;
        }
        threadPool.shutdown();
        try {
            while (threadPool != null
                    && !threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            throw e;
        } finally {
            threadPool.shutdownNow();
        }
        if (isDebug) {
            LogUtils.LOGGER.debug(String.format("threadReadLine(%s, %s, %s): %.3f s",
                    file, length, adapter,
                    (System.currentTimeMillis() - startTime) / 1000f));
        }
    }

    public static void threadCutFile(final File file, final long length,
                                     int thread, final FileWriteReadLineAdapter adapter) throws InterruptedException {
        check(file);
        if (length >= file.length()) {
            // return;
        }

        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,
                thread >= 3 ? thread : 3, 10, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(3),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // ExecutorService threadPool = Executors.newFixedThreadPool(thread);
        int count = (int) Math.ceil(file.length() / length) + 1;

        for (int i = 0; i < count; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        cutFile(file, length, adapter);
                    } catch (IOException e) {
                        LogUtils.LOGGER.error(e.getMessage(), e);
                    }
                }
            });
        }
        threadPool.shutdown();
        try {
            while (threadPool != null
                    && !threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            throw e;
        } finally {
            threadPool.shutdownNow();
        }
        if (isDebug) {
            LogUtils.LOGGER.debug(String.format("threadCutFile(%s, %s, %s): %.3f s",
                    file, length, adapter,
                    (System.currentTimeMillis() - startTime) / 1000f));
        }
    }

    /**
     * @param @param  src
     * @param @param  dst
     * @param @return
     * @return boolean
     * @throws IOException
     * @throws
     * @Title: saveFile
     * @Description:
     */
    public static void saveFile(File src, File dst) throws IOException {

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), 1024 * 10);
            out = new BufferedOutputStream(new FileOutputStream(dst), 1024 * 10);
            byte[] buffer = new byte[1024 * 10];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /**
     * @param @param file
     * @param @param info
     * @return void
     * @throws IOException
     * @throws
     * @Title: setFileTxt
     * @Description:
     */
    public static void setFileTxt(File file, String info) throws IOException {
        setFileTxt(file, info, false);
    }

    /**
     * @param @param file
     * @param @param info
     * @param @param append
     * @return void
     * @throws IOException
     * @throws
     * @Title: setFileTxt
     * @Description:
     */
    public static void setFileTxt(File file, String info, boolean append)
            throws IOException {
        setFileTxt(file, info, Charset.defaultCharset(), append);
    }

    public static void setFileTxt(File file, String info, Charset charset,
                                  boolean append) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, append), charset));
            bw.write(info);
            bw.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(bw);
        }
    }

    /**
     * @param @param  file
     * @param @return
     * @return String
     * @throws IOException
     * @throws FileNotFoundException
     * @throws
     * @Title: getFileTxt
     * @Description:
     */
    public static String getFileTxt(File file) throws IOException {
        return getFileTxt(file, -1, -1);
    }

    /**
     * @param @param  file
     * @param @param  start
     * @param @param  number
     * @param @return
     * @return String
     * @throws IOException
     * @throws
     * @Title: getFileTxt
     * @Description:
     */
    public static String getFileTxt(File file, long startLine, long countLine) throws IOException {
        return getFileTxt(file, Charset.defaultCharset(), startLine, countLine);
    }

    /**
     * @param @param  file
     * @param @param  charsetName
     * @param @param  startLine
     * @param @param  countLine
     * @param @return
     * @return String
     * @throws Exception
     * @throws
     * @Title: getFileTxt
     * @Description:
     */
    public static String getFileTxt(File file, Charset charset, long startLine,
                                    long countLine) throws IOException {
        // if (file == null || !file.exists()) {
        // throw new FileNotFoundException(file.getAbsolutePath());
        // }
        String info = "";
        BufferedReader br = null;
        long start = startLine < 0 ? 0 : startLine;
        long count = countLine < 0 ? -1 : countLine;
        try {
            br = new BufferedReader(new InputStreamReader(
                    charset == null ? new FileInputStream(file)
                            : new FileInputStream(file), charset), 1024 * 10);
            StringBuffer sb = new StringBuffer();
            String s = "";
            long index = 0;
            while ((s = br.readLine()) != null) {
                if (index++ < start) {
                    continue;
                }
                sb.append(s);
                sb.append("\n");
                if (count == -1) {
                    continue;
                }
                count--;
                if (count <= 0) {
                    break;
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            info = sb.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(br);
        }
        return info;
    }

    public static boolean getFileList(File dir, Set<File> arrayList) {
        return getFileList(dir, null, arrayList);
    }

    public static boolean getFileList(File dir, FileFilter filter, Set<File> set) {
        if (dir == null || !dir.exists()) {
            return false;
        }
        if (set == null) {
            set = new HashSet<File>();
        }
        if (dir.isFile()) {
            return true;
        }

        File[] files = filter == null ? dir.listFiles() : dir.listFiles(filter);
        for (File f : files) {
            set.add(f);
            if (f.isDirectory()) {
                getFileList(f, filter, set);
                continue;
            }
        }
        return true;
    }

    public static void close(InputStream in) throws IOException {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } catch (IOException e) {
            throw e;
        }
    }

    public static void close(OutputStream out) throws IOException {
        if (out == null) {
            return;
        }
        try {
            out.close();
        } catch (IOException e) {
            throw e;
        }
    }

    public static void close(InputStream in, OutputStream out)
            throws IOException {
        try {
            close(in);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                close(out);
            } catch (IOException e) {
                throw e;
            }
        }
    }

    public static void close(BufferedReader br) throws IOException {
        if (br == null) {
            return;
        }
        try {
            br.close();
        } catch (IOException e) {
            throw e;
        }
    }

    public static void close(BufferedWriter bw) throws IOException {
        if (bw == null) {
            return;
        }
        try {
            bw.close();
        } catch (IOException e) {
            throw e;
        }
    }

    public static void close(BufferedReader in, BufferedWriter out)
            throws IOException {
        try {
            close(in);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                close(out);
            } catch (IOException e) {
                throw e;
            }
        }
    }


    public static String getFileInfo(File file) {
        if (file == null) {
            return "File is null.";
        }
        if (!file.exists()) {
            return MessageFormat.format("Not exist: '{0}'", file);
        }

        return MessageFormat.format("{0} {1} {2}",
                DateTimeUtils.getDateTime(file.lastModified(), false),
                (file.isFile() ? "F" : "D"),
                (file.isFile() ? file.length() : ""));
    }

    public static String readTxtFile(File file) throws IOException {
        return readTxtFile(file, null);
    }

    public static String readTxtFile(File file, Charset charset) throws IOException {
        long fileLength = file.length();
        if (fileLength >= Integer.MAX_VALUE) {
            return null;
        }
        byte[] b = new byte[(int) fileLength];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(b);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(in);
        }
        return charset == null ? new String(b) : new String(b, charset);
    }

    public static void readTxtFile(File file, Charset charset,
                                   ReadTxtFileAdapter adapter, int bufferSize) throws IOException {
        long fileLength = file.length();
        if (fileLength <= bufferSize) {
            adapter.todo(readTxtFile(file, charset));
            return;
        }
        byte[] b = new byte[bufferSize];
        FileInputStream in = null;
        int length = 0;
        try {
            in = new FileInputStream(file);
            while ((length = in.read(b)) != -1) {
                adapter.todo(charset == null ? new String(b, 0, length)
                        : new String(b, 0, length, charset));
            }

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(in);
        }

    }

    public static void readTxtFile(File file, Charset charset,
                                   ReadTxtFileAdapter adapter, int bufferSize, String lineBreak) throws IOException {
        long fileLength = file.length();
        if (fileLength <= bufferSize) {
            adapter.todo(readTxtFile(file, charset));
            return;
        }
        byte[] b = new byte[bufferSize];
        FileInputStream in = null;
        int length = 0;
        try {
            in = new FileInputStream(file);

            String newLine = null;
            while ((length = in.read(b)) != -1) {
                String txt = charset == null ? new String(b, 0, length)
                        : new String(b, 0, length, charset);

                if (newLine != null) {
                    txt = String.format("%s%s", newLine, txt);
                }
                int offset = MyStringUtils.getLastLineBreakOffset(txt,
                        lineBreak);
                if (offset == -1) {
                    adapter.todo(txt);
                    newLine = null;
                    // System.out.println("	if (offset == -1)");
                    continue;
                }
                if (offset == 0 && lineBreak.length() == txt.length()) {
                    System.out.println("----");
                    continue;
                }
                if (offset >= 0 && offset < txt.length()) {
                    String txtTmp = txt;
                    txt = txtTmp.substring(0, offset - 1);
                    newLine = txtTmp.substring(offset + lineBreak.length());

                    adapter.todo(txt);
                    // System.out.println("if (offset < length)");
                    continue;
                }
                // adapter.todo(txt);
                newLine = null;
            }

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeQuietly(in);
        }

    }

    public static void readTxtFile(File file, Charset charset,
                                   ReadTxtFileAdapter adapter) throws IOException {
        readTxtFile(file, charset, adapter, BUFFER_SIZE);
    }

    public static void readLine(File file, Charset charset,
                                ReadLineAdapter adapter) throws IOException {
        // if (file == null || !file.exists()) {
        // throw new FileNotFoundException(file.getAbsolutePath());
        // }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    charset == null ? new FileInputStream(file)
                            : new FileInputStream(file), charset), 1024 * 10);
            String s = "";
            while ((s = br.readLine()) != null) {
                adapter.todo(s);
            }
        } catch (IOException e) {
            // e.printStackTrace();
            throw e;
        } finally {
            closeQuietly(br);
        }
    }

    public static boolean delete(File dir) {
        boolean ok = true;
        if (!dir.exists()) {
            LogUtils.LOGGER.info(MessageFormat.format("Delete... not exits {0}", dir));
            return true;
        }
        if (dir.isFile()) {
            ok &= dir.delete();
            LogUtils.LOGGER.info(MessageFormat.format("Delete... {1} File {0}", dir, ok));
            return ok;
        }
        File[] list = dir.listFiles();
        for (File file : list) {
            delete(file);
        }
        ok &= dir.delete();
        LogUtils.LOGGER.info(MessageFormat.format("Delete... {1} Directory {0}", dir, ok));
        return ok;
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LogUtils.LOGGER.debug(MessageFormat.format("closeQuietly() -> {0}", e.getMessage()));
        }

    }

    public interface ReadTxtFileAdapter {
        public void todo(String str);
    }

    public interface ReadLineAdapter {
        public void todo(String str);
    }
}
