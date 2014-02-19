class VeracodeScanResultsInCsvTask extends VeracodeTask {
	static final String NAME = 'veracodeScanResultsInCsv'

	VeracodeScanResultsInCsvTask() {
		description = 'Gets the Veracode scan results based on the build id passed in and convert it to csv format'
	}

	void run() {
		File csvFile = new File('build/scan-results.csv')
	    csvFile.newWriter()
	    csvFile << ["Issue Id",
	            "Severity",
	            "Exploit Level",
	            "CWE Id",
	            "CWE Name",
	            "Module",
	            "Source",
	            "Source File Path",
	            "Line",
	            "Remediation Status",
	            "Mitigation Status",
	            "Mitigation Action",
	            "Mitigation Description",
	            "Mitigation Date"].join(",") + "\n"

	    readXml('build/scan-results.xml').severity.each() { severity ->
	        severity.category.each() { category ->
	            category.cwe.each() { cwe ->
	                cwe.staticflaws.flaw.each() { flaw ->
	                    def row = [flaw.@issueid,
	                            flaw.@severity,
	                            flaw.@exploitLevel,
	                            cwe.@cweid,
	                            cwe.@cwename,
	                            flaw.@module,
	                            flaw.@sourcefile,
	                            flaw.@sourcefilepath,
	                            flaw.@line,
	                            flaw.@remediation_status,
	                            flaw.@mitigation_status_desc,
	                            flaw.mitigations?.mitigation[0]?.@action,
	                            flaw.mitigations?.mitigation[0]?.@description,
	                            flaw.mitigations?.mitigation[0]?.@date]
	                            .collect { '"' + (it == null ? "" : it.replace('"', '""')) + '"' }
	                    def rowString = row.join(',')
	                    csvFile << rowString + "\n"
	                }
	            }
	        }
	    }
	}
}