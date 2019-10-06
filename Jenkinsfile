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
                  sh 'oc import-image redhat-openjdk-openshift:1.6 --from=registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.6 --confirm'
                }
	        }
            script {
              openshift.withCluster() {
                echo "Using project: ${openshift.project()}"
              }
            }
        }
      }
      stage('Clean') {
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
	  stage('Checkout') {
		steps {
			echo 'Checkout source code'
			checkout scm
			script {
			  def pom = readMavenPom file: 'pom.xml'
			  def version = pom.version
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
	  			}
	  		}
	  	}
	  }
	  stage('Deploy') {
		steps {
			echo 'Deploy application'
			script {
                openshift.withCluster() {
                	  sh 'oc project springclient-ns'
                	  sh 'oc new-app \'redhat-openjdk-18/openjdk18-openshift:1.6~https://github.com/remkohdev/spring-client\' --allow-missing-images --strategy=source --build-env=\'JAVA_MAIN_CLASS=hello.Application\' -l app=springclient'
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
