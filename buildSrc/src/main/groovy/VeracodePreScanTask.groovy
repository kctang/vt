class VeracodePreScanTask extends VeracodeTask {
	static final String NAME = 'veracodePreScan'

	VeracodePreScanTask() {
		description = 'Start Veracode pre-scan for the application id passed in'
		requiredArguments << 'appId'
	}

	void run() {
		writeXml('build/pre-scan.xml', loginUpdate().beginPreScan(project.appId))
        println 'Check build/pre-scan.xml for response status.'
	}
}