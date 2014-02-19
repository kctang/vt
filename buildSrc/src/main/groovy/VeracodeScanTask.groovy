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

        writeXml('build/scan.xml', loginUpdate().beginScan(project.appId, moduleIds.join(","), 'false'))
	}
}