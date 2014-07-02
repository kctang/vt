class ReportFlawsDiffTask extends VeracodeTask {
	static final String NAME = 'reportFlawsDiff'

	VeracodeScanTask() {
		description = 'Compares veracode report for two builds'
		requiredArguments << 'buildId1' 'buildId2'
	}

	void run() {
		println 'Can you help to implement this? Basically, performing veracodeScanResults on two buildIds and report on:'
        println '  - Flaws in build1 only.'
        println '  - Flaws in build2 only.'
        println '  - Flaws where status changed from build1 to build2.'
        println '  - Think what else useful to know how to follow up.'
        println '  - Format should be in ... probably csv (not sure).'
	}
}