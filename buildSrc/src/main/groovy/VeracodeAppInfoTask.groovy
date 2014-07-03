class VeracodeAppInfoTask extends VeracodeTask {
	static final String NAME = 'veracodeAppInfo'

	VeracodeAppInfoTask() {
		description = 'Lists application information based on the application id passed in'
		requiredArguments << 'appId'
	}

	void run() {
		writeXml('build/app-info.xml', loginUpdate().getAppInfo(project.appId)
        ).application[0].attributes().each() { k, v ->
            println "$k=$v"
        }
	}
}