node ('master') {
    stage('Poll') {
        checkout scm
    }

	stage('Build & Unit test') {
	    withMaven(maven: 'M3') {
            sh 'mvn clean verify -DskipITs=true'
            junit '**/target/surefire-reports/Test-*.xml'
            archive 'target/*.jar'
        }
	}

	stage('Integration test') {
	    withMaven(maven: 'M3') {
    	    sh 'mvn clean verify -Dsurefire.skip=true'
    	    junit '**/target/surefire-reports/Test-*.xml'
            archive 'target/*.jar'
        }
    }
}
