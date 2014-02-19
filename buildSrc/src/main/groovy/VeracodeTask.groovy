import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GFileUtils
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper

abstract class VeracodeTask extends DefaultTask {
	final static String OPTIONAL = '-optional'
	final static def validArguments = [
    'appId': '123',
    'buildId': '123',
    'buildName': 'xxx',
    'dir': 'xxx',
    'force': 'force',
    'fileId': '123',
    'mode': 'action|actionSummary|verbose',
    'maxUploadAttempts': '123',
    'fileId': 'xxx' ]

	def requiredArguments = []
	VeracodeUser veracodeUser

	VeracodeTask() {
		group = 'Veracode'
	}

	abstract void run()

	final String correctUsage() {
		StringBuilder sb = new StringBuilder("Example of usage: gradle ${getName()}")
		requiredArguments.each() {
			if (!isArgumentOptional(it)) {
				sb.append(" -P${it}=${validArguments.get(it)}")
			} else {
				String originalArgument = it.substring(0, it.length() - OPTIONAL.length())
				sb.append(" [-P${originalArgument}=${validArguments.get(originalArgument)}]")
			}
		}

		sb.toString()
	}

	final boolean haveRequiredArguments() {
		boolean haveRequiredArguments = true
		requiredArguments.each() {
			if (!isArgumentOptional(it)) {
				haveRequiredArguments &= getProject().hasProperty(it)
			}
		}

		if (!haveRequiredArguments) {
			println correctUsage()
		}

		return haveRequiredArguments
	}

	@TaskAction
	final def vExecute() { if (haveRequiredArguments()) run() }

	// === utility methods ===
	protected boolean isArgumentOptional(String arg) {
		arg.endsWith(OPTIONAL)
	}

	protected UploadAPIWrapper loginUpdate() {
	    UploadAPIWrapper update = new UploadAPIWrapper()
	    update.setUpCredentials(veracodeUser.username, veracodeUser.password)
	    return update
	}

	protected ResultsAPIWrapper loginResults() {
	    ResultsAPIWrapper results = new ResultsAPIWrapper()
	    results.setUpCredentials(veracodeUser.username, veracodeUser.password)
	    return results
	}

	protected Node writeXml(String filename, String content) {
	    GFileUtils.writeFile(content, new File(filename))
	    new XmlParser().parseText(content)
	}

	protected def readXml(String filename) {
    new XmlParser().parseText(GFileUtils.readFile(new File(filename)))
}

	protected List readListFromFile(File file) {
	    def set = new HashSet<Set>();
	    file.eachLine { line ->
	        if (set.contains(line)) {
	            println "ERROR: duplicate line: [$line]"
	        }
	        set.add(line)
	    }
	    return new ArrayList<String>(set)
	}

	static class VeracodeUser {
		String username
		String password
	}
}