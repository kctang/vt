class VeracodeFileListTask extends VeracodeTask {
	static final String NAME = 'veracodeFileList'

	VeracodeFileListTask() {
		description = "Lists all files under the latest build for the application id passed in. If a build id is provided, the files under that build will be listed instead"
		requiredArguments << 'appId' << "buildId${VeracodeTask.OPTIONAL}"
	}

	void run() {
		String xmlResponse
        if (project.hasProperty('buildId')) {
            xmlResponse = loginUpdate().getFileList(project.appId, project.buildId)
        } else {
            xmlResponse = loginUpdate().getFileList(project.appId)
        }
        Node filelist = writeXml('build/file-list.xml', xmlResponse)
        filelist.each() { file ->
            println "${file.@file_name}=${file.@file_status}"
        }
        println ''
        println 'Total files = ' + filelist.children().size()
	}
}