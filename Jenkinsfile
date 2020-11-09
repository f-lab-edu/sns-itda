pipeline {
    agent any
    tools {
        maven 'M3'
    }

    stages {
        stage('Poll') {
            checkout scm
        }

        stage('Build & Unit test') {
            sh 'mvn clean test -DskipITs=true'
            junit '**/target/surefire-reports/Test-*.xml'
            archive 'target/*.jar'
        }

        stage('Integration test') {
            sh 'mvn clean test -Dsurefire.skip=true'
            junit '**/target/surefire-reports/Test-*.xml'
            archive 'target/*.jar'
        }
    }

    post {
        failure {
            mail to: cyj199637@gmail.com, subject: 'Jenkins Pipeline Build Fail'
        }
    }
}
