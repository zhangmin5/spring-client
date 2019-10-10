# Deploying a Spring Boot App using Pipeline Strategy with Jenkings on Openshift


1. Create a fork of the `spring-client` repository,

	* Go to https://github.com/remkohdev/spring-client,
	* Create a fork in your own GitHub organization, e.g. https://github.com/<username>/spring-client


2. Review Jenkinsfile,

	* Review the Jenkinsfile that is included in the Spring Client repository,
	* If you want an in-depth walk-through of the stages and steps in the Jenkinsfile, go to the [Indepth Review of the Jenkinsfile](indepth-review-jenkinsfile.md),


3. Configure Jenkins

	* Go to the OpenShift web console,
  	* From the logged in user profile dropdown, click the `Copy Login Command`,

		![OpenShift Copy Login Command](../images/openshift-copy-login-command.png)

	* The command should look like,

		```text
		oc login https://c100-e.us-south.containers.cloud.ibm.com:30403 --token=aaHYcMwUyuasfMaS45aWiHfy_Kas5YUa67YTA1AxsNI
		```

	* Copy the OpenShift API token value, e.g. `aaHYcMwUyuasfMaS45aWiHfy_Kas5YUa67YTA1AxsNI`,


	* Go to the Jenkins Administration dashboard, 

		![Jenkins Administration dashboard](../images/jenkins-admin.png)

	* Click `Credentials`, or 
	* Go to Jenkins > Manage Jenkins > Configure Credentials
	* The Jenkinsfile expects the OpenShift API token credential to be available named `openshift-login-api-token`,
	* Go to `Credentials` > `System`,
	* In the `System` view, select the dropdown for `Global credentials (unrestricted)`,

		![Jenkins Add Credentials](../images/jenkins-credentials-system-add.png)

	* Click `Add credentials`,
    	* For `Kind` select `Username with password`,
    	* For `Username` enter `token`,
    	* For `Password` paste the OpenShift API token from the OpenShift web console login command,
    	* For `ID` enter `openshift-login-api-token`, which is the ID that the Jenkinsfile will look for,
    	* For `Description` enter `openshift login api token`,
    	* Click `OK`,

			![Jenkins Add Credentials](../images/jenkins-new-credentials.png)


4. Create a Personal Access Token to Access the Github API

	* Go to your Github account > Settings > Developer settings > [Personal access tokens](https://github.com/settings/tokens),
	* Click `Generate new token`,
	* Under `Note` add `github-access-token-for-jenkins-on-openshift`,
	* Select the scopes for `repo`, `read:repo_hook`, and `user`,
	* Click `Generate token`,
	* Copy the token, we need it to create our Jenkins pipeline,


5. Make sure a project `springclient-ns` exists in OpenShift,

	* Before deploying the `spring-client` application, the Jenkinsfile defines a step to delete and create a project. The delete step causes an error when the project it tries to delete is missing, so make sure the project `springclient-ns` exists in OpenShift,
	* Go to OpenShift > `Cluster Console`,
	* Go to `Administration` > `Projects`,
	* Filter projects by `springclient-ns`,
	* If there is no such project, click `Create Project` to create it,
	* ([See TODOs](../README.md))

6. Create a Multibranch Pipeline using Blue Ocean,

	* In the Jenkins Dashboard, click `Open Blue Ocean` to open the Blue Ocean editor,
	* In the `Welcome to Jenkins` popup window, click the `Create a new Pipeline` button, or click the `New Pipeline` button,

		![Jenkins - Create Pipeline](../images/jenkins-welcome-create-pipeline.png)

	* This will create a new `Multibranch Pipeline`,
	* Select the `GitHub` option,

		![Jenkins - Where do you store your code](../images/jenkins-select-scm.png)

	* In the `Connect to GitHub` section, paste the personal access token you created in your Github account,

		![Jenkins - Select your organization](../images/jenkins-which-org.png)

	* Click `Connect`,
	* Select the organization to where you forked the Spring Client repository,

		![Jenkins - Choose a repository](../images/github-token-choose-repo.png)

	* Search for and select the `spring-client` repo,
	* Click `Create Pipeline`,

	* When the pipeline creation is completed, a build is triggered automatically,

		![Jenkins - Run Pipeline](../images/jenkins-run-pipeline.png)

	* Immediately, a build is triggered,
	* You should see a successful build of the pipeline,

		![Jenkins - Pipeline Success](../images/jenkins-pipeline-success.png)

	* If an error occurs, you can debug the pipeline,
	* A red cross on a stage, will indicate the pipeline broke in that stage, 
	* Unfold the step in the stage, to see the log output,

		![Jenkins - Error 1](../images/jenkins-error-1.png)

		![Jenkins - Error 2](../images/jenkins-error-2.png)

	* Any update to the Github repository, e.g. a push to update the Jenkinsfile, source code of the Spring Boot application, or the README.md file, will trigger a new build of the pipeline,

		![Jenkins - build trigger](../images/jenkins-build-trigger.png)

	* If you're interested, review the pipeline settings:
    	* Click the Configure option,
    	* Review the settings,

## Background 

When a build is run in Jenkins on OpenShift, a new build container is created at each run. Jenkins agent pods, also known as slave pods, are deleted by default after the build completes or is stopped.

The OpenShift Container Platform provides three images suitable for use as [Jenkins slaves](https://docs.openshift.com/container-platform/3.11/using_images/other_images/jenkins_slaves.html): a Base, Maven, and Node.js images. The first is a base image for all Jenkins agents:

* It pulls in both the required tools (headless Java, the Jenkins JNLP client) and the useful ones (including git, tar, zip, and nss among others).
* It establishes the JNLP agent as the entrypoint.
* It includes the oc client tooling for invoking command line operations from within Jenkins jobs.
* It provides Dockerfiles for both CentOS and RHEL images.

Two more images that extend the base image are also provided:
* Maven v3.5 image
* Node.js v8 image

