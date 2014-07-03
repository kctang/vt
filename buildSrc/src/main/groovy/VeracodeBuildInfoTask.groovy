class VeracodeBuildInfoTask extends VeracodeTask {
	static final String NAME = 'veracodeBuildInfo'

	VeracodeBuildInfoTask() {
		description = "Lists latest build information for the applicaiton id passed in. If a build id is provided, that build's information will be listed instead"
		requiredArguments << 'appId' << "buildId${VeracodeTask.OPTIONAL}"
	}

	void run() {
		String xmlResponse
        if (project.hasProperty('buildId')) {
            xmlResponse = loginUpdate().getBuildInfo(project.appId, project.buildId)
        } else {
            xmlResponse = loginUpdate().getBuildInfo(project.appId)
        }
        Node buildInfo = writeXml('build/build-info.xml', xmlResponse) // need to print twice, so assign var
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