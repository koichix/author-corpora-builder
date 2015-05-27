/*
 * Copyright 2015 Ján Švec
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package sk.svec.jan.acb.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sk.svec.jan.acb.main.Main;
import sk.svec.jan.acb.utility.Setting;

/**
 *
 * @author Ján Švec
 */
@WebServlet(
        name = "ActionServlet",
        urlPatterns = {
            ActionServlet.ACTION_PREVIEW,
            ActionServlet.ACTION_OUTPUT,
            ActionServlet.ACTION_SETTINGS,
            ActionServlet.ACTION_GETFILE,
            ActionServlet.ACTION_HELP,
            ActionServlet.ACTION_AJAX,
            ActionServlet.ACTION_INPUT})
public class ActionServlet extends HttpServlet {

    static final String ACTION_INPUT = "/input";
    static final String ACTION_OUTPUT = "/output";
    static final String ACTION_SETTINGS = "/settings";
    static final String ACTION_GETFILE = "/getfile";
    static final String ACTION_PREVIEW = "/preview";
    static final String ACTION_HELP = "/help";
    static final String ACTION_AJAX = "/ajax";

    static final String ATTRIBUTE_OUTPUTS = "outputs";
    static final String ATTRIBUTE_SETTINGS_FORM = "settingsForm";
    static final String ATTRIBUTE_INPUT_FORM = "inputForm";
    static final String ATTRIBUTE_ERROR = "error";

    static final String JSP_INPUT = "/WEB-INF/jsp/input.jsp";
    static final String JSP_SETTINGS = "/WEB-INF/jsp/settings.jsp";
    static final String JSP_OUTPUT = "/WEB-INF/jsp/output.jsp";
    static final String JSP_PREVIEW = "/WEB-INF/jsp/preview.jsp";
    static final String JSP_HELP = "/WEB-INF/jsp/help.jsp";
    static final String JSP_AJAX = "/WEB-INF/jsp/ajax.jsp";

    private Main main = new Main();

    public void ajax(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        for (int i = 0; i < 10; i++) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(500);
////                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                //Handle exception
//            }
//            //System.out.println(i);
//            PrintWriter writer = response.getWriter();
//        
//            writer.print(i);
//            
//          writer.flush();
//        }

        request.getRequestDispatcher(JSP_AJAX).forward(request, response);
    }

    private void help(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//                request.setAttribute("xml", "text");
        request.getRequestDispatcher(JSP_HELP).forward(request, response);
    }

    private void preview(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // reads input file from an absolute path
        String fileName = request.getParameter("name");
        String filePath = fileName;

        //send parameter "xml" to jsp
        request.setAttribute("xml", main.viewXML(filePath));
        try {
            request.setAttribute("corpusFiles", main.getCorpusFiles());
        } catch (Exception ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher(JSP_PREVIEW).forward(request, response);
    }

    private void output(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("info", main.getOutputInfo());
        request.setAttribute("zip", main.getZipPath());
        try {
            request.setAttribute(ATTRIBUTE_OUTPUTS, main.getOutput());
        } catch (Exception ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher(JSP_OUTPUT).forward(request, response);
    }

    private void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // reads input file from an absolute path
        String fileName = request.getParameter("name");
        String filePath = fileName;
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);

        // if you want to use a relative path to context root:
        String relativePath = getServletContext().getRealPath("");
        System.out.println("relativePath = " + relativePath);

        // obtains ServletContext
        ServletContext context = getServletContext();

        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inStream.close();
        outStream.close();

    }

    private void input(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getMethod().equals("POST")) {

            if (request.getParameter("cancel") != null) {
                response.sendRedirect(request.getContextPath());
                return;
            }

            InputForm inputForm = InputForm.extractFromRequest(request);

            StringBuilder errors = new StringBuilder();
            List<String> seeds = inputForm.validateAndToSeeds(errors);
            boolean disc = inputForm.isDisc();
//  rozdelene stiahni a analyzuj
//            if (request.getParameter("analyze") != null) {
//                try {
//
////                    webDetectionMain.start(disc);
//                    main.analyze(disc);
//                } catch (Exception ex) {
//                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                response.sendRedirect(request.getContextPath() + "/output");
//
//            }
//            if (request.getParameter("submit") != null) {
//                if (seeds == null) {
//                    request.setAttribute(ATTRIBUTE_ERROR, errors.toString());
//                    request.setAttribute(ATTRIBUTE_INPUT_FORM, inputForm);
//                    request.getRequestDispatcher(JSP_INPUT).forward(request, response);
//                } else {
//                    main.setSeeds(seeds);
//                    try {
////                    webDetectionMain.start(disc);
//                        main.crawl(disc);
//                    } catch (Exception ex) {
//                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    request.setAttribute("downloaded", "Stránky boli stiahnuté.");
//                    request.setAttribute(ATTRIBUTE_INPUT_FORM, inputForm);
//                    request.getRequestDispatcher(JSP_INPUT).forward(request, response);
////                    response.sendRedirect(request.getContextPath() + "/output");
//
//                }
//            }
            if (request.getParameter("run") != null) {
                if (seeds == null) {
                    request.setAttribute(ATTRIBUTE_ERROR, errors.toString());
                    request.setAttribute(ATTRIBUTE_INPUT_FORM, inputForm);
                    request.getRequestDispatcher(JSP_INPUT).forward(request, response);
                } else {
                    main.setSeeds(seeds);
                    try {
                        main.clean();
                        main.start(disc);
                    } catch (Exception ex) {
                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    response.sendRedirect(request.getContextPath() + "/output");
                    response.sendRedirect("output");
//                    request.setAttribute("downloaded", "Stránky boli stiahnuté a analyzované.");
//                    request.setAttribute(ATTRIBUTE_INPUT_FORM, inputForm);
//                    request.getRequestDispatcher(JSP_INPUT).forward(request, response);

                }
            }
        } else {
            request.setAttribute(ATTRIBUTE_INPUT_FORM, new InputForm());
            request.getRequestDispatcher(JSP_INPUT).forward(request, response);
        }
    }

    private void settings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getMethod().equals("POST")) {

            if (request.getParameter("cancel") != null) {
                response.sendRedirect(request.getContextPath());
                return;
            }

            SettingsForm settingsForm = SettingsForm.extractFromRequest(request);

            StringBuilder errors = new StringBuilder();
            Setting settings = settingsForm.validateAndToSettings(errors);

            if (settings == null) {
                request.setAttribute(ATTRIBUTE_ERROR, errors.toString());
                request.setAttribute(ATTRIBUTE_SETTINGS_FORM, settingsForm);
                request.getRequestDispatcher(JSP_SETTINGS).forward(request, response);
            } else {
                boolean urlOn = settingsForm.isUrlOn();
                if (urlOn) {
                    settings.setSetting4("true");
                } else {
                    settings.setSetting4("false");
                }
                main.setSettings(settings);
                response.sendRedirect(request.getContextPath());
            }

        } else {
            request.setAttribute(ATTRIBUTE_SETTINGS_FORM, main.getSettings());
//            request.setAttribute(ATTRIBUTE_SETTINGS_FORM, new SettingsForm());
            request.getRequestDispatcher(JSP_SETTINGS).forward(request, response);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getServletPath().equals(ACTION_OUTPUT)) {
            output(request, response);
        } else if (request.getServletPath().equals(ACTION_INPUT)) {
            input(request, response);
        } else if (request.getServletPath().equals(ACTION_SETTINGS)) {
            settings(request, response);
        } else if (request.getServletPath().equals(ACTION_GETFILE)) {
            getFile(request, response);
        } else if (request.getServletPath().equals(ACTION_PREVIEW)) {
            preview(request, response);
        } else if (request.getServletPath().equals(ACTION_HELP)) {
            help(request, response);
        } else if (request.getServletPath().equals(ACTION_AJAX)) {
            ajax(request, response);
        } else {
            throw new RuntimeException("Unknown operation: " + request.getServletPath());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
