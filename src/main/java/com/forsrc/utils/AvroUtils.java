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

/**
 * The type Avro utils.
 *
 * @param <E> the type parameter
 */
public final class AvroUtils<E> {
    private AvroUtils() {

    }


    /**
     * From bytes list.
     *
     * @param <E>   the type parameter
     * @param bytes the bytes
     * @param e     the e
     * @return the list
     * @throws IOException the io exception
     */
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


    /**
     * To bytes byte [ ].
     *
     * @param <E>   the type parameter
     * @param ees   the ees
     * @param clazz the clazz
     * @return the byte [ ]
     * @throws IOException               the io exception
     * @throws NoSuchMethodException     the no such method exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     */
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
