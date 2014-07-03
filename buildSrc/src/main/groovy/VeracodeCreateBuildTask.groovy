class VeracodeCreateBuildTask extends VeracodeTask {
	static final String NAME = 'veracodeCreateBuild'

	VeracodeCreateBuildTask() {
		description = 'Creates a new build under the application id passed in, with the buildName as the identifier for the new build'
		requiredArguments << 'appId' << 'buildName'
	}

	void run() {
		Node buildInfo = writeXml(
			'build/create-build.xml',
			loginUpdate().createBuild(project.appId, project.buildName))
        if (buildInfo.name().equals('error')) {
            println buildInfo.text()
        } else {
            println '[Build]'
            buildInfo.build[0].attributes().each() { k, v ->
                println "\t$k=$v"
            }
            println '[Analysis Unit]'
            buildInfo.build[0].children()[0].attributes().each { k, v ->
                println "\t$k=$v"
            }
        }
	}
}