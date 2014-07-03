class VeracodeDeleteBuildTask extends VeracodeTask {
	static final String NAME = 'veracodeDeleteBuild'

	VeracodeDeleteBuildTask() {
		description = 'Deletes the most recent build, even those that have their scan completed!'
		requiredArguments << 'appId'
	}

	void run() {
		writeXml('build/delete-build.xml', loginUpdate().deleteBuild(project.appId))
	}
}