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

        stage('Build Docker Image') {
            steps {
                script {
                    image = docker.build('cyj199637/sns-itda')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script{
                    docker.withRegistry('https://registry.hub.docker.com/', 'docker-hub')
                    image.push('latest')
                }
            }
        }

        stage('Remove Docker Image') {
            steps {
                sh 'docker rmi cyj199637/sns-itda'
                sh 'registry.hub.docker.com/cyj199637/sns-itda:latest'
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
                                    execCommand: "sh ~/scripts/deploy-docker.sh"
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
