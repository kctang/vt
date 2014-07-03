class VeracodeScanTask extends VeracodeTask {
	static final String NAME = 'veracodeScan'

	VeracodeScanTask() {
		description = 'Starts a Veracode scan for the application id passed in'
		requiredArguments << 'appId'
	}

	void run() {
		def moduleIds = []
        def whiteList = readListFromFile(new File("src/apps/${project.appId}/modules-whitelist.txt"))
        readXml('build/pre-scan-results.xml').each() { module ->
            if (whiteList.contains(module.@name)) {
                moduleIds << module.@id
            }
        }
        println "Modules in whitelist: ${whiteList.size()}"
        println "Modules selected: ${moduleIds.size()}"
        if(whiteList.size() != moduleIds.size()) {
            println 'WARNING: Not all the files in whitelist are being scanned. Some modules no longer exist? Manual whitelist maintenance should be performed.'
        }

        writeXml('build/scan.xml', loginUpdate().beginScan(project.appId, moduleIds.join(","), 'false'))
	}
}