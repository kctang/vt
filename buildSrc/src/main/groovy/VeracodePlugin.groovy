import org.gradle.api.Plugin
import org.gradle.api.Project

class VeracodePlugin implements Plugin<Project> {
	void apply(Project project) {
		project.extensions.create('veracodeUser', VeracodeTask.VeracodeUser)

		project.task(VeracodeAppListTask.NAME, type: VeracodeAppListTask)
		project.task(VeracodeAppInfoTask.NAME, type: VeracodeAppInfoTask)
		project.task(VeracodeBuildListTask.NAME, type: VeracodeBuildListTask)
		project.task(VeracodeBuildInfoTask.NAME, type: VeracodeBuildInfoTask)
		project.task(VeracodeFileListTask.NAME, type: VeracodeFileListTask)
		project.task(VeracodeCreateBuildTask.NAME, type: VeracodeCreateBuildTask)
		project.task(VeracodeDeleteBuildTask.NAME, type: VeracodeDeleteBuildTask)
		project.task(GenerateToUploadTask.NAME, type: GenerateToUploadTask)
		project.task(VeracodeUploadTask.NAME, type: VeracodeUploadTask)
		project.task(VeracodePreScanTask.NAME, type: VeracodePreScanTask)
		project.task(VeracodePreScanResultsTask.NAME, type: VeracodePreScanResultsTask)
		project.task(PreScanModuleVerifyTask.NAME, type: PreScanModuleVerifyTask)
		project.task(VeracodeScanTask.NAME, type: VeracodeScanTask)
		project.task(VeracodeScanResultsTask.NAME, type: VeracodeScanResultsTask)
		project.task(VeracodeScanResultsInCsvTask.NAME, type: VeracodeScanResultsInCsvTask)
		project.task(VeracodeRemoveFileTask.NAME, type: VeracodeRemoveFileTask)
		project.task(ReportFlawsByTeamTask.NAME, type: ReportFlawsByTeamTask)
		project.task(ReportFlawsDiffTask.NAME, type: ReportFlawsDiffTask)
		project.configure(project.getTasks()) {
			it.veracodeUser = project.veracodeUser
		}
	}
}