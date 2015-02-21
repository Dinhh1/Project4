package cs122b.utils;

import cs122b.models.DPLPDocument;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Created by dinhho on 2/20/15.
 */
public class ThreadUtil implements  Runnable{
    private ArrayList<DPLPDocument> documents;
    public ThreadUtil(ArrayList<DPLPDocument> documents) {
        this.documents = documents;
    }

    @Override
    public void run() {
        CallableStatement cStatement = null;
        Connection con = null;
        String sql = "{call AddDocument(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            con = ConnectionManager.getConnection();
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
        } catch (SQLException e) {
            System.out.println("ERROR IN UPDATING");
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
