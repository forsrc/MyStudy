package com.forsrc.utils;

import org.junit.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.*;

public class ImageUtilsTest {
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
    public void test(){
        String number = ImageUtils.getNumber();
        //System.out.println(number);
        assertEquals(4, number.length());
        String code = ImageUtils.getCode(10);
        assertEquals(10, code.length());

        BufferedImage bufferedImage = ImageUtils.getBufferedImage(code);
        assertNotNull(bufferedImage);

        Map<String, BufferedImage> map = ImageUtils.getImage();

        assertEquals(1, map.size());
        assertEquals(4, map.keySet().iterator().next().length());
        assertNotNull(map.entrySet().iterator().next().getValue());
        ByteArrayInputStream in = null;
        try {
            in = ImageUtils.getInputStream(bufferedImage);
            assertTrue(in.available() > 0);
        } catch (IOException e) {
            fail();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    fail();
                }
            }
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInputStreamNullPointerException(){
        try {
            ImageUtils.getInputStream(null);
            fail();
        } catch (IOException e) {

        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInputStreamIllegalArgumentException(){
        try {
            ImageUtils.getInputStream(new BufferedImage(0, 0, 0));
            fail();
        } catch (IOException e) {

        }
    }


}
