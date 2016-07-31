package com.forsrc.springmvc.restful.plantuml.controller;

import com.forsrc.base.dao.BaseHibernateDao;
import com.forsrc.exception.ActionException;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.api.PlantumlUtils;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.servlet.UmlExtractor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.IIOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/plantuml")
public class PlantUmlController {

    /**
     * The constant LOGGER.
     */
    public static final Logger LOGGER = Logger.getLogger(BaseHibernateDao.class.getName());

    private static final Pattern URL_PATTERN = Pattern.compile(".*/(.*)"); // Last part of the URL
    private static final Pattern ENCODED_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-\\_]+$"); // Format of a compressed
    // diagram
    private static final Pattern START_PATTERN = Pattern.compile("/\\w+/start/(.*)");

    private String TEXT_NULL = "Form data 'text' is null.";

    @RequestMapping(value = {"/welcome"}, method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("decoded", "Bob -> Alice : hello");
        request.setAttribute("encoded", "SyfFKj2rKt3CoKnELR1Io4ZDoSa70000");
        // forward to index.jsp
        //RequestDispatcher dispatcher = request.getRequestDispatcher("/plantuml/index.jsp");
        //dispatcher.forward(request, response);
        return new ModelAndView("from");
    }

    @RequestMapping(value = {"/form"}, method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        handleForm(request, response);

        return new ModelAndView("from");
    }

    @RequestMapping(value = {"/img"}, method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public void img(HttpServletRequest request
            , HttpServletResponse response
            , @RequestParam("text") String text
            , @RequestParam ("format") String fileFormat) throws IOException, ServletException, ActionException {

        FileFormat ff = FileFormat.PNG;
        if (fileFormat != null) {
            try {
                ff = FileFormat.valueOf(fileFormat.trim().toUpperCase());
            } catch (Exception e) {
                ;
            }
        }
        handle(request, response, text, ff);
    }

    @RequestMapping(value = {"/txt"}, method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public void txt(HttpServletRequest request
            , HttpServletResponse response
            , @RequestParam ("text") String text) throws IOException, ServletException, ActionException {
        handle(request, response, text, FileFormat.UTXT);
    }

    public String check() {
        return "check";
    }

    public String map() throws IOException, ServletException {
        return "map";
    }

    @RequestMapping(value = {"/start"}, method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public void start(HttpServletRequest request
            , HttpServletResponse response
            , @RequestParam ("text") String text
            , @RequestParam ("format") String format) throws ActionException {

        try {
            handleImage(response, text, format);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public String proxy() {
        return "proxy";
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, String text, FileFormat fileFormat) throws IOException, ServletException {

        // build the UML source from the compressed request parameter
        String uml = UmlExtractor.getUmlSource(text);

        // generate the response
        DiagramResponse dr = new DiagramResponse(response, fileFormat);
        try {
            dr.sendDiagram(uml);
        } catch (IIOException e) {
            // Browser has closed the connection, so the HTTP OutputStream is closed
            // Silently catch the exception to avoid annoying log
            LOGGER.error(e.getMessage(), e);
        }
        dr = null;
    }

    private void handle(String text, HttpServletResponse response, FileFormat fileFormat) throws IOException, ServletException {

        // build the UML source from the compressed request parameter
        String uml = UmlExtractor.getUmlSource(text);

        // generate the response
        DiagramResponse dr = new DiagramResponse(response, fileFormat);
        try {
            dr.sendDiagram(uml);
        } catch (IIOException e) {
            // Browser has closed the connection, so the HTTP OutputStream is closed
            // Silently catch the exception to avoid annoying log
            LOGGER.error(e.getMessage(), e);
        }
        dr = null;
    }

    protected void handleForm(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        request.setCharacterEncoding("UTF-8");
        String text = request.getParameter("text");
        String url = request.getParameter("url");
        String encoded = "";

        Transcoder transcoder = getTranscoder();
        // the URL form has been submitted
        if (url != null && !url.trim().isEmpty()) {
            // Catch the last part of the URL if necessary
            Matcher m1 = URL_PATTERN.matcher(url);
            if (m1.find()) {
                url = m1.group(1);
            }
            // Check it's a valid compressed text
            Matcher m2 = ENCODED_PATTERN.matcher(url);
            if (m2.find()) {
                url = m2.group(0);
                text = transcoder.decode(url);
            } else {
                System.out.println("PlantUML ERROR Not a valid compressed string : " + url);
            }
        }
        // the Text form has been submitted
        if (text != null && !text.trim().isEmpty()) {
            encoded = transcoder.encode(text);
        }

        request.setAttribute("decoded", text);
        request.setAttribute("encoded", encoded);

        // check if an image map is necessary
        if (text != null && PlantumlUtils.hasCMapData(text)) {
            request.setAttribute("mapneeded", Boolean.TRUE);
        }

        // forward to index.jsp
        //RequestDispatcher dispatcher = request.getRequestDispatcher("/plantuml/index.jsp");
        //dispatcher.forward(request, response);
        return;
    }

    private Transcoder getTranscoder() {
        return TranscoderUtil.getDefaultTranscoder();
    }

    // This method will be removed in a near future, please don't use it.
    private void handleImage(HttpServletResponse response, String source, String format) throws IOException {
        source = URLDecoder.decode(source, "UTF-8");
        if (source.trim().startsWith("@startuml") && source.trim().endsWith("@enduml")) {
            String text = source.replace("\\n", "\n");
            text = text.replace("\\t", "\t");
            sendImage(response, text, format);
            return;
        }
        StringBuilder plantUmlSource = new StringBuilder();

        StringTokenizer tokenizer = new StringTokenizer(source, "/@");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            plantUmlSource.append(token).append("\n");
        }
        sendImage(response, plantUmlSource.toString(), format);

    }

    // This method will be removed in a near future, please don't use it.
    private void sendImage(HttpServletResponse response, String text, String format) throws IOException {
        String uml = "";
        if (text.startsWith("@startuml")) {
            uml = text;
        } else {
            StringBuilder plantUmlSource = new StringBuilder();
            plantUmlSource.append("@startuml\n");
            plantUmlSource.append(text);
            if (text.endsWith("\n") == false) {
                plantUmlSource.append("\n");
            }
            plantUmlSource.append("@enduml");
            uml = plantUmlSource.toString();
        }
        // Write the first image to "os"
        long today = System.currentTimeMillis();
        if (StringUtils.isDiagramCacheable(uml)) {
            // Add http headers to force the browser to cache the image
            response.addDateHeader("Expires", today + 31536000000L);
            // today + 1 year
            response.addDateHeader("Last-Modified", 1261440000000L);
            // 2009 dec 22 constant date in the past
            response.addHeader("Cache-Control", "public");
        }
        //response.setContentType("image/png");
        //SourceStringReader reader = new SourceStringReader(uml);
        FileFormat fileFormat = FileFormat.PNG;
        if (format != null) {
            try {
                fileFormat = FileFormat.valueOf(format.trim().toUpperCase());
            } catch (Exception e) {
                fileFormat = FileFormat.PNG;
            }
        }
        //reader.generateImage(response.getOutputStream(), new FileFormatOption(fileFormat, false));
        DiagramResponse dr = new DiagramResponse(response, fileFormat);
        try {
            dr.sendDiagram(uml);
        } catch (IIOException e) {
            // Browser has closed the connection, so the HTTP OutputStream is closed
            // Silently catch the exception to avoid annoying log
            LOGGER.error(e.getMessage(), e);
        }
        dr = null;

        //request.setAttribute("decoded", uml);
        //String encoded = getTranscoder().encode(uml.replace("@startuml", "").replace("@enduml", ""));
        //request.setAttribute("encoded", encoded);

    }

}