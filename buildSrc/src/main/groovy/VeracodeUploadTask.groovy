import groovy.io.FileType
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper

class VeracodeUploadTask extends VeracodeTask {
	static final String NAME = 'veracodeUpload'

	VeracodeUploadTask() {
		description = "Uploads all files from 'build/to-upload' folder to Veracode based on the application id provided"
		requiredArguments << 'appId' << "maxUploadAttempts${VeracodeTask.OPTIONAL}"
	}

	void run() {
		String xmlResponse = ''
		UploadAPIWrapper update = loginUpdate()
		File uploadFolder = new File('build/to-upload')
		def error
        def tries = 1;
        def maxTries = Integer.parseInt((hasProperty('maxUploadAttempts') ? maxUploadAttempts : '10'))

        while (uploadFolder.list().length > 0 && (tries <= maxTries || maxTries == 0)) {
            println '\\/----------\\/----------\\/----------\\/----------\\/'
            println "Take ${tries}"
            println "Maximum upload attempts = ${maxTries} (0 means until the end of the world as we know it)"
            println ''

            def fileList = []
            uploadFolder.eachFileRecurse(FileType.FILES) { file ->
                fileList << file
            }

            // upload each file in build/to-upload
            for (File file : fileList) {
                try {
                    xmlResponse = update.uploadFile(project.appId, file.absolutePath)
                    project.delete file.absolutePath
                    println "Processed $file.name"
                } catch (Exception e) {
                	println ''
                    println e
                    println ''
                    println "Upload failing at take ${tries}"
                    println '/\\----------/\\----------/\\----------/\\----------/\\'
                    println ''

                    // write output of last upload
                    writeXml("build/upload-file.xml", xmlResponse)
                    error = e

                    sleep(5000)
                    ++tries

                    break
                }
            }
        }

        if (tries > maxTries) {
            println "Exceeded maximum upload attempt : ${maxTries}"
            throw error
        }

        println 'Check build/upload-file.xml for status of uploaded files.'
    }
}