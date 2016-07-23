package com.forsrc.utils;


import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StarDictUtils {

    public static byte UTF8_END_BYTE = 0x00;
    public static String CHARSET_NAME = "UTF-8";

    public static int byteArrayToInt(final byte[] bytesToConvert) {
        byte[] bytes = bytesToConvert;
        if (bytesToConvert.length < 4) {
            bytes = new byte[4];
            System.arraycopy(bytesToConvert, 0, bytes, 0, bytesToConvert.length);
        }
        return (bytes[0] & 0xff) << 24 |
                (bytes[1] & 0xff) << 16 |
                (bytes[2] & 0xff) << 8 |
                (bytes[3] & 0xff);
    }

    public static String filterUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes(CHARSET_NAME);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            } else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            } else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }
        }
        buffer.flip();
        return new String(buffer.array(), CHARSET_NAME);
    }

    public static Info getInfo(File file) throws FileNotFoundException {
        Info info = new Info();
        Scanner scanner = new Scanner(file, CHARSET_NAME);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ((line).indexOf('=') != -1) {
                String[] pair = line.split("=", 2);
                info.getInfo().put(pair[0], pair[1]);
            }
        }
        return info;
    }

    public static byte[] getFileBytes(File file) throws IOException {
        byte[] fileBytes;
        FileInputStream fileIn = new FileInputStream(file);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[524288];   // 512KB
        int bytesCount;
        while (fileIn.available() != 0) {
            bytesCount = fileIn.read(buffer);
            outStream.write(buffer, 0, bytesCount);
        }
        fileBytes = outStream.toByteArray();
        fileIn.close();
        outStream.close();
        return fileBytes;
    }

    public static Syn getSyn(int currentIndex, byte[] fileBytes) throws UnsupportedEncodingException {
        if (currentIndex >= fileBytes.length) {
            return null;
        }
        Syn syn = new Syn();
        syn.setCurrentIndex(currentIndex);
        int baseIndex = currentIndex;
        while (fileBytes[currentIndex] != UTF8_END_BYTE) {
            currentIndex++;
            if (currentIndex >= fileBytes.length) {
                syn.setNextIndex(fileBytes.length);
                return syn;
            }
        }
        int wordBytesCount = currentIndex - baseIndex;
        String word = new String(fileBytes, baseIndex, wordBytesCount, CHARSET_NAME);
        syn.setWord(word);
        currentIndex++;
        byte[] synIndexBytes = new byte[4];
        System.arraycopy(fileBytes, currentIndex, synIndexBytes, 0, 4);
        int synIndex = byteArrayToInt(synIndexBytes);
        currentIndex += 4;
        syn.setNextIndex(currentIndex);
        return syn;
    }

    public static Idx getIdx(int currentIndex, byte[] fileBytes) throws UnsupportedEncodingException {
        if (currentIndex >= fileBytes.length) {
            return null;
        }
        Idx idx = new Idx();
        int baseIndex = currentIndex;
        idx.setCurrentIndex(currentIndex);

        while (fileBytes[currentIndex] != UTF8_END_BYTE) {
            currentIndex++;
            if (currentIndex >= fileBytes.length) {
                idx.setNextIndex(fileBytes.length);
                return idx;
            }
        }

        int wordBytesCount = currentIndex - baseIndex;
        String word = new String(fileBytes, baseIndex, wordBytesCount, CHARSET_NAME);
        idx.setWord(word);
        currentIndex++;
        byte[] dataOffsetBytes = new byte[4];
        System.arraycopy(fileBytes, currentIndex, dataOffsetBytes, 0, 4);
        int dataOffset = byteArrayToInt(dataOffsetBytes);
        idx.setDataOffset(dataOffset);
        currentIndex += 4;
        byte[] dataSizeBytes = new byte[4];
        System.arraycopy(fileBytes, currentIndex, dataSizeBytes, 0, 4);
        int dataSize = byteArrayToInt(dataSizeBytes);
        idx.setDataSize(dataSize);
        currentIndex += 4;
        idx.setNextIndex(currentIndex);
        return idx;
    }

    public static void handle(String fileName, Handler handler) throws Exception {
        File idxFile = new File(fileName + ".idx");
        File dictFile = new File(fileName + ".dict");
        File ifoFile = new File(fileName + ".ifo");
        File synFile = new File(fileName + ".syn");
        Info info = getInfo(ifoFile);
        int currentIndex = 0;
        Idx idx = null;
        Syn syn = null;
        byte[] idxFileBytes = getFileBytes(idxFile);
        byte[] dictFileBytes = getFileBytes(dictFile);
        byte[] synFileBytes = synFile.exists() ? getFileBytes(synFile) : new byte[0];
        int index = 0;
        idx = getIdx(currentIndex, idxFileBytes);
        if (idx == null) {
            return;
        }
        do {
            String definition = getDefinition(dictFileBytes, idx.getDataOffset(), idx.getDataSize());
            syn = getSyn(currentIndex, synFileBytes);
            currentIndex = idx.getNextIndex();
            Idx next = getIdx(currentIndex, idxFileBytes);
            if (!handler.handle(index++, next != null, info, idx, syn, definition)) {
                break;
            }
            idx = next;
        } while (idx != null);
    }

    public static String getDefinition(byte[] fileBytes, int offset, int size) throws UnsupportedEncodingException {
        if (offset >= fileBytes.length) {
            return "";
        }
        size = offset + size > fileBytes.length ? fileBytes.length - offset : size;
        String definition = new String(fileBytes, offset, size, CHARSET_NAME);
        return definition;
    }

    public static interface Handler {
        public boolean handle(int index, boolean hasNext, Info info, Idx idx, Syn syn, String definition) throws Exception;
    }

    public static class Info {

        private Map<String, String> info = new HashMap<String, String>();

        public String getVersion() {
            return info.get("version");
        }

        public String getDate() {
            return info.get("date");
        }

        public String getDescription() {
            return info.get("description");
        }

        public String getSameTypeSequence() {
            return info.get("sametypesequence");
        }

        public String getIdxFileSize() {
            return info.get("idxfilesize");
        }

        public String getBookname() {
            return info.get("bookname");
        }

        public String getAuthor() {
            return info.get("author");
        }

        public String getWordcount() {
            return info.get("wordcount");
        }

        public String getSynwordcount() {
            return info.get("synwordcount");
        }

        public Map<String, String> getInfo() {
            return this.info;
        }

        public void setInfo(Map<String, String> info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "info=" + info +
                    '}';
        }
    }

    public static class Idx {

        private String word;
        private int dataOffset;
        private int dataSize;
        private int nextIndex;
        private int currentIndex;

        public int getNextIndex() {
            return this.nextIndex;
        }

        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }

        public String getWord() {
            return this.word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getDataOffset() {
            return this.dataOffset;
        }

        public void setDataOffset(int dataOffset) {
            this.dataOffset = dataOffset;
        }

        public int getDataSize() {
            return this.dataSize;
        }

        public void setDataSize(int dataSize) {
            this.dataSize = dataSize;
        }

        public int getCurrentIndex() {
            return this.currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }
    }

    public static class Syn {
        private String word;
        private int currentIndex;
        private int synIndex;
        private int nextIndex;

        public String getWord() {
            return this.word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getCurrentIndex() {
            return this.currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        public int getSynIndex() {
            return this.synIndex;
        }

        public void setSynIndex(int synIndex) {
            this.synIndex = synIndex;
        }

        public int getNextIndex() {
            return this.nextIndex;
        }

        public void setNextIndex(int nextIndex) {
            this.nextIndex = nextIndex;
        }
    }


}
