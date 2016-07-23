package com.forsrc.utils;


import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class AvroUtils<E> {
    private AvroUtils() {

    }


    public static <E> List<E> fromBytes(byte[] bytes, Class<E> e) throws IOException {
        DatumReader<E> datumReader = new SpecificDatumReader<E>(e);
        SeekableInput input = new SeekableByteArrayInput(bytes);
        DataFileReader<E> dataFileReader = new DataFileReader<E>(input, datumReader);

        List<E> list = new ArrayList<E>();
        Iterator<E> it = dataFileReader.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        dataFileReader.close();
        return list;
    }


    public static <E> byte[] toBytes(E[] ees, Class<E> clazz) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DatumWriter<E> datumWriter = new SpecificDatumWriter<E>(clazz);
        DataFileWriter<E> dataFileWriter = new DataFileWriter<E>(datumWriter);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        E e = ees[0];
        Method method = e.getClass().getDeclaredMethod("getSchema");
        dataFileWriter.create((org.apache.avro.Schema) method.invoke(e), out);
        for (int i = 0; i < ees.length; i++) {
            dataFileWriter.append(ees[i]);
        }
        dataFileWriter.close();
        return out.toByteArray();
    }
}
