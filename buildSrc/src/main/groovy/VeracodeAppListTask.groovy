class VeracodeAppListTask extends VeracodeTask {
	static final String NAME = 'veracodeAppList'

	VeracodeAppListTask() {
		description = 'Lists all applictions from the Veracode site with Application number to Application name mapping'
	}

	void run() {
		writeXml('build/app-list.xml', loginUpdate().getAppList())
		.each() { app ->
        	println app.@app_id + '=' + app.@app_name
    	}
	}
}