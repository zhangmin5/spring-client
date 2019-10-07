pipeline {
	agent {
		label 'maven'
	}  
	stages {
	  stage('Setup') {
        steps {
        	withCredentials([usernamePassword(
	          	credentialsId: 'openshift-login-api-token', 
	          	usernameVariable: 'USERNAME',
	        	passwordVariable: 'PASSWORD',
	        )]) {
            	script {
                  sh "oc login https://c100-e.us-south.containers.cloud.ibm.com:30403 --token=${PASSWORD}"
                  sh 'oc import-image redhat-openjdk-18/openjdk18-openshift:1.6 --from=registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.6 --confirm'
                }
	        }
            script {
              openshift.withCluster() {
                echo "Using project: ${openshift.project()}"
              }
            }
        }
      }
      stage('Clean Project') {
        steps {
          withCredentials([usernamePassword(
          	credentialsId: 'openshift-login-api-token', 
          	usernameVariable: 'USERNAME',
        	passwordVariable: 'PASSWORD',
          )]) {
            	script {
                  sh "oc login https://c100-e.us-south.containers.cloud.ibm.com:30403 --token=${PASSWORD}"
                  sh 'oc delete project springclient-ns'
                }
          }
        }
      }
	  stage('Maven Build') {
		steps {
			echo 'Build jar file'
			sh 'mvn clean install -DskipTests=true'
		}
	  }
	  stage('Unit Tests') {
		steps {
			echo 'Run unit tests'
			sh 'mvn test'
		}
	  }
	  stage('Create Project') {
	  	steps {
	  		script {
	  			openshift.withCluster() {
	  				sh 'oc new-project springclient-ns'
	  				sh 'oc project springclient-ns'
	  			}
	  		}
	  	}
	  }
	  stage('Deploy') {
		steps {
			echo 'Deploy application'
			script {
                openshift.withCluster() {
                  sh 'oc new-app --name springclient \'registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.6~https://github.com/remkohdev/spring-client\' --strategy=source --allow-missing-images --build-env=\'JAVA_APP_JAR=hello.jar\''
                }
            }
		}
	  }
	  stage('Expose') {
		steps {
			echo 'Expose Route'
			script {
                openshift.withCluster() {
                	  sh 'oc expose svc/springclient'
                }
            }
		}
	  }
	}
}
