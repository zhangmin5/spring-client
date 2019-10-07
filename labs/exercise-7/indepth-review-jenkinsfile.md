# Pipeline Walk-Through

## Syntax
See the [Pipeline Syntax](https://jenkins.io/doc/book/pipeline/syntax/) for a detailed explanation of the syntax of pipelines in Jenkins. You can use the Declarative or the Scripted Syntax, I used the Declarative Syntax in these pipeline, largely based on [Groovy](https://jenkins.io/doc/book/pipeline/syntax/#compare) syntax.

## OpenShift
The base image used to create a Jenkins slave container, includes the `oc` cli. I have mostly used the `oc` cli to communicate with the OpenShift cluster. You can also use the [Openshift Jenkins Client Plugin](https://github.com/openshift/jenkins-client-plugin), which uses an OpenShift plugin that is implemented as a Jenkins Global Variable, its step name, openshift, is a singleton within the context of a job run.

	```script
	openshift.withCluster() {
		
	}
	```

## Jenkins Agent
The Jenkinsfile pipeline starts with defining which image must be used by the current pipeline build, e.g.

	```script
	pipeline {
		agent {
			label 'maven'
		}  
	```

## Setup
In the Setup stage, the pipeline logs into the OpenShift cluster using the credentials defined in the Jenkins Configuration named 'openshift-login-api-token'. The login API token is retrieved from the OpenShift web console. This is not the proper way to identify in the pipeline, because the API token will expire, so in the real environment, you will use the server certificate for instance. 

The [Credentials Binding Plugin](https://jenkins.io/doc/pipeline/steps/credentials-binding/) uses the syntaxt `withCredentials` to bind the token value to the `PASSWORD` variable. There is a long list of bindings available, here the `usernamePassword` binding is used.

	```script
	withCredentials([usernamePassword(
		  credentialsId: 'openshift-login-api-token', 
		  usernameVariable: 'USERNAME',
		  passwordVariable: 'PASSWORD',
		)]) {
	        sh "oc login https://c100-e.us-south.containers.cloud.ibm.com:30403 --token=${PASSWORD}"
	```

The OpenShift endpoint, using the cluster domain and port, is also hardcoded here, probably also not how you would do it in a real environment. This will be updated in a future version, but for a workshop it is not crucial.

I used to have another line here,

	```script
	sh 'oc import-image redhat-openjdk-18/openjdk18-openshift:1.6 --from=registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.6 --confirm'
	```

But it was not working as expected, and I need to revisit. I was expecting it to pull the base image and store it in the local registry, but I think instead it only creates an imageStream for the image. (Like I said, revisiting this as well).

## Delete Project




