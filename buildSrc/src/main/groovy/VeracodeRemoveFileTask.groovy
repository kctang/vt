class VeracodeRemoveFileTask extends VeracodeTask {
	static final String NAME = 'veracodeRemoveFile'

	VeracodeRemoveFileTask() {
		description = 'Remove file based on the file id for the application id passed in'
		requiredArguments << 'appId' << 'fileId'
	}

	void run() {
		writeXml('build/remove-file.xml', loginUpdate().removeFile(project.appId, project.fileId))
	}
}