class VeracodePreScanResultsTask extends VeracodeTask {
	static final String NAME = 'veracodePreScanResults'

	VeracodePreScanResultsTask() {
		description = 'Gets the results for Veracode pre-scan for the application id passed in'
		requiredArguments << 'appId' << "buildId${OPTIONAL}"
	}

	void run() {
		String xmlResponse
        if (project.hasProperty('buildId')) {
            xmlResponse = loginUpdate().getPreScanResults(project.appId, project.buildId)
        } else {
            xmlResponse = loginUpdate().getPreScanResults(project.appId)
        }

        Node result = writeXml('build/pre-scan-results.xml', xmlResponse)
        println ((result.name().equals('error')) ? result.text() : 'Pre-scan completed')
	}
}