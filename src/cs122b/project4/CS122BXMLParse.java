package cs122b.project4;

import javax.xml.parsers.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import javafx.util.Pair;
import org.w3c.dom.*;
import cs122b.models.*;
import cs122b.utils.*;

/**
 * Created by dinhho on 2/18/15.
 */
public class CS122BXMLParse {

    private ArrayList<DPLPDocument> documents = new ArrayList<DPLPDocument>();

    public Collection<DPLPDocument> getParsedDocument() {
        return this.documents;
    }

    public void parseFile(File file) throws Exception {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        if (document.hasChildNodes()) {
            NodeList dlpNode = document.getChildNodes();
            NodeList elementNodes = dlpNode.item(1).getChildNodes();
            beginParsing(elementNodes);
        }
    }

    private void beginParsing(NodeList nodeList) {
        for (int i = 1; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    DPLPDocument document = parseDocument(node);
                    documents.add(document);
                } catch (UnsupportedEncodingException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public DPLPDocument parseDocument(Node node) throws UnsupportedEncodingException
    {
        DPLPDocument doc = new DPLPDocument();
        doc.setDocument_type(node.getNodeName());
        // check if it has any attributes and parse those into an attribute map
        if (node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                doc.addAttribute(attribute.getNodeName(), attribute.getNodeValue());
            }
        }
        if (node.hasChildNodes()) {
            NodeList elementNode = node.getChildNodes();
            for (int i = 0; i < elementNode.getLength(); i++) {
                Node n = elementNode.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    String tempString = n.getTextContent().trim();
//                    byte[] b = s.getBytes(Charset.forName("ISO-8859-1"));
//                    String tempString = new String(b, "UTF-8");
//                    System.out.println("\tELEMENT =  " + n.getNodeName() + ": " + tempString);
//                    if (b == null) {
//                        System.out.println("ERROR GETTING BYTES");
//                    }
                    if (n.getNodeName().equals("title")) {
                        doc.setTitle(tempString);
                    }
                    else if (n.getNodeName().equals("booktitle")) {
                        doc.setBooktitle(tempString);
                    }
                    else if (n.getNodeName().equals("pages")) {
                        String p = tempString;
                        Pair<String, String> pages = parsePair(p);
                        doc.setStart_page(pages.getKey());
                        doc.setEnd_page(pages.getValue());
                    }
                    else if (n.getNodeName().equals("year")) {
                        doc.setYear(tempString);
                    }
                    else if (n.getNodeName().equals("volume")) {
                        doc.setVolume(tempString);
                    }
                    else if (n.getNodeName().equals("number")) {
                        doc.setNumber(tempString);
                    }
                    else if (n.getNodeName().equals("url")) {
                        doc.setUrl(tempString);
                    }
                    else if (n.getNodeName().equals("ee")) {
                        doc.setEe(tempString);
                    }
                    else if (n.getNodeName().equals("cdrom")) {
                        doc.setCdrom(tempString);
                    }
                    else if (n.getNodeName().equals("cite")) {
                        doc.setCite(tempString);
                    }
                    else if (n.getNodeName().equals("crossref")) {
                        doc.setCrossref(tempString);
                    }
                    else if (n.getNodeName().equals("isbn")) {
                        doc.setIsbn(tempString);
                    }
                    else if (n.getNodeName().equals("series")) {
                        doc.setSeries(tempString);
                    }
                    else if (n.getNodeName().equals("editor")) {
                        String e = tempString;
                        doc.setEditor(e);
                    }
                    else if (n.getNodeName().equals("publisher")) {
                        String p = tempString;
                        doc.setPublisher(p);
                    }
                    else if (n.getNodeName().equals("school")) {
                        doc.setSchool(tempString);
                    }
                    else if (n.getNodeName().equals("journal")) {
                        doc.setJournal(tempString);
                    }
                    else if (n.getNodeName().equals("month")) {
                        doc.setMonth(tempString);
                    }
                    else if (n.getNodeName().equals("author")) {
                        doc.addAuthor(n.getTextContent().trim());
                    }

                }
            }
        }
        return doc;
    }

    public Pair<String, String> parsePair(String p) {
        String [] s = p.split("-");
        if (s.length == 1) {
            System.out.println("Error parsing Pages, value = " + p);
            return new Pair<String, String>(s[0], null);
        }
        else if (s.length ==0) {
            System.out.println("Error parsing Pages, value = " + p);
            return new Pair<String, String>(null, null);
        }
        return new Pair<String, String>(s[0],s[1]);
    }


    public void addDocuments(Collection<DPLPDocument> documents){
        long startTime = System.currentTimeMillis();
        long stopTime;
        CallableStatement cStatement = null;
        Connection con = null;
        String sql = "{call AddDocument(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            con = ConnectionManager.getSingleConnection();
            if (con == null) {
                System.out.println("Cannot get JDBC Connection. Operation terminating");
                return;
            }
            cStatement = con.prepareCall(sql);
            for (DPLPDocument doc: documents) {
                if (StringUtil.isNullOrEmpty(doc.getTitle())) {
                    cStatement.setNull(1, Types.NULL);
                } else {
                    cStatement.setString(1, doc.getTitle());
                }

                if (StringUtil.isNullOrEmpty(doc.getStart_page()) || StringUtil.isStringIntParsable(doc.getStart_page()) == -1) {
                    cStatement.setNull(2, Types.NULL);
                } else {
                    cStatement.setInt(2, StringUtil.isStringIntParsable(doc.getStart_page()));
                }

                if (StringUtil.isNullOrEmpty(doc.getEnd_page()) || StringUtil.isStringIntParsable(doc.getEnd_page()) == -1) {
                    cStatement.setNull(3, Types.NULL);
                } else {
                    cStatement.setInt(3, StringUtil.isStringIntParsable(doc.getEnd_page()));
                }

                if (StringUtil.isNullOrEmpty(doc.getYear()) || StringUtil.isStringIntParsable(doc.getYear()) == -1) {
                    cStatement.setNull(4, Types.NULL);
                } else {
                    cStatement.setInt(4, StringUtil.isStringIntParsable(doc.getYear()));
                }

                if (StringUtil.isNullOrEmpty(doc.getVolume()) || StringUtil.isStringIntParsable(doc.getVolume()) == -1) {
                    cStatement.setNull(5, Types.NULL);
                } else {
                    cStatement.setInt(5, StringUtil.isStringIntParsable(doc.getVolume()));
                }

                if (StringUtil.isNullOrEmpty(doc.getNumber()) || StringUtil.isStringIntParsable(doc.getNumber()) == -1) {
                    cStatement.setNull(6, Types.NULL);
                } else {
                    cStatement.setInt(6, StringUtil.isStringIntParsable(doc.getNumber()));
                }

                if (StringUtil.isNullOrEmpty(doc.getUrl())) {
                    cStatement.setNull(7, Types.NULL);
                } else {
                    cStatement.setString(7, doc.getUrl());
                }

                if (StringUtil.isNullOrEmpty(doc.getEe())) {
                    cStatement.setNull(8, Types.NULL);
                } else {
                    cStatement.setString(8, doc.getEe());
                }

                if (StringUtil.isNullOrEmpty(doc.getCdrom())) {
                    cStatement.setNull(9, Types.NULL);
                } else {
                    cStatement.setString(9, doc.getCdrom());
                }

                if (StringUtil.isNullOrEmpty(doc.getCite())) {
                    cStatement.setNull(10, Types.NULL);
                } else {
                    cStatement.setString(10, doc.getCite());
                }

                if (StringUtil.isNullOrEmpty(doc.getCrossref())) {
                    cStatement.setNull(11, Types.NULL);
                } else {
                    cStatement.setString(11, doc.getCrossref());
                }

                if (StringUtil.isNullOrEmpty(doc.getIsbn())) {
                    cStatement.setNull(12, Types.NULL);
                } else {
                    cStatement.setString(12, doc.getIsbn());
                }

                if (StringUtil.isNullOrEmpty(doc.getSeries())) {
                    cStatement.setNull(13, Types.NULL);
                } else {
                    cStatement.setString(13, doc.getSeries());
                }

                if (StringUtil.isNullOrEmpty(doc.getEditor())) {
                    cStatement.setNull(14, Types.NULL);
                } else {
                    cStatement.setString(14, doc.getEditor());
                }

                if (StringUtil.isNullOrEmpty(doc.getPublisher())) {
                    cStatement.setNull(15, Types.NULL);
                } else {
                    cStatement.setString(15, doc.getPublisher());
                }

                if (StringUtil.isNullOrEmpty(doc.getBooktitle())) {
                    cStatement.setNull(16, Types.NULL);
                } else {
                    cStatement.setString(16, doc.getBooktitle());
                }

                if (StringUtil.isNullOrEmpty(doc.getDocumentType())) {
                    cStatement.setNull(17, Types.NULL);
                } else {
                    cStatement.setString(17, doc.getDocumentType());
                }

                if (doc.getAuthors().size() == 0) {
                    cStatement.setNull(18, Types.NULL);
                } else {
                    cStatement.setString(18, doc.createAuthorString());
                }
                cStatement.addBatch();
            }
            System.out.println("EXECUTING remaining Batch Update");
            int [] updateCounts = cStatement.executeBatch();
            System.out.println("FINISH UPDATING BATCH");
            stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            System.out.println("Finished inserting in :" + TimeUnit.MILLISECONDS.toSeconds(runTime) + "seconds");
        } catch (Exception e) {
            stopTime = System.currentTimeMillis();
            long runTime = stopTime - startTime;
            System.out.println("Finished inserting in :" + TimeUnit.MILLISECONDS.toSeconds(runTime) + "seconds");
            System.out.println("Some entries had errors in insertion");
            System.out.println(e.getMessage());
        } finally {
            try {
                cStatement.close();
                con.close();
            } catch (SQLException closeEx) {
                System.out.println(closeEx.getMessage());
            }
        }
    }


}
