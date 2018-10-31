package com.newgen.domain;

public class Member {

	private Integer id;
	
	private Integer parentid;
	
	private Integer isparent;
	
	private String memcode;
	
	private String nickname;
	
	private String password;
	
	private String userword;
	
	private String position;
	
	private String phonenumber;
	
	private String  lastlogintime;
	
	private String lastapphardid;
	
	private String userpic;
	
	private Integer flag;
	
	

	private Integer level;
	
	private Integer roleid;
	private String organization;
	private Integer Integration;
	
	private String email;
	
	private int state;

	private String createtime;


	private String card;
	
	private String photo;
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRoleid() {
		return roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getIsparent() {
		return isparent;
	}

	public void setIsparent(Integer isparent) {
		this.isparent = isparent;
	}

	public String getMemcode() {
		return memcode;
	}

	public void setMemcode(String memcode) {
		this.memcode = memcode;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserword() {
		return userword;
	}

	public void setUserword(String userword) {
		this.userword = userword;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}


	public String getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public String getLastapphardid() {
		return lastapphardid;
	}

	public void setLastapphardid(String lastapphardid) {
		this.lastapphardid = lastapphardid;
	}

	public String getUserpic() {
		return userpic;
	}

	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getIntegration() {
		return Integration;
	}

	public void setIntegration(Integer integration) {
		Integration = integration;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
