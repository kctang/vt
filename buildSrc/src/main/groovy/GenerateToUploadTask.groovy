class GenerateToUploadTask extends VeracodeTask {
	static final String NAME = 'generateToUpload'

	GenerateToUploadTask() {
		description = "Grabs all jar files from the dir provided and filter it into a the 'build/to-upload' folder"
		requiredArguments << 'dir' << "force${VeracodeTask.OPTIONAL}"
	}

	void run() {
		List<File> files = []

		// prevent uploading if "to-upload" is not empty unless user force
        if (!project.hasProperty('force')) {
            File toUploadDir = new File('build/to-upload')
            if (toUploadDir.isDirectory() && toUploadDir.list().size() > 0) {
                // to-upload is not empty
                println 'Directory "build/to-upload" is not empty. Cannot proceed.'
                return
            }
        }

        // get files to process
        new File((String) project.dir).eachFileRecurse() { file ->
            if (file.isFile() && file.name.toLowerCase().endsWith(".jar")) {
                files.add file
            }
        }

        // copy to build directory
        for (File file : files) {
            project.copy {
                from file
                into 'build/to-upload'

                // strip timestamp
                rename { String fileName ->
                    fileName.replaceAll(/(.+)-\d{8}.\d{6}-\d+/, '$1')
                }
            }
        }

        println "${files.size()} file(s) copied to build/to-upload"
	}
}