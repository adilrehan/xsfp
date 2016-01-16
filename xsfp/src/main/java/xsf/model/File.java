package xsf.model;

import java.util.Date;

public class File {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Date getAccessed() {
		return accessed;
	}
	public void setAccessed(Date accessed) {
		this.accessed = accessed;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getPackageIndex() {
		return packageIndex;
	}
	public void setPackageIndex(String packageIndex) {
		this.packageIndex = packageIndex;
	}
	public String getArchType() {
		return archType;
	}
	public void setArchType(String archType) {
		this.archType = archType;
	}
	public String getExeType() {
		return exeType;
	}
	public void setExeType(String exeType) {
		this.exeType = exeType;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMsDos() {
		return msDos;
	}
	public void setMsDos(String msDos) {
		this.msDos = msDos;
	}

	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getDosName() {
		return dosName;
	}
	public void setDosName(String dosName) {
		this.dosName = dosName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	public String getFileVersion() {
		return fileVersion;
	}
	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getInternalName() {
		return internalName;
	}
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	public String getFileDescription() {
		return fileDescription;
	}
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	public String getLegalCopyright() {
		return legalCopyright;
	}
	public void setLegalCopyright(String legalCopyright) {
		this.legalCopyright = legalCopyright;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("File [name=");
		builder.append(name);
		builder.append(", created=");
		builder.append(created);
		builder.append(", modified=");
		builder.append(modified);
		builder.append(", accessed=");
		builder.append(accessed);
		builder.append(", size=");
		builder.append(size);
		builder.append(", attribute=");
		builder.append(attribute);
		builder.append(", Signature=");
		builder.append(Signature);
		builder.append(", packageIndex=");
		builder.append(packageIndex);
		builder.append(", archType=");
		builder.append(archType);
		builder.append(", exeType=");
		builder.append(exeType);
		builder.append(", versionId=");
		builder.append(versionId);
		builder.append(", flag=");
		builder.append(flag);
		builder.append(", msDos=");
		builder.append(msDos);
		builder.append(", languageCode=");
		builder.append(languageCode);
		builder.append(", realName=");
		builder.append(realName);
		builder.append(", dosName=");
		builder.append(dosName);
		builder.append(", description=");
		builder.append(description);
		builder.append(", productVersion=");
		builder.append(productVersion);
		builder.append(", fileVersion=");
		builder.append(fileVersion);
		builder.append(", originalFilename=");
		builder.append(originalFilename);
		builder.append(", companyName=");
		builder.append(companyName);
		builder.append(", productName=");
		builder.append(productName);
		builder.append(", internalName=");
		builder.append(internalName);
		builder.append(", fileDescription=");
		builder.append(fileDescription);
		builder.append(", legalCopyright=");
		builder.append(legalCopyright);
		builder.append("]");
		return builder.toString();
	}

	private String name;
	private Date created;
	private Date modified; 
	private Date accessed;
	private int size;
	private String attribute;
	private String Signature;
	private String packageIndex;
	private String archType;
	private String exeType;
	private String versionId;
	private String flag;
	private String msDos;	
	
	private String	languageCode;
	private String	realName;
	private String	dosName;
	private String	description;
	private String	productVersion;
	private String	fileVersion;
	private String	originalFilename;
	private String	companyName;
	private String	productName;
	private String	internalName;
	private String	fileDescription;
	private String	legalCopyright;

	

}
