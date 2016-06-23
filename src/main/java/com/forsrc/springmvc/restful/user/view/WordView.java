package com.forsrc.springmvc.restful.user.view;


import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public class WordView extends AbstractView {


    Configuration configuration;

    public WordView() {
        this.setContentType("application/msword");
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");

    }


    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.addHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode("hello world.doc", "UTF-8"));
        //httpServletResponse.setContentLength(istream.available());
        POIFSFileSystem poifs = new POIFSFileSystem();

        //configuration.setClassForTemplateLoading(this.getClass(), "../ftl");
        System.out.println(this.getClass().getResource("/").getFile());
        configuration.setDirectoryForTemplateLoading(new File(this.getClass().getResource("/").getFile() + "../ftl"));
        Template template = null;
        try {

            template = configuration.getTemplate("word.html");

        } catch (IOException e) {
            e.printStackTrace();
        }


        Writer out = null;
        FileOutputStream fos = null;
        try {
            out = httpServletResponse.getWriter();
            template.process(map, out);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }


        //DirectoryEntry directory = poifs.getRoot();
        //directory.createDocument("WordDocument", this.istream);
        //poifs.writeFilesystem(httpServletResponse.getOutputStream());
        //httpServletResponse.getOutputStream().close();
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType(this.getContentType());

    }

}
