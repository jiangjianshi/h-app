package com.huifenqi.hzf_platform.context.entity.phone;

public class CallReport {
	
	private String sub_id;
	private String call_id;
	private String phone_no;
	private String secret_no;
	private String peer_no;
	private String call_type;
	private String call_time;
	private String ring_time;
	private String start_time;
	private String release_time;
	private String release_dir;
	private String out_id;
	
	
	
	public String getSub_id() {
		return sub_id;
	}
	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}
	public String getCall_id() {
		return call_id;
	}
	public void setCall_id(String call_id) {
		this.call_id = call_id;
	}
	public String getPhone_no() {
		return phone_no;
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	public String getSecret_no() {
		return secret_no;
	}
	public void setSecret_no(String secret_no) {
		this.secret_no = secret_no;
	}
	public String getPeer_no() {
		return peer_no;
	}
	public void setPeer_no(String peer_no) {
		this.peer_no = peer_no;
	}
	public String getCall_type() {
		return call_type;
	}
	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}
	public String getCall_time() {
		return call_time;
	}
	public void setCall_time(String call_time) {
		this.call_time = call_time;
	}
	public String getRing_time() {
		return ring_time;
	}
	public void setRing_time(String ring_time) {
		this.ring_time = ring_time;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getRelease_time() {
		return release_time;
	}
	public void setRelease_time(String release_time) {
		this.release_time = release_time;
	}
	public String getRelease_dir() {
		return release_dir;
	}
	public void setRelease_dir(String release_dir) {
		this.release_dir = release_dir;
	}
	
	public String getOut_id() {
		return out_id;
	}
	public void setOut_id(String out_id) {
		this.out_id = out_id;
	}
	@Override
	public String toString() {
		return "CallReport [sub_id=" + sub_id + ", call_id=" + call_id + ", phone_no=" + phone_no + ", secret_no="
				+ secret_no + ", peer_no=" + peer_no + ", call_type=" + call_type + ", call_time=" + call_time
				+ ", ring_time=" + ring_time + ", start_time=" + start_time + ", release_time=" + release_time
				+ ", release_dir=" + release_dir + ", out_id=" + out_id + "]";
	}
	
}
