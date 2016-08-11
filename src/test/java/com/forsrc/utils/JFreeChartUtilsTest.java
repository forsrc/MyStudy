package com.forsrc.utils;


import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class JFreeChartUtilsTest {

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
        DefaultPieDataset dpd = new DefaultPieDataset();
        dpd.setValue("DefaultPieDataset-1", 25);
        dpd.setValue("DefaultPieDataset-2", 25);
        dpd.setValue("DefaultPieDataset-3", 45);
        dpd.setValue("DefaultPieDataset-4", 10);

        JFreeChart chart = ChartFactory.createPieChart("DefaultPieDataset", dpd, true, true, false);

        /*ChartFrame chartFrame = new ChartFrame("text", chart);
        chartFrame.pack();
        chartFrame.setVisible(true);*/

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ChartRenderingInfo info = new ChartRenderingInfo(
                new StandardEntityCollection());
        ChartUtilities.writeChartAsJPEG(byteArrayOutputStream, chart, 800, 400, info);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        FileOutputStream fileOutputStream = new FileOutputStream(new File("out/A.jpg"));
        IOUtils.copy(byteArrayInputStream, fileOutputStream);
        fileOutputStream.close();

    }
}
