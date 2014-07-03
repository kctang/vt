class VeracodeScanResultsTask extends VeracodeTask {
	static final String NAME = 'veracodeScanResults'

	VeracodeScanResultsTask() {
		description = 'Gets the Veracode scan results based on the build id passed in'
		requiredArguments << 'buildId'
	}

	void run() {
		String xmlResponse = loginResults().detailedReport(project.buildId)
        writeXml('build/scan-results.xml', xmlResponse)
	}
}