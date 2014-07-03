class PreScanModuleVerifyTask extends VeracodeTask {
	static final String NAME = 'preScanModuleVerify'

	PreScanModuleVerifyTask() {
		description = 'Verifies that all jars uploaded to Veracode are categorized in the blacklists/whitelist'
		requiredArguments << 'appId'
	}

	void run() {
		println "Verifying against modules list modules-*.txt..."
        println ""

        def blackListErr = readListFromFile(new File("src/apps/${project.appId}/modules-blacklist-error.txt"))
        def blackListExt = readListFromFile(new File("src/apps/${project.appId}/modules-blacklist-external.txt"))
        def blackListInt = readListFromFile(new File("src/apps/${project.appId}/modules-blacklist-internal.txt"))
        def whiteList = readListFromFile(new File("src/apps/${project.appId}/modules-whitelist.txt"))

        def allList = blackListErr + blackListExt + blackListInt + whiteList
        allList = allList.sort()

        println "modules-blacklist-error = ${blackListErr.size()}"
        println "modules-blacklist-external = ${blackListExt.size()}"
        println "modules-blacklist-internal = ${blackListInt.size()}"
        println "modules-whitelist = ${whiteList.size()}"
        println "all lists total = ${allList.size()}"
        println ""

        def notFoundList = new ArrayList<String>()
        def fatalErrorList = new ArrayList<String>()
        readXml('build/pre-scan-results.xml').each() { module ->
            if (!allList.contains(module.@name)) {
                notFoundList << module.@name
            }

            if (module.@has_fatal_errors.equals("true") && whiteList.contains(module.@name)) {
                fatalErrorList << module.@name
            }
        }

        if (notFoundList.isEmpty() && fatalErrorList.isEmpty()) {
            println "Prescan module verification success."
        } else {
            println "Prescan module verification failed. DO NOT PROCEED WITH VERACODE SCAN!"
            if (!notFoundList.isEmpty()) {
                println "Module not found in all list:"
                notFoundList.each { notFoundModule ->
                    println "${notFoundModule}"
                }
            }

            if (!fatalErrorList.isEmpty()) {
                println "Module in fatal error but in whitelist:"
                fatalErrorList.each { fatalErrorModule ->
                    println "${fatalErrorModule}"
                }
            }
        }
	}
}