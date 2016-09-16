package com.forsrc.utils;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

public class ItextUtils {

    private ItextUtils() {

    }

    public static ItextUtils crate() {
        return new ItextUtils();
    }

    public void html2Pdf() {

    }

    public static void html2Pdf(URL url, OutputStream outputStream) throws DocumentException, IOException {

        org.jsoup.nodes.Document doc = Jsoup.connect(url.toString())
                //.data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(3000)
                .get();

        doc.getElementsByTag("script").remove();

        Elements elements = doc.getAllElements();
        Iterator<Element> it = elements.iterator();
        while (it.hasNext()) {
            Element element = it.next();
            String href = element.attr("href");
            if (href != null && !href.startsWith("http")) {
                element.attr("href", url.toString() + href);
            }
            if ("img".equals(element.tag().getName())) {
                String src = element.attr("src");
                if (src == null || src.startsWith("http")) {
                    continue;
                }

                element.attr("src", url.toString() + src);
            }

            if ("link".equals(element.tag().getName()) && "stylesheet".equals(element.attr("rel"))) {
                href = element.attr("href");
                if (href == null || href.startsWith("http")) {
                    continue;
                }
                element.attr("href", url.toString() + element.attr("href"));
            }
        }


        String html = doc.html();
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes());

        System.out.println(html);


        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setViewerPreferences(PdfWriter.PageModeUseOC);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);

        document.open();

        document.close();

    }


    private static void parseImg(Element element, Document document, URL url) throws IOException, DocumentException {

        if (!"img".equals(element.tag().getName())) {
            return;
        }

        try {

            Image image = Image.getInstance(new URL(url.toString() + element.attr("src")));
            float height = image.getHeight();
            float width = image.getWidth();
            //image.setAlignment(Image.MIDDLE);
            System.out.println(url.toString() + element.attr("src") + " --> " + width + ", " + height);

            document.add(image);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


    }

    private static void parseCss(Element element, URL url) throws IOException, DocumentException {

        String css = getCssText(element, url);
        if (css == null) {
            return;
        }
        System.out.println(css);

    }

    private static String getCssText(Element element, URL url) throws IOException, DocumentException {

        if (!"link".equals(element.tag().getName())) {
            return null;
        }

        if (!"stylesheet".equals(element.attr("rel"))) {
            return null;
        }

        String href = element.attr("href");
        href = href.startsWith("http") ? href : url.toString() + element.attr("href");


        System.out.println(href);

        return Jsoup.connect(href)
                .timeout(1000)
                .get().body().html();

    }
}
