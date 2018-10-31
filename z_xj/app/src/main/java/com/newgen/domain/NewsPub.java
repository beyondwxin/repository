package com.newgen.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsPub implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3251396714677829256L;

	private int id;

	private Integer categoryid;

	private String title;
	private String shorttitle;
	private String author;

	private String digest;

	private String body;

	private String htmlbody;
	private Integer editorid;

	private String editor;

	private String source;

	private String createdatetime;

	private String publishtime;
	private String publishTime;

	private String createtime;
	private String createTime;
	
	private String score;
	
	
	private int liveId;
	
	private boolean isStop = true;
	
	public int getLiveId() {
		return liveId;
	}

	public void setLiveId(int liveId) {
		this.liveId = liveId;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	private Integer wordcount;

	private String topTime;

	private String pubmanid;

	private String pubdatetime;
	private Integer stype;
	private Integer isEpaper;
	
	public Integer getIsEpaper() {
		return isEpaper;
	}

	public void setIsEpaper(Integer isEpaper) {
		this.isEpaper = isEpaper;
	}

	private Integer commentcount;
	private List<Comment> comments;
	private int flag;

	private String html;
	
	

	private NewsPubExt newsPubExt = new NewsPubExt();

	private List<NewsReview> listreview = new ArrayList<NewsReview>();

	private List<NewsFile> listpic = new ArrayList<NewsFile>();

	private List<NewsFile> listvedio = new ArrayList<NewsFile>();

	private List<NewsFile> listaudio = new ArrayList<NewsFile>();

	private int reviewcount;

	private List<RunImage> listrunimage = new ArrayList<RunImage>();
	
	private List<NewsVote> listvote = new ArrayList<NewsVote>();
	
	private List<Leader> listLeader = new ArrayList<Leader>();
	
	private List<NewsFile> liveFiles = new ArrayList<NewsFile>();
	public List<NewsFile> getLiveFiles() {
		return liveFiles;
	}

	public void setLiveFiles(List<NewsFile> liveFiles) {
		this.liveFiles = liveFiles;
	}

	private int livestate;
	
	private LiveHost liveHost;
	private liveFile liveFile;
	

	public liveFile getLiveFile() {
		return liveFile;
	}

	public void setLiveFile(liveFile liveFile) {
		this.liveFile = liveFile;
	}

	public LiveHost getLiveHost() {
		return liveHost;
	}

	public void setLiveHost(LiveHost liveHost) {
		this.liveHost = liveHost;
	}

	public int getLivestate() {
		return livestate;
	}

	public void setLivestate(int livestate) {
		this.livestate = livestate;
	}

	public List<Leader> getListLeader() {
		return listLeader;
	}

	public void setListLeader(List<Leader> listLeader) {
		this.listLeader = listLeader;
	}

	public List<NewsVote> getListvote() {
		return listvote;
	}

	public void setListvote(List<NewsVote> listvote) {
		this.listvote = listvote;
	}

	public List<RunImage> getListrunimage() {
		return listrunimage;
	}

	public void setListrunimage(List<RunImage> listrunimage) {
		this.listrunimage = listrunimage;
	}

	public List<NewsFile> getListpic() {
		return listpic;
	}

	public void setListpic(List<NewsFile> listpic) {
		this.listpic = listpic;
	}

	public List<NewsFile> getListvedio() {
		return listvedio;
	}

	public void setListvedio(List<NewsFile> listvedio) {
		this.listvedio = listvedio;
	}

	public List<NewsFile> getListaudio() {
		return listaudio;
	}

	public void setListaudio(List<NewsFile> listaudio) {
		this.listaudio = listaudio;
	}

	public String getShorttitle() {
		return shorttitle;
	}

	public void setShorttitle(String shorttitle) {
		this.shorttitle = shorttitle;
	}

	public String getHtmlbody() {
		return htmlbody;
	}

	public void setHtmlbody(String htmlbody) {
		this.htmlbody = htmlbody;
	}

	public Integer getEditorid() {
		return editorid;
	}

	public void setEditorid(Integer editorid) {
		this.editorid = editorid;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(String publishtime) {
		this.publishtime = publishtime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getWordcount() {
		return wordcount;
	}

	public void setWordcount(Integer wordcount) {
		this.wordcount = wordcount;
	}

	public String getTopTime() {
		return topTime;
	}

	public void setTopTime(String topTime) {
		this.topTime = topTime;
	}

	public NewsPubExt getNewsPubExt() {
		return newsPubExt;
	}

	public void setNewsPubExt(NewsPubExt newsPubExt) {
		this.newsPubExt = newsPubExt;
	}

	public List<NewsReview> getListreview() {
		return listreview;
	}

	public void setListreview(List<NewsReview> listreview) {
		this.listreview = listreview;
	}

	public int getReviewcount() {
		return reviewcount;
	}

	public void setReviewcount(int reviewcount) {
		this.reviewcount = reviewcount;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getPubmanid() {
		return pubmanid;
	}

	public void setPubmanid(String pubmanid) {
		this.pubmanid = pubmanid;
	}

	public String getPubdatetime() {
		return pubdatetime;
	}

	public void setPubdatetime(String pubdatetime) {
		this.pubdatetime = pubdatetime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(Integer categoryid) {
		this.categoryid = categoryid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}

	public Integer getStype() {
		return stype;
	}

	public void setStype(Integer stype) {
		this.stype = stype;
	}

	public Integer getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(Integer commentcount) {
		this.commentcount = commentcount;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public boolean getStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
