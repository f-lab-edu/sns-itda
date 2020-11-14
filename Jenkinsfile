pipeline {
    agent any
    tools {
        maven 'M3'
    }

    stages {
        stage('Poll') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Unit test') {
            steps {
                sh 'mvn test -DskipITs=true'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Integration test') {
            steps {
                sh 'mvn test -Dsurefire.skip=true'
            }
            post {
                always {
                    junit testResults: '**/target/failsafe-reports/*.xml', allowEmptyResults: true
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }

    post {
        failure {
            mail to: 'cyj199637@gmail.com',
            subject: 'Jenkins Pipeline Build Fail',
            body: 'Somthing Errors'
        }
    }
}
