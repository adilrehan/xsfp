package xsf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Directory {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getContains() {
		return contains;
	}
	public void setContains(String contains) {
		this.contains = contains;
	}
	public String getMsDos() {
		return msDos;
	}
	public void setMsDos(String msDos) {
		this.msDos = msDos;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}

	public void addFile( File file ) {
		files.add(file);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Directory [name=");
		builder.append(name);
		builder.append(", date=");
		builder.append(date);
		builder.append(", attribute=");
		builder.append(attribute);
		builder.append(", link=");
		builder.append(link);
		builder.append(", contains=");
		builder.append(contains);
		builder.append(", msDos=");
		builder.append(msDos);
		builder.append(", files=");
		builder.append(files);
		builder.append("]");
		return builder.toString();
	}


	private String name;
	private Date date;
	private String attribute;
	private String link;
	private String contains;
	private String msDos;
	private List<File> files = new ArrayList<File>();

}
