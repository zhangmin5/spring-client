pipeline {
  agent {
    label 'maven'
  }
  stages {
    stage('Login') {
      steps {
        withCredentials(bindings: [usernamePassword(
          		  credentialsId: 'openshift-login-api-token', 
          		  usernameVariable: 'USERNAME',
          		  passwordVariable: 'PASSWORD',
          		)]) {
            sh "oc login ${LOGIN_URL}:${LOGIN_PORT} --token=${PASSWORD}"
          }

        }
      }
      stage('Delete Project') {
        steps {
          sh 'oc delete project springclient-ns'
        }
      }
      stage('Maven Build') {
        steps {
          echo 'Build jar file'
          sh 'mvn clean install -DskipTests=true'
        }
      }
      stage('Run Unit Tests') {
        steps {
          echo 'Run unit tests'
          sh 'mvn test'
        }
      }
      stage('Create Project') {
        steps {
          echo 'Create Project'
          script {
            sh 'oc new-project springclient-ns'
            sh 'oc project springclient-ns'
            echo "Using project: ${openshift.project()}"
          }

        }
      }
      stage('Deploy') {
        steps {
          echo 'Deploy application'
          script {
            sh 'oc new-app --name springclient \'registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.6~https://github.com/remkohdev/spring-client\' --strategy=source --allow-missing-images --build-env=\'JAVA_APP_JAR=hello.jar\''
          }

        }
      }
      stage('Expose') {
        steps {
          echo 'Expose Route'
          script {
            sh 'oc expose svc/springclient'
          }

        }
      }
    }
    environment {
      LOGIN_URL = 'https://c107-e.us-south.containers.cloud.ibm.com'
      LOGIN_PORT = '31608'
    }
  }