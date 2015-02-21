package cs122b.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by dinhho on 2/18/15.
 */
public class DPLPDocument {


    private String document_type;

    private String title;
    private String booktitle;
    private String start_page;
    private String end_page;
    private String year;
    private String editor;
    private String publisher;
    private String school;
    private String journal;
    private String volume;
    private String number;
    private String month;
    private String url;
    private String ee;
    private String cdrom;
    private String cite;
    private String crossref;
    private String isbn;
    private String series;

    private HashMap<String, String> attributes;
    private HashSet<String> authors;


    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public void addAuthor(String a) {
        authors.add(a);
    }


    public DPLPDocument() {
        attributes = new HashMap<String, String>();
        authors = new HashSet<String>();
    }

    public String createAuthorString() {
        String s = "";
        String comma = "";
        if (authors.size() == 0)
            return s;
        for (String author : authors) {
            s += comma + author;
            comma = ",";
        }
        return s;
    }

    public Collection<String> getAuthors() {
        return this.authors;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDocumentType() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getJournal() {
        return journal;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getStart_page() {
        return start_page;
    }

    public void setStart_page(String start_page) {
        this.start_page = start_page;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getEnd_page() {
        return end_page;
    }

    public void setEnd_page(String end_page) {
        this.end_page = end_page;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEe() {
        return ee;
    }

    public void setEe(String ee) {
        this.ee = ee;
    }

    public String getCdrom() {
        return cdrom;
    }

    public void setCdrom(String cdrom) {
        this.cdrom = cdrom;
    }

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }

    public String getCrossref() {
        return crossref;
    }

    public void setCrossref(String crossref) {
        this.crossref = crossref;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }


    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(this);
    }
}
