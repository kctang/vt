class VeracodeBuildListTask extends VeracodeTask {
	static final String NAME = 'veracodeBuildList'

	VeracodeBuildListTask() {
		description = 'Lists builds that are under the apllication id passed in'
		requiredArguments << 'appId'
	}

	void run() {
		writeXml('build/build-list.xml', loginUpdate().getBuildList(project.appId)
        ).each() { build ->
            println "${build.@build_id}=${build.@version}"
        }
	}
}