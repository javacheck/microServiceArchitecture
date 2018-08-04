package cn.lastmiles.bean;
/**
 * createDate : 2015-07-15 AM 10:00
 */
import java.util.Date;

public class APK {
	private Long id;
	private String name;
	private String version;
	private String memo;
	private Date createdTime;
	private String fileId;
	private int type;//0随身社区APP  1商户APP  2POSAPP  3应用中心
	private int needingUpdate;//1强制更新，0不需要更新，可以使用旧版本
	
	@Override
	public String toString() {
		return "APK [id=" + id + ", name=" + name + ", version=" + version
				+ ", memo=" + memo + ", createdTime=" + createdTime
				+ ", fileId=" + fileId + ", type=" + type + ", needingUpdate="
				+ needingUpdate + "]";
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public int getNeedingUpdate() {
		return needingUpdate;
	}

	public void setNeedingUpdate(int needingUpdate) {
		this.needingUpdate = needingUpdate;
	}
}