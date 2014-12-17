/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.solr_web_application;

/**
 *
 * @author Imran Bijapuri
 */
public class ResponseWrapper {
    private String response;
    private String highlighting;
    private String responseHeader;

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the highlighting
     */
    public String getHighlighting() {
        return highlighting;
    }

    /**
     * @param highlighting the highlighting to set
     */
    public void setHighlighting(String highlighting) {
        this.highlighting = highlighting;
    }

    /**
     * @return the responseHeader
     */
    public String getResponseHeader() {
        return responseHeader;
    }

    /**
     * @param responseHeader the responseHeader to set
     */
    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }
}
