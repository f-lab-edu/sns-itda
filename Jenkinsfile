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
                archiveArtifacts 'target/*.jar'
            }
        }

        stage('Unit test') {
            steps {
                sh 'mvn test -DskipITs=true'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
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
                }
            }
        }

        stage('Deploy') {
            steps([$class: 'BapSshPromotionPublisherPlugin']) {
                sshPublisher(
                    continueOnError: false, failOnError: true,
                    publishers: [
                        sshPublisherDesc(
                            configName: "server-deploy",
                            verbose: true,
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "",
                                    removePrefix: "",
                                    remoteDirectory: "",
                                    execCommand: "./deploy.sh"
                                )
                            ]
                        )
                    ]
                )
            }
        }
    }

    post {
        failure {
            mail to: "cyj199637@gmail.com",
            subject: "FAILURE: Job '${env.JOB_NAME} [${env.BRANCH_NAME}] [${env.BUILD_NUMBER}]'",
            body: """
                  Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BRANCH_NAME}] [${env.BUILD_NUMBER}]</a>"
                  """
        }
    }
}
