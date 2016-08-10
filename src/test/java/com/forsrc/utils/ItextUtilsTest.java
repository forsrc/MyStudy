package com.forsrc.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.junit.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.*;

public class ItextUtilsTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        ItextUtils.crate().html2Pdf();
        URL url = new URL("https://github.com/");
        ByteOutputStream outputStream = new ByteOutputStream();
        FileOutputStream out = new FileOutputStream(new File("out/A.pdf"));
        ItextUtils.html2Pdf(url, out);
        out.flush();
        out.close();
    }



}
