drop table XSF_FILE;

create table XSF_FILE(
	hostname 				varchar(256),
	dir_name 				varchar(256), 
	file_name				varchar(256), 
	
	dir_date				timestamp,
	dir_attr				varchar(9), 
	dir_link				varchar(256),
	dir_contains			varchar(20),
	dir_msdos				varchar(256),

	file_created			timestamp,
	file_modified			timestamp, 
	file_accessed			timestamp,
	file_size				numeric,
	file_attr				varchar(9), 
	file_signature			varchar(20), 
	file_versionid			varchar(10), 
	file_pkgindex			varchar(120),
	file_arctype			varchar(20),
	file_exetype			varchar(20), 
	file_flag				varchar(1), 
	file_msdos				varchar(12),

	
	file_languageCode		varchar(10),
	file_realName			varchar(256),
	file_dosName			varchar(12),
	file_description		varchar(50),
	file_productVersion		varchar(100),
	file_fileVersion		varchar(100),
	file_originalFilename	varchar(256),
	file_companyName		varchar(100),
	file_productName		varchar(100),
	file_internalName		varchar(100),
	file_fileDescription	varchar(256),
	file_legalCopyright		varchar(256)
	
);

CREATE INDEX file_hostname ON XSF_FILE( hostname );

CREATE INDEX file_file_name ON XSF_FILE( file_name );
