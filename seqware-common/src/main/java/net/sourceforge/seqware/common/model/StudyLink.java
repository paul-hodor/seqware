package net.sourceforge.seqware.common.model;

//default package
//Generated 09.12.2011 15:01:20 by Hibernate Tools 3.2.4.GA

/**
 * StudyLink generated by hbm2java
 *
 * @author boconnor
 * @version $Id: $Id
 */
public class StudyLink implements java.io.Serializable {

  private static final long serialVersionUID = 1L;
  private int studyLinkId;
  private Study study;
  private String label;
  private String url;
  private String db;
  private String id;

  /**
   * <p>Constructor for StudyLink.</p>
   */
  public StudyLink() {
  }

  /**
   * <p>Constructor for StudyLink.</p>
   *
   * @param studyLinkId a int.
   * @param study a {@link net.sourceforge.seqware.common.model.Study} object.
   * @param label a {@link java.lang.String} object.
   * @param url a {@link java.lang.String} object.
   */
  public StudyLink(int studyLinkId, Study study, String label, String url) {
    this.studyLinkId = studyLinkId;
    this.study = study;
    this.label = label;
    this.url = url;
  }

  /**
   * <p>Constructor for StudyLink.</p>
   *
   * @param studyLinkId a int.
   * @param study a {@link net.sourceforge.seqware.common.model.Study} object.
   * @param label a {@link java.lang.String} object.
   * @param url a {@link java.lang.String} object.
   * @param db a {@link java.lang.String} object.
   * @param id a {@link java.lang.String} object.
   */
  public StudyLink(int studyLinkId, Study study, String label, String url, String db, String id) {
    this.studyLinkId = studyLinkId;
    this.study = study;
    this.label = label;
    this.url = url;
    this.db = db;
    this.id = id;
  }

  /**
   * <p>Getter for the field <code>studyLinkId</code>.</p>
   *
   * @return a int.
   */
  public int getStudyLinkId() {
    return this.studyLinkId;
  }

  /**
   * <p>Setter for the field <code>studyLinkId</code>.</p>
   *
   * @param studyLinkId a int.
   */
  public void setStudyLinkId(int studyLinkId) {
    this.studyLinkId = studyLinkId;
  }

  /**
   * <p>Getter for the field <code>study</code>.</p>
   *
   * @return a {@link net.sourceforge.seqware.common.model.Study} object.
   */
  public Study getStudy() {
    return this.study;
  }

  /**
   * <p>Setter for the field <code>study</code>.</p>
   *
   * @param study a {@link net.sourceforge.seqware.common.model.Study} object.
   */
  public void setStudy(Study study) {
    this.study = study;
  }

  /**
   * <p>Getter for the field <code>label</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * <p>Setter for the field <code>label</code>.</p>
   *
   * @param label a {@link java.lang.String} object.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * <p>Getter for the field <code>url</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getUrl() {
    return this.url;
  }

  /**
   * <p>Setter for the field <code>url</code>.</p>
   *
   * @param url a {@link java.lang.String} object.
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * <p>Getter for the field <code>db</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getDb() {
    return this.db;
  }

  /**
   * <p>Setter for the field <code>db</code>.</p>
   *
   * @param db a {@link java.lang.String} object.
   */
  public void setDb(String db) {
    this.db = db;
  }

  /**
   * <p>Getter for the field <code>id</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getId() {
    return this.id;
  }

  /**
   * <p>Setter for the field <code>id</code>.</p>
   *
   * @param id a {@link java.lang.String} object.
   */
  public void setId(String id) {
    this.id = id;
  }

}
