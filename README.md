# vt

Veracode Tools (vt) is a Gradle project that aims to ease effort required to work with Veracode application security scanning activities.

As a set of Gradle tasks, it is meant to be usable either as a command line submission tool or integrated as part of a continuous integration build process.

It helps perform the following tasks:

* Perform Veracode submission for applications.
* Scan results for flaws.
* Perform flaws triage. TODO: not done

## Pre-Requisites

* Veracode account & application to perform scanning.
* Veracode Java API JAR file (copy to `lib` directory).
* JDK 7 is a requirement for Veracode Java API.

## Getting Started

* Clone project.
* Copy `VeracodeJavaAPI.jar` (from Veracode) to a newly created `lib` directory.
* Rename `sample-gradle.properties` to `gradle.properties` and edit its contents to setup Veracode credentials.
* Execute `gradle tasks` to see available tasks.
* Execute `gradle veracodeApplicationList` to see available applications along with its `appId`.

## Typical Veracode Submission Workflow

Sequence of tasks to perform Veracode scan for application involves:

* Download application to scan (manual).
* Install the application (manual).
* `generateToUpload` Prepare JAR files installed by the application for upload. This involves stripping timestamp
information. TODO: rename task to "prepareUpload"?
* `veracodeCreateBuild` Create a new build for application to scan (build and application in this case are Veracode
concepts).
* `veracodeUpload` Upload files to Veracode. This process will upload each file prepared by `generateToUpload` to
Veracode's site. Once a file has been uploaded, it will be deleted. If a network error occurred during upload, just
re-run this task to continue uploading outstanding files.
* `veracodePreScan` Perform pre-scan for current build. This task will take some time, depending on number of files
being processed.
** Wait for pre-scan to complete with the `veracodeBuildInfo` task. It should return "status=Pre-Scan Success".
# `veracodePreScanResults` and `preScanModuleVerify` Once pre-scan has completed successfully, we need to perform a
pre-scan module verification. This task ensures all modules in pre-scan results are accounted for within the
white/black list of modules in `src/apps/${appId}/modules-*.txt`. Any unaccounted module should be manually resolved by
updating the appropriate `modules-*.txt` file.

### Example Sequence of Tasks

Described as Gradle tasks, a typical submission workflow might look like this:

    # Download application from Jenkins (manual).
    # Install the application so that we can scan for files to upload & scan (manual).
    gradle veracodeCreateBuild -PappId=20299 -Pversion="my-product#913"
    gradle generateToUpload -Pdir="C:\temp\my-product#913"
    gradle veracodeUpload -PappId=20299
    gradle veracodePreScan -PappId=20299
    gradle veracodePreScanResults -PappId=20299
    gradle preScanModuleVerify -PappId=20299
    gradle veracodeScan -PappId=20299
    gradle veracodeScanResults -PbuildId=xxxxx OR 
    gradle veracodeScanResultsInCsv -PbuildId=xxxxx

#### Using This Example

To use the example above, change:

- Replace "20299" with the appropriate application ID to process.
    - Use "gradle -q veracodeAppList" to list available applications.
- Replace "my-product" with the appropriate Jenkins job name or other build names if you prefer.
- Replace "913" with the appropriate Jenkins job build number.
- Replace "C:\temp\my-product#913" with the directory where the application is installed.

## Veracode Report

This tool provides the ability to generate CSV reports based on scan results for an application.

1. First get the buildId of the application you want a report on using the `veracodeBuildList` command. The last item in the output represents the latest scan.
2. Then generate the raw CSV flaw report to `build/scan-results.csv` using the `veracodeScanResultsInCsv` command.
3. In addition to getting the flaw report, a more specific report that groups flaw by the module that it was reported from can be generated using the `reportFlawsByTeam` command (filtering managed in `teams.json`). This command has multiple modes:
    - `action` - Report flaws that require action to be taken.
    - `actionSummary` - Similar to `action` mode but more concise.
    - `verbose` - List all flaws.

### Example (Generating Reports)

        C:\github\vt>gradle veracodeBuildList -PappId=20299
        :veracodeBuildList
        22792=xxx build #113
        253467=xxx build #2758
        259771=xxx build #2965
        264509=xxx build #3057
        264853=xxx build #3085
        266517=xxx build #3145
        BUILD SUCCESSFUL
        Total time: 12.094 secs

        C:\github\vt>gradle veracodeScanResultsInCsv -PbuildId=266517
        :veracodeScanResults
        :veracodeScanResultsInCsv
        BUILD SUCCESSFUL
        Total time: 13.105 secs

        C:\github\vt>gradle reportFlawsByTeam -PappId=20299 -Pmode=action
        :reportFlawsByTeam
        ...output... (pipe the output to a file for analysis)

TODO: Someone please help implement `reportFlawsDiff`.

## Tips

## Authentication
Rename `sample-gradle.properties` to `gradle.properties` and enter your Veracode login credentials.

## List Available Tasks
Provide a list of available tasks for this project.

    gradle -q tasks

## Analyze `build/xxx.xml`
Most Veracode related tasks will generate a relevant `build/xxx.xml` file. It might be useful to analyze the contents
of this file to gain additional insight into the task that was just executed.

## Check Pre-Scan Results
This tasks is used to check (Note: this task can take some time to complete):
    gradle -q veracodePreScanResults -PappId=20299

## Check Build Status
Pre-scan can be time consuming. To check the status of a build after pre-scan submission, do this:

    gradle -q veracodeBuildInfo -PappId=20299
    [Build]
            version=my-product#123
            build_id=123
            submitter=Xxx
            platform=Not Specified
            lifecycle_stage=Xxx
            results_ready=false
            policy_name=Xxx
            policy_version=999
            policy_compliance_status=Xxx
            rules_status=Xxx
            grace_period_expired=false
            scan_overdue=false
    [Analysis Unit]
            analysis_type=Static
            *status=Pre-Scan Submitted*

Note: This task can be executed anytime.

## Delete Bad Build
To abandon a build with partially uploaded files or pre-scanned files, execute the delete build task:

    gradle -q veracodeDeleteBuild -PappId=20299

